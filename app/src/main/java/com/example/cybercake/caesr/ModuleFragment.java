package com.example.cybercake.caesr;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.cybercake.caesr.data.DataContract;

import java.util.ArrayList;

/**
 * Created by SeterraNova on 2015-03-25.
 */
public class ModuleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = ModuleFragment.class.getSimpleName();

    private static final int MODULE_LOADER = 0;

    private static final String[] MODULE_COLUMNS = {
            DataContract.ModuleEntry.TABLE_NAME + "." + DataContract.ModuleEntry._ID,
            DataContract.ModuleEntry.COLUMN_MODULE,
            DataContract.ModuleEntry.COLUMN_DESCRIPTION,
            DataContract.ModuleEntry.COLUMN_SUB
    };

    static final int COL_MODULE_TITLE = 1;
    static final int COL_MODULE_DESCRIPTION = 2;
    static final int COL_MODULE_SUBSCRIPTION = 3;

    private ModuleDataAdapter mModuleAdapter;

    private ListView mListView;
    private int mPosition =ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public interface Callback {
        public void onItemSelected(Uri dateUri);
    }

    public ModuleFragment()
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
        inflater.inflate(com.example.cybercake.caesr.R.menu.modulefragment, menu);
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
        if (id == com.example.cybercake.caesr.R.id.action_refresh)
        {
            update();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void update()
    {
        FetchModuleDataTask moduleDataTask = new FetchModuleDataTask(getActivity());
        //read values from shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String prefModules = new ArrayList<>(prefs.getStringSet(getString(R.string.subscription_list_key),null)).toString();
        //String prefModules = "";

        Log.v(LOG_TAG, "!!!!!!Preferred modules:" + prefModules);

        Toast toast = Toast.makeText(getActivity(),"Please be patient while the modules are loading...", Toast.LENGTH_LONG);
        toast.show();

        moduleDataTask.execute(prefModules);
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    public void onSubscriptionChanged()
    {
        update();
        getLoaderManager().restartLoader(MODULE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(com.example.cybercake.caesr.R.layout.fragment_main, container, false);


        mModuleAdapter = new ModuleDataAdapter(getActivity(), null, 0);

        mListView  = (ListView) rootView.findViewById(com.example.cybercake.caesr.R.id.listview_module);
        mListView .setAdapter(mModuleAdapter);

        final String moduleset;

        //Call Announcement activity
        mListView .setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(), com.example.cybercake.caesr.AnnouncementActivity.class)
                            .setData(DataContract.AnnouncementEntry.buildAnnouncementWithModule(
                                    cursor.getString(COL_MODULE_TITLE)));

                    mPosition = position;
                    startActivity(intent);

                }
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {// The listview probably hasn't even been populated yet.  Actually perform the
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

//        View view = inflater.inflate(R.layout.list_item_assignment, container, false);
//
//        ImageView annView = (ImageView) listView.findViewById(R.id.list_item_announcement_icon);
//        ImageView asView = (ImageView) rootView.findViewById(R.id.list_item_assignment_icon);
//        ImageView disView = (ImageView) rootView.findViewById(R.id.list_item_discussion_icon);
//        ImageView modcView = (ImageView) rootView.findViewById(R.id.list_item_modulecontent_icon);
//
//        annView.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(getActivity(), AnnouncementActivity.class)
//                        .setData(DataContract.AnnouncementEntry.buildAnnouncementWithModule(
//                                ));
//                startActivity(intent);
//
//            }
//        });
//
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
//
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
//
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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(MODULE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mPosition != ListView.INVALID_POSITION)
        {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Sort order:  Ascending, by date.
        String sortOrder = DataContract.ModuleEntry.COLUMN_MODULE + " ASC";
        Uri moduleUri = DataContract.ModuleEntry.buildModules("1");
//        Uri moduleUri = DataContract.ModuleEntry.CONTENT_URI;
//        Uri moduleUri = DataContract.AnnouncementEntry.buildAnnouncementModule("COS212");
//        Uri moduleUri = DataContract.AnnouncementEntry.buildAnnouncementUri("COS212");

        //Log.d(LOG_TAG, "URI####################" + moduleUri.toString());

        return new CursorLoader(getActivity(),
                moduleUri,
                MODULE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mModuleAdapter.swapCursor(cursor);
        if (mPosition != ListView.INVALID_POSITION) {
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mModuleAdapter.swapCursor(null);
    }
}
