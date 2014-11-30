package mcc.mcccontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddContact extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Button bSave = (Button) findViewById(R.id.bSave);
        
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();

                EditText etAddress = (EditText) findViewById(R.id.etAddress);
                EditText etFName = (EditText) findViewById(R.id.etFName);
                EditText etEmail = (EditText) findViewById(R.id.etEmail);
                EditText etLName = (EditText) findViewById(R.id.etLName);
                EditText etMobile = (EditText) findViewById(R.id.etMobile);
                EditText etPhoneNum = (EditText) findViewById(R.id.etPhone);

                intent.putExtra("addr", etAddress.getText().toString());
                intent.putExtra("fname", etFName.getText().toString());
                intent.putExtra("email", etEmail.getText().toString());
                intent.putExtra("lname", etLName.getText().toString());
                intent.putExtra("mobile", etMobile.getText().toString());
                intent.putExtra("phone", etPhoneNum.getText().toString());

                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
