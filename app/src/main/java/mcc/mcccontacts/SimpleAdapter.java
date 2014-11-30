package mcc.mcccontacts;

/**
 * Created by pereida on 30.11.2014.
 */
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SimpleAdapter extends ArrayAdapter<Contact> {

    private List<Contact> itemList;
    private Context context;

    public SimpleAdapter(List<Contact> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_expandable_list_item_2, itemList);
        this.itemList = itemList;
        this.context = ctx;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }

    public Contact getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }

    /*public long getItemId(int position) {
        if (itemList != null)
            return Long.parseLong(itemList.get(position).getId());
        return 0;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, null);
        }

        Contact c = itemList.get(position);
        TextView text = (TextView) v.findViewById(R.id.firstName);
        text.setText(c.getName());

        TextView text1 = (TextView) v.findViewById(R.id.lastName);
        text1.setText(c.getLastName());

        TextView text2 = (TextView) v.findViewById(R.id.email);
        text2.setText(c.getEmail());

        TextView text3 = (TextView) v.findViewById(R.id.phoneNumber);
        text3.setText(c.getPhoneNumber());

        return v;

    }

    public List<Contact> getItemList() {
        return itemList;
    }

    public void setItemList(List<Contact> itemList) {
        this.itemList = itemList;
    }


}