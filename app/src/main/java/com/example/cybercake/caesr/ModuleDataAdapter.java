package com.example.cybercake.caesr;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by SeterraNova on 2015-03-29.
 */
public class ModuleDataAdapter extends CursorAdapter
{
    private final String LOG_TAG = ModuleDataAdapter.class.getSimpleName();

    public ModuleDataAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }



    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_module, parent, false);

        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String module = cursor.getString(ModuleFragment.COL_MODULE_TITLE);
        String description = cursor.getString(ModuleFragment.COL_MODULE_DESCRIPTION);
        String subscribed = cursor.getString(ModuleFragment.COL_MODULE_SUBSCRIPTION);

        TextView moduleView = (TextView) view.findViewById(R.id.list_item_module_textview);
        moduleView.setText(module);

        TextView descriptioneView = (TextView) view.findViewById(R.id.list_item_moduledescription_textview);
        descriptioneView.setText(description);

        //tv.setText(convertCursorRowToUXFormat(cursor));
    }
}
