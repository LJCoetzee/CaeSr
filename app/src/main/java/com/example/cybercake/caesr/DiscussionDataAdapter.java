package com.example.cybercake.caesr;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by SeterraNova on 2015-03-29.
 */
public class DiscussionDataAdapter extends CursorAdapter
{
    private final String LOG_TAG = DiscussionDataAdapter.class.getSimpleName();

    public DiscussionDataAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_discussion, parent, false);

        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {

        String module = cursor.getString(DiscussionFragment.COL_DISCUSSION_MODULE);
        String title = cursor.getString(DiscussionFragment.COL_DISCUSSION_TITLE);
        String link = cursor.getString(DiscussionFragment.COL_DISCUSSION_LINK);
        String datetime = Utility.formatDate(cursor.getLong(DiscussionFragment.COL_DISCUSSION_DATE))
                + " | "
                + cursor.getString(DiscussionFragment.COL_DISCUSSION_TIME);

        TextView titleView = (TextView)view.findViewById(R.id.list_item_discussion_title_textview);
        TextView datetimeView = (TextView)view.findViewById(R.id.list_item_discussion_datetime_textview);

        titleView.setText(title);
        datetimeView.setText(datetime);

        String viewed = cursor.getString(DiscussionFragment.COL_DISCUSSION_VIEWED);
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

        Log.v(LOG_TAG, "----+" + cursor.getCount());

        //if(!datetimeView.getText().toString().contains(":"))
//        if(cursor.getCount() )
//        {
//            titleView.setText("This module has no active discussions.");
//            datetimeView.setText("");
//        }

    }
}
