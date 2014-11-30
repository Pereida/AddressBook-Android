package mcc.mcccontacts;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.ArrayList;


import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class MainActivity extends Activity {

    private ListView mainListView;
    private ArrayAdapter<Contact> listAdapter;
    private ArrayList<Contact> alContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListView = (ListView) findViewById( R.id.lvContacts );

        mainListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                // ListView Clicked item value
                Contact itemValue = (Contact) mainListView.getItemAtPosition(position);

                // Change Activity
                Intent intent = new Intent(MainActivity.this, ContactView.class);
                intent.putExtra("fname", itemValue.getName());
                intent.putExtra("lname", itemValue.getLastName());
                intent.putExtra("phone", itemValue.getPhoneNumber());
                intent.putExtra("mobile", itemValue.getMobileNumber());
                intent.putExtra("addr", itemValue.getAddress());
                intent.putExtra("email", itemValue.getEmail());
                intent.putExtra("id", itemValue.getId());

                startActivity(intent);

            }
        });

        alContact = new ArrayList<Contact>();
        listAdapter = new ArrayAdapter<Contact>(this, R.layout.simplerow, alContact);
        mainListView.setAdapter(listAdapter);

        UpdateList();
    }

    private void UpdateList()
    {
        (new RetrieveContacts()).execute("get");
    }

    private void AddContact(String fname, String lname, String phone,
                            String mobile, String email, String address) {

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("first_ name", fname);
            jsonParam.put("last_name", lname);
            jsonParam.put("phone", phone);
            jsonParam.put("mobile", mobile);
            jsonParam.put("email", email);
            jsonParam.put("address", address);

            (new RetrieveContacts()).execute("post", jsonParam.toString());

        } catch (Exception ex)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Cannot add contact."
                    , Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //
        switch(id) {
            case R.id.action_refresh:
                UpdateList();
                return true;
            case R.id.action_add:
                Intent i = new Intent(this, AddContact.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class RetrieveContacts extends AsyncTask<String, Void, List<Contact>>{

        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        private String task;
        @Override
        protected void onPostExecute(List<Contact> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(this.task == "get") {
                listAdapter.clear();
                if (result != null)
                    listAdapter.addAll(result);
                listAdapter.notifyDataSetChanged();
            } else if (this.task == "post")
            {
                if(result != null){
                    dialog.setMessage("Contact added!");
                } else {
                    dialog.setMessage("Error");
                }
                dialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Processing...");
            dialog.show();
        }

        private List<Contact> doGet()
        {
            List<Contact> result = new ArrayList<Contact>();

            try {
                URL u = new URL(getString(R.string.RESTURI));

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");

                conn.connect();
                InputStream is = conn.getInputStream();

                // Read the stream
                byte[] b = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(b) != -1)
                    baos.write(b);

                String JSONResp = new String(baos.toByteArray());
                JSONObject JObject = new JSONObject(JSONResp);

                JSONArray JArr = JObject.getJSONArray("contacts");

                for (int i=0; i < JArr.length(); i++) {
                    result.add(convertContact(JArr.getJSONObject(i)));
                }

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        protected List<Contact> doInBackground(String... params) {
            this.task = params[0];
            if(this.task == "get") {
                return doGet();
            } else if (this.task == "post") {
                return doPost(params[1]);
            }
            return null;
        }

        private List<Contact> doPost(String param) {
            List<Contact> result = new ArrayList<Contact>();

            try {
                URL u = new URL(getString(R.string.RESTURI));

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");

                conn.connect();

                // Write to the stream
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeUTF(URLEncoder.encode(param, "UTF-8"));
                os.flush();
                os.close();


                // Read the stream
                InputStream is = conn.getInputStream();
                byte[] b = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(b) != -1)
                    baos.write(b);

                String JSONResp = new String(baos.toByteArray());
                JSONObject JObject = new JSONObject(JSONResp);

                int success;
                success = JObject.getInt("success");

                if(success != 1)
                {
                    result = null;
                }

                return result;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }

        private Contact convertContact(JSONObject obj) throws JSONException {
            String firstName = obj.getString("first_name");
            String lastName = obj.getString("last_name");
            String email = obj.getString("email");
            String phoneNumber = obj.getString("phone");
            String mobile = obj.getString("mobile");
            String address = obj.getString("address");
            String id = obj.getString("_id");

            return new Contact(firstName, lastName, email, phoneNumber, mobile, address, id);
        }
    }


}
