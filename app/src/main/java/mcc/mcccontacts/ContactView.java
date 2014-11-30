package mcc.mcccontacts;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


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
                if(saveContact()){
                    msg = "Contact saved.";
                }
                else
                {
                    msg = "Error saving contact.";
                }
                break;
        }

        Context ctx = getApplicationContext();
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.show();

        return super.onOptionsItemSelected(item);
    }

    public boolean saveContact()
    {

        ArrayList<ContentProviderOperation> ops =
                new ArrayList<ContentProviderOperation>();

        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                        ((TextView) findViewById(R.id.tvFName)).getText())
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                        ((TextView) findViewById(R.id.tvLName)).getText())
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ((TextView) findViewById(R.id.tvMobile)).getText())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ((TextView) findViewById(R.id.tvPhoneNum)).getText())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MAIN)
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA,
                        ((TextView) findViewById(R.id.tvEmail)).getText())
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                .build());

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA,
                        ((TextView) findViewById(R.id.tvAddress)).getText())
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME)
                .build());

        try{
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        } catch (Exception ex)
        {
            return false;
        }

    }
}
