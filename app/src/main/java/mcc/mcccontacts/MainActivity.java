package mcc.mcccontacts;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;



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

                int itemPosition = position;

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

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void dummyUpdate()
    {

        Contact[] contactsArray = new Contact[]{new Contact("Eduardo", "Castellanos"), new Contact()};
        alContact = new ArrayList<Contact>();
        alContact.addAll(Arrays.asList(contactsArray));
        updateUI();
    }

    private void updateUI() {
        // Update the UI after the contacts are updated.
        if(alContact != null)
        {
            listAdapter = new ArrayAdapter<Contact>(this, R.layout.simplerow, alContact);
            mainListView.setAdapter(listAdapter);
        }
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
                dummyUpdate();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
