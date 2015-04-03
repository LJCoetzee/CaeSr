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
public class AssignmentDataAdapter extends CursorAdapter
{
    private final String LOG_TAG = AssignmentDataAdapter.class.getSimpleName();

    public AssignmentDataAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_assignment, parent, false);

        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        String module;
        String title;
        String datetime;
        if (!cursor.moveToFirst() || cursor == null) {
            module = "";
            title = "There are currently no assignments due.";
            datetime = "";

            // cursor.getString(AssignmentFragment.COL_ASSIGNMENT_VIEWED);
        }
        else
        {

            module = cursor.getString(AssignmentFragment.COL_ASSIGNMENT_MODULE) + ": ";
            title = cursor.getString(AssignmentFragment.COL_ASSIGNMENT_TITLE);
            datetime = Utility.formatDate(cursor.getLong(AssignmentFragment.COL_ASSIGNMENT_DUEDATE))
                    + " | "
                    + cursor.getString(AssignmentFragment.COL_ASSIGNMENT_DUETIME);
        }
            TextView moduleView = (TextView) view.findViewById(R.id.list_item_assignment_module_textview);
            TextView titleView = (TextView) view.findViewById(R.id.list_item_assignment_title_textview);
            TextView datetimeView = (TextView) view.findViewById(R.id.list_item_assignment_datetime_textview);

            Log.d(LOG_TAG, "Cursor####################" + cursor.getCount());

        moduleView.setText(module);
        titleView.setText(title);
        datetimeView.setText(datetime);

        String viewed = cursor.getString(AssignmentFragment.COL_ASSIGNMENT_VIEWED);
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

//    public void bindViewEmpty(View view, Context context, Cursor cursor) {
//
//        TextView moduleView = (TextView)view.findViewById(R.id.list_item_assignment_module_textview);
//        TextView titleView = (TextView)view.findViewById(R.id.list_item_assignment_title_textview);
//        TextView datetimeView = (TextView)view.findViewById(R.id.list_item_assignment_datetime_textview);
//
//        moduleView.setText("");
//        titleView.setText("There are currently no assignments due.");
//        datetimeView.setText("");
//    }
}
