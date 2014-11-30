package mcc.mcccontacts;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private SimpleAdapter adpt;
    List<Contact> contactList = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adpt  = new SimpleAdapter(contactList, this);
        ListView lView = (ListView) findViewById(R.id.listview);

        lView.setAdapter(adpt);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_refresh) {
            (new RetrieveContacts()).execute("http://cloudguest116.niksula.hut.fi:8080/contacts/");
        }

        return super.onOptionsItemSelected(item);
    }

    public class RetrieveContacts extends AsyncTask<String, Void, List<Contact>>{

        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPostExecute(List<Contact> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            adpt.setItemList(result);
            adpt.notifyDataSetChanged();
            contactList = result;
            Log.e("Contactos", contactList.toString());
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