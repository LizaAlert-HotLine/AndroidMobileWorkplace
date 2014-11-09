package ru.lizaalert.hotline.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.lizaalert.hotline.R;

import static ru.lizaalert.hotline.SpreadsheetXmlParser.Entry;

/**
 * Created by defuera on 19/10/14.
 */
public class OrganizationsArrayAdapter extends ArrayAdapter<Entry> {
    @SuppressWarnings("UnusedDeclaration")
    private static final String LOG_TAG = OrganizationsArrayAdapter.class.getSimpleName();

    private final Context context;
    private List<Entry> entries;
    private List<Entry> filteredData = new ArrayList<>();
    private ViewHolder viewHolder;

    public OrganizationsArrayAdapter(Context context, List<Entry> entries) {
        super(context, R.layout.list_item_organization);
        this.context = context;
        this.entries = entries;
    }

    @Override
    public int getCount() {
        if (filteredData != null)
            return filteredData.size();
        else
            return entries == null ? 0 : entries.size();
    }

    @Override
    public Entry getItem(int position) {
        if (filteredData != null)
            return filteredData.get(position);
        else
            return entries.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_organization, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.organizationName = (TextView) convertView.findViewById(R.id.organization_name);
            viewHolder.phones = (TextView) convertView.findViewById(R.id.phones);
            viewHolder.description = (TextView) convertView.findViewById(R.id.descriprion);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // object item based on the position
        Entry item = getItem(position);

        // assign values if the object is not null
        if (item != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            viewHolder.organizationName.setText(item.name);
            viewHolder.phones.setText(item.phone.replace(" ", "\n"));
            viewHolder.description.setText(item.description);

//            Log.i(LOG_TAG, "display item " + position + " " + item.name);
        }

        return convertView;
    }

    public void swapData(List<Entry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    public void applyFilter(String filter) {
        filteredData.clear();
        for (Entry e : entries) {
            if (TextUtils.equals(e.region, filter))
                filteredData.add(e);
        }
        notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView organizationName;
        public TextView phones;
        public TextView description;
    }
}
