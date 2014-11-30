package mcc.mcccontacts;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
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

                startActivityForResult(intent, 1);

            }
        });

        alContact = new ArrayList<Contact>();
        listAdapter = new ArrayAdapter<Contact>(this, R.layout.simplerow, alContact);
        mainListView.setAdapter(listAdapter);

        UpdateList();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("FIRST", "result:" + resultCode);
        Log.d("FIRST", "request:" + requestCode);
        switch(requestCode) {
            case 1:
                if (resultCode == 0) {
                    UpdateList();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK)
                {
                    Bundle extras = data.getExtras();

                    AddContact(extras.getString("fname"), extras.getString("lname"),
                            extras.getString("phone"), extras.getString("mobile"),
                            extras.getString("email"), extras.getString("addr"));
                }
                break;
        }
    }

    public void UpdateList()
    {
        (new RetrieveContacts()).execute("get");
    }

    private void AddContact(String fname, String lname, String phone,
                            String mobile, String email, String address) {

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("first_name", fname);
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
                startActivityForResult(i, 2);
                return true;
            case R.id.action_pickcontact:
                Intent intent = new Intent(this, PhoneContacts.class);
                startActivityForResult(intent, 10);
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

                conn.disconnect();

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

                conn.setRequestProperty("Content-Type", "application/json");

                conn.setDoOutput(true);

                // Write to the stream
                OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());

                os.write(param);

                os.flush();
                os.close();

                conn.connect();

                int success = 0;
                int r = conn.getResponseCode();

                if(r == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(u.openStream()));
                    StringBuffer res = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        res.append(line);
                    }
                    reader.close();

                    JSONObject JObject = new JSONObject(res.toString());
                    if(JObject.has("success"))
                        success = JObject.getInt("success");

                }



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
            String firstName = "";
            if(obj.has("first_name"))
                firstName = obj.getString("first_name");
            String lastName = "";

            if(obj.has("last_name"))
                lastName = obj.getString("last_name");
            String email = "";

            if(obj.has("email"))
                email = obj.getString("email");
            String phoneNumber = "";

            if(obj.has("phone"))
                phoneNumber = obj.getString("phone");
            String mobile = "";

            if(obj.has("mobile"))
                mobile = obj.getString("mobile");
            String address = "";

            if(obj.has("address"))
                address = obj.getString("address");
            String id = "";

            if(obj.has("_id"))
                id = obj.getString("_id");

            return new Contact(firstName, lastName, email, phoneNumber, mobile, address, id);
        }
    }
}
