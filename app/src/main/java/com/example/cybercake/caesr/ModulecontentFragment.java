package com.example.cybercake.caesr;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.example.cybercake.caesr.data.DataContract;

/**
 * Created by SeterraNova on 2015-03-25.
 */
public class ModulecontentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = ModulecontentFragment.class.getSimpleName();

    private static final int MODULECONTENT_LOADER = 0;

    private String mModuleSetting;
    private ModuleContentDataAdapter mContentAdapter;

    private static final String[] MODULECONTENT_COLUMNS = {
            DataContract.ContentEntry.TABLE_NAME + "." + DataContract.ContentEntry._ID,
            DataContract.ContentEntry.COLUMN_LOC_KEY,
            DataContract.ContentEntry.COLUMN_LINK,
            DataContract.ContentEntry.COLUMN_TITLE,
            DataContract.ContentEntry.COLUMN_SIZE,
            DataContract.ContentEntry.COLUMN_VIEWED
    };

    static final int COL_MODULECONTENT_MODULE = 1;
    static final int COL_MODULECONTENT_LINK = 2;
    static final int COL_MODULECONTENT_TITLE = 3;
    static final int COL_MODULECONTENT_SIZE = 4;
    static final int COL_MODULECONTENT_VIEWED = 5;

    static final String CONTENT_URI = "URI";
    private Uri mUri;


    public ModulecontentFragment()
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
            mUri = arguments.getParcelable(ModulecontentFragment.CONTENT_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_modulecontent, container, false);

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

        System.out.println("\nmContentAdapter: "+ mContentAdapter);


        mContentAdapter = new ModuleContentDataAdapter(getActivity(), null, 0);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_modulecontent);
        listView.setAdapter(mContentAdapter);

        ImageView annView = (ImageView) rootView.findViewById(R.id.tab_announcement_icon);
        ImageView asView = (ImageView) rootView.findViewById(R.id.tab_assignment_icon);
        ImageView disView = (ImageView) rootView.findViewById(R.id.tab_discussion_icon);
//        ImageView modcView = (ImageView) rootView.findViewById(R.id.tab_content_icon);

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

//        modcView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(getActivity(), ModulecontentActivity.class)
//                        .setData(DataContract.ContentEntry.buildContentWithModule(
//                                mModuleSetting));
//                startActivity(intent);
//
//            }
//        });
//
        //Call Anouncement activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {

                    ContentValues updatedValues = new ContentValues();
                    updatedValues.put(DataContract.ContentEntry.COLUMN_VIEWED, "TRUE");
                    int changed = getActivity().getContentResolver().update(DataContract.ContentEntry.CONTENT_URI,
                            updatedValues,
                            DataContract.ContentEntry.COLUMN_TITLE + " = ? AND " +DataContract.ContentEntry.COLUMN_VIEWED + " = ?",
                            new String[]{cursor.getString(COL_MODULECONTENT_TITLE), "FALSE"});
                    //                    Log.d(LOG_TAG, "changed####################" + changed
                    //                            +"\nTitle: "+cursor.getString(COL_ANNOUNCEMENT_TITLE)
                    //                            +"\nViewed:" + cursor.getString(COL_ANNOUNCEMENT_VIEWED));

                    DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(cursor.getString(COL_MODULECONTENT_LINK)));

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                            cursor.getString(COL_MODULECONTENT_MODULE)+"-"+cursor.getString(COL_MODULECONTENT_TITLE));

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    downloadManager.enqueue(request);

                    Toast toast = Toast.makeText(getActivity()
                            , "Downloading "+cursor.getString(COL_MODULECONTENT_TITLE) + "...", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
            });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(MODULECONTENT_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if(mUri != null) {
            // Sort order:  Ascending, by date.
            String sortOrder = DataContract.ContentEntry.COLUMN_TITLE + " DESC";
            //        Uri moduleUri = DataContract.ModuleEntry.buildContentModule("1");
            //        Uri moduleUri = DataContract.ModuleEntry.CONTENT_URI;
            //Uri moduleUri = DataContract.ContentEntry.buildContentWithModule(mModuleSetting);
            //        Uri moduleUri = DataContract.ContentEntry.buildContentUri("COS212");

            //Log.d(LOG_TAG, "URI####################" + moduleUri.toString());

            return new CursorLoader(getActivity(),
                    mUri,
                    MODULECONTENT_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mContentAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mContentAdapter.swapCursor(null);
    }
}
