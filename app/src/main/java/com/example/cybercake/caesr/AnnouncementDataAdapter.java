package com.example.cybercake.caesr;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SeterraNova on 2015-03-29.
 */
public class AnnouncementDataAdapter extends CursorAdapter
{
    private final String LOG_TAG = AnnouncementDataAdapter.class.getSimpleName();

    private final int VIEW_TYPE_MENU = 0;
    private final int VIEW_TYPE_ANN = 1;

    public static class ViewHolder {
        public final ImageView annIconView;
        public final ImageView assiIconView;
        public final ImageView disIconView;
        public final ImageView mcIconView;

//        public final TextView moduleView;
//        public final TextView titleView;
//        public final TextView datetimeView;

        public ViewHolder(View view) {
            annIconView = (ImageView) view.findViewById(R.id.tab_announcement_icon);
            assiIconView = (ImageView) view.findViewById(R.id.tab_assignment_icon);
            disIconView = (ImageView) view.findViewById(R.id.tab_discussion_icon);
            mcIconView = (ImageView) view.findViewById(R.id.tab_content_icon);

//            moduleView = (TextView)view.findViewById(R.id.list_item_announcement_module_textview);
//
//            titleView = (TextView)view.findViewById(R.id.list_item_announcement_title_textview);
//
//            datetimeView = (TextView)view.findViewById(R.id.list_item_announcement_datetime_textview);
        }
    }


    public AnnouncementDataAdapter(Context context, Cursor c, int flags)
    {
        super(context, c, flags);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_MENU : VIEW_TYPE_ANN;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
//        int viewType = getItemViewType(cursor.getPosition());
//        int layoutId = -1;
//
//        if(viewType == VIEW_TYPE_MENU)
//        {
//            layoutId = R.layout.layout_tabs;
//        }
//        else
//        {
//            layoutId = R.layout.list_item_announcement;
//        }

        //layoutId = R.layout.list_item_announcement;


        //System.out.println("\n"+R.layout.list_item_announcement + " | " + R.layout.layout_tabs + " - " +layoutId);

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_announcement, parent, false);

//        ViewHolder viewHolder = new ViewHolder(view);
//        view.setTag(viewHolder);

        return view;
    }

    /*
       This is where we fill-in the views with the contents of the cursor.
    */
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        String module = cursor.getString(AnnouncementFragment.COL_ANNOUNCEMENT_MODULE);
        String title = cursor.getString(AnnouncementFragment.COL_ANNOUNCEMENT_TITLE);
        String datetime = Utility.formatDate(cursor.getLong(AnnouncementFragment.COL_ANNOUNCEMENT_DATE)) + " | " + cursor.getString(AnnouncementFragment.COL_ANNOUNCEMENT_TIME);

        TextView moduleView = (TextView)view.findViewById(R.id.list_item_announcement_module_textview);
        TextView titleView = (TextView)view.findViewById(R.id.list_item_announcement_title_textview);
        TextView datetimeView = (TextView)view.findViewById(R.id.list_item_announcement_datetime_textview);

        if(title.contains("No Anouncements.") || title.length() == 0 || title == null)
        {
            title = "No Anouncements.";
            datetime = "";
        }

        moduleView.setText(module);
        titleView.setText(title);
        datetimeView.setText(datetime);

        String viewed = cursor.getString(AnnouncementFragment.COL_ANNOUNCEMENT_VIEWED);
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
