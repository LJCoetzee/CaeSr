package com.example.cybercake.caesr;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.cybercake.caesr.data.DataContract;

/**
 * Created by SeterraNova on 2015-03-25.
 */
public class AnnouncementFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = AnnouncementFragment.class.getSimpleName();

    private static final int ANNOUNCEMENT_LOADER = 0;

    private String mModuleSetting;
    private AnnouncementDataAdapter mAnnouncementAdapter;

    private static final String[] ANNOUNCEMENT_COLUMNS = {
            DataContract.AnnouncementEntry.TABLE_NAME + "." + DataContract.AnnouncementEntry._ID,
            DataContract.AnnouncementEntry.COLUMN_LOC_KEY,
            DataContract.AnnouncementEntry.COLUMN_TITLE,
            DataContract.AnnouncementEntry.COLUMN_AUTHOR,
            DataContract.AnnouncementEntry.COLUMN_DATE,
            DataContract.AnnouncementEntry.COLUMN_TIME,
            DataContract.AnnouncementEntry.COLUMN_BODY,
            DataContract.AnnouncementEntry.COLUMN_VIEWED
    };

    static final int COL_ANNOUNCEMENT_MODULE = 1;
    static final int COL_ANNOUNCEMENT_TITLE = 2;
    static final int COL_ANNOUNCEMENT_AUTHOR = 3;
    static final int COL_ANNOUNCEMENT_DATE = 4;
    static final int COL_ANNOUNCEMENT_TIME = 5;
    static final int COL_ANNOUNCEMENT_BODY = 6;
    static final int COL_ANNOUNCEMENT_VIEWED = 7;

    public AnnouncementFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.modulefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_refresh) {
//            updateWeather();
//            return true;
//        }
        if (id == R.id.action_refresh)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null)
        {
            mModuleSetting = intent.getDataString();
        }

        SharedPreferences pref = getActivity().getSharedPreferences("module", 0);
        SharedPreferences.Editor editor = pref.edit();

        if(null != mModuleSetting)
        {
            mModuleSetting = mModuleSetting.substring(mModuleSetting.length()-6);
            editor.putString("module",mModuleSetting);
            editor.commit();
        }
        else
        {
            mModuleSetting = pref.getString("module","COS212");
        }

        System.out.println("\nmAnnouncementAdapter: "+ mAnnouncementAdapter);


        mAnnouncementAdapter = new AnnouncementDataAdapter(getActivity(), null, 0);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_announcement);
        listView.setAdapter(mAnnouncementAdapter);

//        getActivity().setTitle(mModuleSetting + " - Announcements");

        //ImageView annView = (ImageView) listView.findViewById(R.id.tab_announcement_icon);
        ImageView asView = (ImageView) rootView.findViewById(R.id.tab_assignment_icon);
        ImageView disView = (ImageView) rootView.findViewById(R.id.tab_discussion_icon);
        ImageView modcView = (ImageView) rootView.findViewById(R.id.tab_content_icon);

//        annView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(getActivity(), AnnouncementActivity.class)
//                        .setData(DataContract.AnnouncementEntry.buildAnnouncementWithModule(
//                                mModuleSetting));
//                startActivity(intent);
//
//            }
//        });

        asView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), com.example.cybercake.caesr.AssignmentActivity.class)
                        .setData(DataContract.AssignmentEntry.buildAssignmentsWithModule(
                                mModuleSetting));
                startActivity(intent);

            }
        });

        disView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), DiscussionActivity.class)
                        .setData(DataContract.DiscussionEntry.buildDiscussionWithModule(
                                mModuleSetting));
                startActivity(intent);

            }
        });

        modcView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), ModulecontentActivity.class)
                        .setData(DataContract.ContentEntry.buildContentWithModule(
                                mModuleSetting));
                startActivity(intent);

            }
        });

        //Call AnnouncementDetail activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null)
                {
                    ContentValues updatedValues = new ContentValues();
                    updatedValues.put(DataContract.AnnouncementEntry.COLUMN_VIEWED, "TRUE");
                    int changed = getActivity().getContentResolver().update(DataContract.AnnouncementEntry.CONTENT_URI,
                            updatedValues,
                            DataContract.AnnouncementEntry.COLUMN_TITLE + " = ? AND " +DataContract.AnnouncementEntry.COLUMN_VIEWED + " = ?",
                            new String[]{cursor.getString(COL_ANNOUNCEMENT_TITLE), "FALSE"});
                    Log.d(LOG_TAG, "changed####################" + changed
                            +"\nTitle: "+cursor.getString(COL_ANNOUNCEMENT_TITLE)
                            +"\nViewed:" + cursor.getString(COL_ANNOUNCEMENT_VIEWED));

                    Intent intent = new Intent(getActivity(), com.example.cybercake.caesr.AnnouncementDetailActivity.class)
                            .setData(DataContract.AnnouncementEntry.buildAnnouncementModuleWithTitle(
                                    mModuleSetting, cursor.getString(COL_ANNOUNCEMENT_TITLE)));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(ANNOUNCEMENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Sort order:  Ascending, by date.
        String sortOrder = DataContract.AnnouncementEntry.COLUMN_DATE + " DESC";
//        Uri moduleUri = DataContract.ModuleEntry.buildAnnouncementModule("1");
//        Uri moduleUri = DataContract.ModuleEntry.CONTENT_URI;
        Uri moduleUri = DataContract.AnnouncementEntry.buildAnnouncementWithModule(mModuleSetting);
//        Uri moduleUri = DataContract.AnnouncementEntry.buildAnnouncementUri("COS212");

        //Log.d(LOG_TAG, "URI####################" + moduleUri.toString());

        return new CursorLoader(getActivity(),
                moduleUri,
                ANNOUNCEMENT_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAnnouncementAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAnnouncementAdapter.swapCursor(null);
    }
}
