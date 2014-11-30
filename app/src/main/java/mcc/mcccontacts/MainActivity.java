package mcc.mcccontacts;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
        }
    }

    public void UpdateList()
    {
        (new RetrieveContacts()).execute("http://cloudguest116.niksula.hut.fi:8080/contacts/");
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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class RetrieveContacts extends AsyncTask<String, Void, List<Contact>>{

        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPostExecute(List<Contact> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            listAdapter.clear();
            if(result != null)
                listAdapter.addAll(result);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading contacts...");
            dialog.show();
        }

        protected List<Contact> doInBackground(String... params) {

            List<Contact> result = new ArrayList<Contact>();

            try {
                URL u = new URL(params[0]);

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
