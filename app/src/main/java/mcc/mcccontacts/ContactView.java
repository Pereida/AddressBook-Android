package mcc.mcccontacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class ContactView extends Activity {

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");

        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        TextView tvFName = (TextView) findViewById(R.id.tvFName);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        TextView tvLName = (TextView) findViewById(R.id.tvLName);
        TextView tvMobile = (TextView) findViewById(R.id.tvMobile);
        TextView tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);

        tvAddress.setText(intent.getStringExtra("addr"));
        tvFName.setText(intent.getStringExtra("fname"));
        tvEmail.setText(intent.getStringExtra("email"));
        tvLName.setText(intent.getStringExtra("lname"));
        tvMobile.setText(intent.getStringExtra("mobile"));
        tvPhoneNum.setText(intent.getStringExtra("phone"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        CharSequence msg = "Nothing";

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_delete:
                msg = "Deleting contact.";
                break;
            case R.id.action_save:
                msg = "Contact saved.";
                break;
        }

        Context ctx = getApplicationContext();
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.show();

        return super.onOptionsItemSelected(item);
    }
}
