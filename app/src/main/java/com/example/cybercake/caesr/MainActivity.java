package com.example.cybercake.caesr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
{
    private static final String ANNOUNCEMENTSFRAGMENT_TAG = "ANFTAG";
    private static final String ANNOUNCEMENTDETAILFRAGMENT_TAG = "ANDFTAG";
    private static final String ASSIGNMENTFRAGMENT_TAG = "ASFTAG";
    private static final String DISCUSSIONFRAGMENT_TAG = "DFTAG";
    private static final String MODULECONTENTFRAGMENT_TAG = "MCFTAG";

    private boolean mTwoPane;
    String mSubscruptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.cybercake.caesr.R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        if (findViewById(R.id.announcements_container) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.announcements_container, new AnnouncementFragment(), ANNOUNCEMENTSFRAGMENT_TAG)
                        .commit();
            }
        }
        else if (findViewById(R.id.announcement_detail_container) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.announcement_detail_container, new AnnouncementDetailFragment(), ANNOUNCEMENTDETAILFRAGMENT_TAG)
                        .commit();
            }
        }
        else if (findViewById(R.id.assignments_container) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.assignments_container, new AssignmentFragment(), ASSIGNMENTFRAGMENT_TAG)
                        .commit();
            }
        }
        else if (findViewById(R.id.discussions_container) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.discussions_container, new DiscussionFragment(), DISCUSSIONFRAGMENT_TAG)
                        .commit();
            }
        }
        else if (findViewById(R.id.module_content_container) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.module_content_container, new ModulecontentFragment(), MODULECONTENTFRAGMENT_TAG)
                        .commit();
            }
        }
        else
        {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.cybercake.caesr.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.cybercake.caesr.R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String subscriptions = new ArrayList<>(prefs.getStringSet(getString(R.string.subscription_list_key),null)).toString();

        // update the location in our second pane using the fragment manager
        if (subscriptions != null && !subscriptions.equals(mSubscruptions))
        {
            ModuleFragment ff = (ModuleFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_module);
            if ( null != ff ) {
                ff.onSubscriptionChanged();
            }
            mSubscruptions = subscriptions;
        }
    }
}
