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
public class AssignmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = AssignmentFragment.class.getSimpleName();

    private static final int ASSIGNMENT_LOADER = 0;

    private String mModuleSetting;
    private AssignmentDataAdapter mAssignmentAdapter;

    private static final String[] ASSIGNMENT_COLUMNS = {
            DataContract.AssignmentEntry.TABLE_NAME + "." + DataContract.AssignmentEntry._ID,
            DataContract.AssignmentEntry.COLUMN_LOC_KEY,
            DataContract.AssignmentEntry.COLUMN_TITLE,
            DataContract.AssignmentEntry.COLUMN_DUEDATE,
            DataContract.AssignmentEntry.COLUMN_DUETIME,
            DataContract.AssignmentEntry.COLUMN_VIEWED
    };

    static final int COL_ASSIGNMENT_MODULE = 1;
    static final int COL_ASSIGNMENT_TITLE = 2;
    static final int COL_ASSIGNMENT_DUEDATE = 3;
    static final int COL_ASSIGNMENT_DUETIME = 4;
    static final int COL_ASSIGNMENT_VIEWED = 5;

    static final String ASSIGNMENT_URI = "URI";
    private Uri mUri;

    View mView;

    public AssignmentFragment()
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
            mUri = arguments.getParcelable(AssignmentFragment.ASSIGNMENT_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_assignment, container, false);

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

        System.out.println("\nmAssignmentAdapter: "+ mAssignmentAdapter);


        mAssignmentAdapter = new AssignmentDataAdapter(getActivity(), null, 0);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_assignment);
        listView.setAdapter(mAssignmentAdapter);

//        mView = inflater.inflate(R.layout.list_item_assignment, container, false);



        ImageView annView = (ImageView) rootView.findViewById(R.id.tab_announcement_icon);
        //ImageView asView = (ImageView) rootView.findViewById(R.id.tab_assignment_icon);
        ImageView disView = (ImageView) rootView.findViewById(R.id.tab_discussion_icon);
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

//        asView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(getActivity(), com.example.cybercake.caesr.AssignmentActivity.class)
//                        .setData(DataContract.AssignmentEntry.buildAssignmentsWithModule(
//                                mModuleSetting));
//                startActivity(intent);
//
//            }
//        });

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
//
        //Send user to browser
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ContentValues updatedValues = new ContentValues();
                    updatedValues.put(DataContract.AssignmentEntry.COLUMN_VIEWED, "TRUE");
                    int changed = getActivity().getContentResolver().update(DataContract.AssignmentEntry.CONTENT_URI,
                            updatedValues,
                            DataContract.AssignmentEntry.COLUMN_TITLE + " = ? AND " +DataContract.AssignmentEntry.COLUMN_VIEWED + " = ?",
                            new String[]{cursor.getString(COL_ASSIGNMENT_TITLE), "FALSE"});
                    //                    Log.d(LOG_TAG, "changed####################" + changed
                    //                            +"\nTitle: "+cursor.getString(COL_ANNOUNCEMENT_TITLE)
                    //                            +"\nViewed:" + cursor.getString(COL_ANNOUNCEMENT_VIEWED));
                    String url = "http://www.cs.up.ac.za/courses/" + mModuleSetting;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
            });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(ASSIGNMENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if(mUri != null) {

            // Sort order:  Ascending, by date.
            String sortOrder = DataContract.AssignmentEntry.COLUMN_DUEDATE + " DESC";
//        Uri moduleUri = DataContract.ModuleEntry.buildAssignmentModule("1");
//        Uri moduleUri = DataContract.ModuleEntry.CONTENT_URI;
            //Uri moduleUri = DataContract.AssignmentEntry.buildAssignmentsWithModule(mModuleSetting);
//        Uri moduleUri = DataContract.AssignmentEntry.buildAssignmentUri("COS212");

            //Log.d(LOG_TAG, "URI####################" + moduleUri.toString());

            return new CursorLoader(getActivity(),
                    mUri,
                    ASSIGNMENT_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAssignmentAdapter.swapCursor(cursor);
//
//        if (cursor.getCount() == 0 || !cursor.moveToFirst() || cursor == null)
//        {
////            Log.d(LOG_TAG, "Cursor####################" + cursor.getCount());
//            //mView.setText("There are currently no assignments due.");
//            mAssignmentAdapter.bindView(mView, getActivity(),cursor);
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAssignmentAdapter.swapCursor(null);
    }
}
