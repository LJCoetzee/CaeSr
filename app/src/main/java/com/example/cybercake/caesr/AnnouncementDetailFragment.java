package com.example.cybercake.caesr;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cybercake.caesr.data.DataContract;

/**
 * Created by SeterraNova on 2015-03-25.
 */
public class AnnouncementDetailFragment extends Fragment implements LoaderCallbacks<Cursor>
{
    private final String LOG_TAG = AnnouncementDetailFragment.class.getSimpleName();

    private static final int ANNOUNCEMENT_DETAIL_LOADER = 0;
    private static final String APP_SHARE_HASHTAG = "\n#CeasrApp";

    private String mAnnouncement;
    private ShareActionProvider mShareActionProvider;

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

    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mAuthorView;
    private TextView mDateTimeView;

    static final String ANNOUNCEMENTD_URI = "URI";
    private Uri mUri;

    public AnnouncementDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.announcement_detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mAnnouncement != null) {
            mShareActionProvider.setShareIntent(createShareAnnouncementIntent());
        }
    }

    private Intent createShareAnnouncementIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mAnnouncement + APP_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(AnnouncementDetailFragment.ANNOUNCEMENTD_URI);
        }

        View rootView = inflater.inflate(R.layout.fragment_announcement_detail, container, false);

        mTitleView = (TextView)rootView.findViewById(R.id.list_item_anndetail_title_textview);
        mBodyView = (TextView)rootView.findViewById(R.id.list_item_anndetail_body_textview);
        mAuthorView = (TextView)rootView.findViewById(R.id.list_item_anndetail_author_textview);
        mDateTimeView = (TextView)rootView.findViewById(R.id.list_item_anndetail_datetime_textview);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        getLoaderManager().initLoader(ANNOUNCEMENT_DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
//        Intent intent = getActivity().getIntent();
//        if (intent == null) {
//            return null;
//        }
        if(mUri != null) {

            return new CursorLoader(getActivity(),
                    mUri,
                    ANNOUNCEMENT_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!cursor.moveToFirst() && cursor == null) { return; }

        String module = cursor.getString(COL_ANNOUNCEMENT_MODULE);
        String title = cursor.getString(COL_ANNOUNCEMENT_TITLE);
        String author = cursor.getString(COL_ANNOUNCEMENT_AUTHOR);
        String date = Utility.formatDate(
                cursor.getLong(COL_ANNOUNCEMENT_DATE));
        String time = cursor.getString(COL_ANNOUNCEMENT_TIME);
        String body = cursor.getString(COL_ANNOUNCEMENT_BODY);



        mTitleView.setText(title);
        mBodyView.setText(body);
        mAuthorView.setText(author);
        mDateTimeView.setText(date +" | "+ time);

        mAnnouncement = String.format("%s\n%s\n\n%s\n\n%s\n%s | %s", module, title, body, author, date, time);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareAnnouncementIntent());
        }

        getActivity().setTitle(title);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }
}
