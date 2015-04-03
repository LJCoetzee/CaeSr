package com.example.cybercake.caesr;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by SeterraNova on 2015-03-29.
 */
public class ModuleContentDataAdapter extends CursorAdapter
{
    private final String LOG_TAG = ModuleContentDataAdapter.class.getSimpleName();

    public ModuleContentDataAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_modulecontent, parent, false);

        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title = cursor.getString(ModulecontentFragment.COL_MODULECONTENT_TITLE);
        String size = cursor.getString(ModulecontentFragment.COL_MODULECONTENT_SIZE);

        TextView titleView = (TextView)view.findViewById(R.id.list_item_mc_title_textview);
        TextView sizeView = (TextView)view.findViewById(R.id.list_item_mc_size_textview);

        titleView.setText(title);
        sizeView.setText(size);

        String viewed = cursor.getString(ModulecontentFragment.COL_MODULECONTENT_VIEWED);
        if(!viewed.equals("TRUE"))
        {
            titleView.setTextColor(Color.RED);
//            Log.v(LOG_TAG, "changed#################### to RED"+viewed);
        }
        else
        {
            titleView.setTextColor(Color.BLACK);
//            Log.v(LOG_TAG, "changed#################### to BLACK"+viewed);
        }
    }
}
