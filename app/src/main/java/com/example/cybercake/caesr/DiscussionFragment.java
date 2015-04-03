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
public class DiscussionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = DiscussionFragment.class.getSimpleName();

    private static final int DISCUSSION_LOADER = 0;

    private String mModuleSetting;
    private DiscussionDataAdapter mDiscussionAdapter;

    private static final String[] DISCUSSION_COLUMNS = {
            DataContract.DiscussionEntry.TABLE_NAME + "." + DataContract.DiscussionEntry._ID,
            DataContract.DiscussionEntry.COLUMN_LOC_KEY,
            DataContract.DiscussionEntry.COLUMN_LINK,
            DataContract.DiscussionEntry.COLUMN_TITLE,
            DataContract.DiscussionEntry.COLUMN_LUDATE,
            DataContract.DiscussionEntry.COLUMN_LUTIME,
            DataContract.DiscussionEntry.COLUMN_VIEWED
    };

    static final int COL_DISCUSSION_MODULE = 1;
    static final int COL_DISCUSSION_LINK= 2;
    static final int COL_DISCUSSION_TITLE = 3;
    static final int COL_DISCUSSION_DATE= 4;
    static final int COL_DISCUSSION_TIME = 5;
    static final int COL_DISCUSSION_VIEWED = 6;

    static final String DISCUSSION_URI = "URI";
    private Uri mUri;

    public DiscussionFragment()
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
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DiscussionFragment.DISCUSSION_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_discussion, container, false);

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

        System.out.println("\nmDiscussionAdapter: "+ mDiscussionAdapter);

        mDiscussionAdapter = new DiscussionDataAdapter(getActivity(), null, 0);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_discussion);
        listView.setAdapter(mDiscussionAdapter);

        ImageView annView = (ImageView) rootView.findViewById(R.id.tab_announcement_icon);
        ImageView asView = (ImageView) rootView.findViewById(R.id.tab_assignment_icon);
//        ImageView disView = (ImageView) rootView.findViewById(R.id.tab_discussion_icon);
        ImageView modcView = (ImageView) rootView.findViewById(R.id.tab_content_icon);

        annView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), AnnouncementActivity.class)
                        .setData(DataContract.AnnouncementEntry.buildAnnouncementWithModule(
                                mModuleSetting));
                startActivity(intent);

            }
        });

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

//        disView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(getActivity(), DiscussionActivity.class)
//                        .setData(DataContract.DiscussionEntry.buildDiscussionWithModule(
//                                mModuleSetting));
//                startActivity(intent);
//
//            }
//        });

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ContentValues updatedValues = new ContentValues();
                    updatedValues.put(DataContract.DiscussionEntry.COLUMN_VIEWED, "TRUE");
                    int changed = getActivity().getContentResolver().update(DataContract.DiscussionEntry.CONTENT_URI,
                            updatedValues,
                            DataContract.DiscussionEntry.COLUMN_TITLE + " = ? AND " +DataContract.DiscussionEntry.COLUMN_VIEWED + " = ?",
                            new String[]{cursor.getString(COL_DISCUSSION_TITLE), "FALSE"});
                    //                    Log.d(LOG_TAG, "changed####################" + changed
                    //                            +"\nTitle: "+cursor.getString(COL_ANNOUNCEMENT_TITLE)
                    //                            +"\nViewed:" + cursor.getString(COL_ANNOUNCEMENT_VIEWED));
                    String url = "http://www.cs.up.ac.za/courses/" + mModuleSetting;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                    browserIntent.setData(Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
            });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(DISCUSSION_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if(mUri != null) {
            // Sort order:  Ascending, by date.
            String sortOrder = DataContract.DiscussionEntry.COLUMN_LUDATE + " DESC";
            //        Uri moduleUri = DataContract.ModuleEntry.buildDiscussionModule("1");
            //        Uri moduleUri = DataContract.ModuleEntry.CONTENT_URI;
            //Uri moduleUri = DataContract.DiscussionEntry.buildDiscussionWithModule(mModuleSetting);
            //        Uri moduleUri = DataContract.DiscussionEntry.buildDiscussionUri("COS212");

            //Log.d(LOG_TAG, "URI####################" + moduleUri.toString());

            return new CursorLoader(getActivity(),
                    mUri,
                    DISCUSSION_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mDiscussionAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mDiscussionAdapter.swapCursor(null);
    }
}
