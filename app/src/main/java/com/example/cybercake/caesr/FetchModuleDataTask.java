package com.example.cybercake.caesr;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.cybercake.caesr.data.DataContract;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FetchModuleDataTask extends AsyncTask<String, Void, Void>
{
    private final String LOG_TAG = FetchModuleDataTask.class.getSimpleName();

    private static final String BASE_URL = "http://www.cs.up.ac.za/";
    private static final String UNDERGRAD = "undergrad";
    private static final String HONOURS = "honours";
    private static final String POSTGRAD = "postgrad";
    private static final String COURSE = "courses/";

    String preferredModules = "";

    //Module Adapters
    ArrayAdapter<String> mUModuleTitleAdapter;
//    ArrayAdapter<String> mHModuleAdapter;
//    ArrayAdapter<String> mPModuleAdapter;

    //Announcement Adapters
    ArrayList<ArrayList<String>> mUAnnouncementList;
//    ArrayAdapter<String> mHAnnouncementAdapter;
//    ArrayAdapter<String> mPAnnouncementAdapter;

    //Assignment Adapters
    ArrayList<ArrayList<String>> mUAssignmentList;
//    ArrayAdapter<String> mHAssignmentAdapter;
//    ArrayAdapter<String> mPAssignmentAdapter;

    //Discussion Adapters
    ArrayList<ArrayList<String>> mUDiscussionList;
//    ArrayAdapter<String> mHDiscussionAdapter;
//    ArrayAdapter<String> mPDiscussionAdapter;

    //Content Adapters
    ArrayList<ArrayList<String>> mUContentList;
//    ArrayAdapter<String> mHContentAdapter;
//    ArrayAdapter<String> mPContentAdapter;

    ArrayList<ArrayList<String>> modules = new ArrayList<>();

    String undergradPage = "";
    String honoursPage = "";
    String postgradPage = "";

    ArrayList<ArrayList<String>> undergradAnnouncements;
    ArrayList<ArrayList<String>> honoursAnnouncements;
    ArrayList<ArrayList<String>> postgradAnnouncements;

    ArrayList<ArrayList<String>> undergradAssignments;
    ArrayList<ArrayList<String>> honoursAssignments;
    ArrayList<ArrayList<String>> postgradAssignments;

    ArrayList<ArrayList<String>> undergradDiscussions;
    ArrayList<ArrayList<String>> honoursDiscussions;
    ArrayList<ArrayList<String>> postgradDiscussions;

    ArrayList<ArrayList<String>> undergradModuleContent;
    ArrayList<ArrayList<String>> honoursModuleContent;
    ArrayList<ArrayList<String>> postgradModuleContent;

    private final Context mContext;

    public FetchModuleDataTask(Context context)
    {
        mContext = context;
    }

    private boolean DEBUG = true;

    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        return format.format(date).toString();
    }

    long addModule(String module, String description, boolean subscribed)
    {
        long moduleId;


        Cursor moduleCursor = mContext.getContentResolver().query(
                DataContract.ModuleEntry.CONTENT_URI,
                new String[]{DataContract.ModuleEntry._ID},
                DataContract.ModuleEntry.COLUMN_MODULE + " = ?",
                new String[]{module},
                null);


        //mContext.getContentResolver().delete(DataContract.ModuleEntry)
        if(moduleCursor.moveToFirst())
        {
            int moduleIdIndex = moduleCursor.getColumnIndex(DataContract.ModuleEntry._ID);
            moduleId = moduleCursor.getLong(moduleIdIndex);
        }
        else
        {
            ContentValues moduleValues = new ContentValues();

            moduleValues.put(DataContract.ModuleEntry.COLUMN_MODULE, module);
            moduleValues.put(DataContract.ModuleEntry.COLUMN_DESCRIPTION, description);
            moduleValues.put(DataContract.ModuleEntry.COLUMN_SUB, subscribed);

            Uri insertedUri = mContext.getContentResolver().insert(
                    DataContract.ModuleEntry.CONTENT_URI,
                    moduleValues);

            moduleId = ContentUris.parseId(insertedUri);
        }

        moduleCursor.close();

        return moduleId;
    }

    void updateModule(String module, boolean subscribed)
    {
        ContentValues updatedValues = new ContentValues();

        if(subscribed)
        {
            updatedValues.put(DataContract.ModuleEntry.COLUMN_SUB, true);

            mContext.getContentResolver().update(DataContract.ModuleEntry.CONTENT_URI,
                    updatedValues,
                    DataContract.ModuleEntry.COLUMN_MODULE + " = ? AND " +DataContract.ModuleEntry.COLUMN_SUB + " = ?",
                    new String[]{module, "0"});
        }
        else
        {
            updatedValues.put(DataContract.ModuleEntry.COLUMN_SUB, false);
            mContext.getContentResolver().update(DataContract.ModuleEntry.CONTENT_URI,
                    updatedValues,
                    DataContract.ModuleEntry.COLUMN_MODULE + " = ? AND " +DataContract.ModuleEntry.COLUMN_SUB + " = ?",
                    new String[]{module, "1"});
        }


    }

    long addAnnouncement(String module_loc, String title, String author, long date, String time, String body)
    {
        long announcementId;

        Cursor announcementCursor = mContext.getContentResolver().query(
                DataContract.AnnouncementEntry.CONTENT_URI,
                new String[]{DataContract.AnnouncementEntry._ID},
                DataContract.AnnouncementEntry.COLUMN_TITLE + " = ?",
                new String[]{title},
                null);


        //mContext.getContentResolver().delete(DataContract.ModuleEntry)
        if(announcementCursor.moveToFirst())
        {
            int announcementIdIndex = announcementCursor.getColumnIndex(DataContract.AnnouncementEntry._ID);
            announcementId = announcementCursor.getLong(announcementIdIndex);
        }
        else
        {
            ContentValues announcementValues = new ContentValues();

            announcementValues.put(DataContract.AnnouncementEntry.COLUMN_LOC_KEY, module_loc);
            announcementValues.put(DataContract.AnnouncementEntry.COLUMN_TITLE, title);
            announcementValues.put(DataContract.AnnouncementEntry.COLUMN_AUTHOR, author);
            announcementValues.put(DataContract.AnnouncementEntry.COLUMN_DATE, date);
            announcementValues.put(DataContract.AnnouncementEntry.COLUMN_TIME, time);
            announcementValues.put(DataContract.AnnouncementEntry.COLUMN_BODY, body);


            Uri insertedUri = mContext.getContentResolver().insert(
                    DataContract.AnnouncementEntry.CONTENT_URI,
                    announcementValues);

            announcementId = ContentUris.parseId(insertedUri);
        }

        announcementCursor.close();
        return announcementId;
    }

    long addAssignment(String module_loc, String title, long date, String time)
    {
        long assignmentId;

        Cursor assignmentCursor = mContext.getContentResolver().query(
                DataContract.AssignmentEntry.CONTENT_URI,
                new String[]{DataContract.AssignmentEntry._ID},
                DataContract.AssignmentEntry.COLUMN_TITLE + " = ?",
                new String[]{title},
                null);

        if(assignmentCursor.moveToFirst())
        {
            int assignmentIdIndex = assignmentCursor.getColumnIndex(DataContract.AssignmentEntry._ID);
            assignmentId = assignmentCursor.getLong(assignmentIdIndex);
        }
        else
        {
            ContentValues assignmentValues = new ContentValues();

            assignmentValues.put(DataContract.AssignmentEntry.COLUMN_LOC_KEY, module_loc);
            assignmentValues.put(DataContract.AssignmentEntry.COLUMN_TITLE, title);
            assignmentValues.put(DataContract.AssignmentEntry.COLUMN_DUEDATE, date);
            assignmentValues.put(DataContract.AssignmentEntry.COLUMN_DUETIME, time);


            Uri insertedUri = mContext.getContentResolver().insert(
                    DataContract.AssignmentEntry.CONTENT_URI,
                    assignmentValues);

            assignmentId = ContentUris.parseId(insertedUri);
        }

        assignmentCursor.close();
        return assignmentId;
    }

    long addDiscussion(String module_loc, String title, String link, long date, String time)
    {
        // First, check if the location with this city name exists in the db
        // If it exists, return the current ID
        long discussionId;

        Cursor discussionCursor = mContext.getContentResolver().query(
                DataContract.DiscussionEntry.CONTENT_URI,
                new String[]{DataContract.DiscussionEntry._ID},
                DataContract.DiscussionEntry.COLUMN_TITLE + " = ?",
                new String[]{title},
                null);



        if(discussionCursor.moveToFirst())
        {
            int discussionIdIndex = discussionCursor.getColumnIndex(DataContract.DiscussionEntry._ID);
            discussionId = discussionCursor.getLong(discussionIdIndex);
        }
        else
        {
            ContentValues discussionValues = new ContentValues();

            discussionValues.put(DataContract.DiscussionEntry.COLUMN_LOC_KEY, module_loc);
            discussionValues.put(DataContract.DiscussionEntry.COLUMN_LINK, link);
            discussionValues.put(DataContract.DiscussionEntry.COLUMN_TITLE, title);
            discussionValues.put(DataContract.DiscussionEntry.COLUMN_LUDATE, date);
            discussionValues.put(DataContract.DiscussionEntry.COLUMN_LUTIME, time);


            Uri insertedUri = mContext.getContentResolver().insert(
                    DataContract.DiscussionEntry.CONTENT_URI,
                    discussionValues);

            discussionId = ContentUris.parseId(insertedUri);
        }

        discussionCursor.close();
        return discussionId;
    }

    long addModuleContent(String module_loc, String link, String title, String size)
    {
        // First, check if the location with this city name exists in the db
        // If it exists, return the current ID
        long moduleContentId;

        Cursor moduleContentCursor = mContext.getContentResolver().query(
                DataContract.ContentEntry.CONTENT_URI,
                new String[]{DataContract.ContentEntry._ID},
                DataContract.ContentEntry.COLUMN_TITLE + " = ?",
                new String[]{title},
                null);


        //mContext.getContentResolver().delete(DataContract.ModuleEntry)
        if(moduleContentCursor.moveToFirst())
        {
            int moduleContentIdIndex = moduleContentCursor.getColumnIndex(DataContract.ContentEntry._ID);
            moduleContentId = moduleContentCursor.getLong(moduleContentIdIndex);
        }
        else
        {
            ContentValues moduleContentValues = new ContentValues();

            moduleContentValues.put(DataContract.ContentEntry.COLUMN_LOC_KEY, module_loc);
            moduleContentValues.put(DataContract.ContentEntry.COLUMN_TITLE, title);
            moduleContentValues.put(DataContract.ContentEntry.COLUMN_LINK, link);
            moduleContentValues.put(DataContract.ContentEntry.COLUMN_SIZE, size);


            Uri insertedUri = mContext.getContentResolver().insert(
                    DataContract.ContentEntry.CONTENT_URI,
                    moduleContentValues);

            moduleContentId = ContentUris.parseId(insertedUri);
        }

        moduleContentCursor.close();
        return moduleContentId;
    }

    private void bulkInsertAnnouncements(ArrayList<ArrayList<String>> announcements,ArrayList<String> modules)
    {
        for (int j = 0; j < modules.size(); j++)
        {
//                Log.d(LOG_TAG, "---------------------------------------------------------------------------------------------");
            for(int i = 0; i < announcements.get(j).size(); i+=5)
            {
                addAnnouncement(modules.get(j), announcements.get(j).get(i), announcements.get(j).get(i+1), Long.parseLong(announcements.get(j).get(i+2)), announcements.get(j).get(i+3), announcements.get(j).get(i+4));

//                    Log.d(LOG_TAG, "MODULE##################" + modules.get(j));
//                    Log.d(LOG_TAG, "TITLE###################" + announcements.get(j).get(i));
//                    Log.d(LOG_TAG, "AUTHOR##################" + announcements.get(j).get(i + 1));
//                    Log.d(LOG_TAG, "DATE####################" + announcements.get(j).get(i + 2));
//                    Log.d(LOG_TAG, "TIME####################" + announcements.get(j).get(i + 3));
//                    Log.d(LOG_TAG, "BODY####################" + announcements.get(j).get(i+4));
//                    Log.d(LOG_TAG, "------------------------");

            }
        }
    }

    private void bulkInsertAssignments(ArrayList<ArrayList<String>> assignments,ArrayList<String> modules)
    {
        for (int j = 0; j < assignments.size(); j++)
        {
//                Log.d(LOG_TAG, "---------------------------------------------------------------------------------------------");
            for(int i = 0; i < assignments.get(j).size(); i+=3) {
                addAssignment(modules.get(j), assignments.get(j).get(i), Long.parseLong(assignments.get(j).get(i + 1)), assignments.get(j).get(i + 2));

//                    Log.d(LOG_TAG, "MODULE##################" + modules.get(j));
//                    Log.d(LOG_TAG, "TITLE###################" + assignments.get(j).get(i));
//                    Log.d(LOG_TAG, "DATE DUE################" + assignments.get(j).get(i + 1));
//                    Log.d(LOG_TAG, "TIME DUE################" + assignments.get(j).get(i + 2));
            }
        }
    }

    private void bulkInsertDiscussions(ArrayList<ArrayList<String>> discussions,ArrayList<String> modules)
    {
        for (int j = 0; j < discussions.size(); j++)
        {
//                Log.d(LOG_TAG, "---------------------------------------------------------------------------------------------");
            for(int i = 0; i < discussions.get(j).size(); i+=4)
            {
                addDiscussion(modules.get(j), discussions.get(j).get(i), discussions.get(j).get(i+1), Long.parseLong(discussions.get(j).get(i + 2)), discussions.get(j).get(i + 3));

//                    Log.d(LOG_TAG, "MODULE##################" + modules.get(j));
//                    Log.d(LOG_TAG, "TITLE###################" + discussions.get(j).get(i));
//                    Log.d(LOG_TAG, "LINK####################" + discussions.get(j).get(i + 1));
//                    Log.d(LOG_TAG, "LU TIME#################" + discussions.get(j).get(i + 2));
//                    Log.d(LOG_TAG, "LU TIME#################" + discussions.get(j).get(i + 3));
            }
        }
    }

    private void bulkInsertModuleContent(ArrayList<ArrayList<String>> content,ArrayList<String> modules)
    {
        for (int j = 0; j < content.size(); j++)
        {
//                Log.d(LOG_TAG, "---------------------------------------------------------------------------------------------");
            for(int i = 0; i < content.get(j).size(); i+=3)
            {
                addModuleContent(modules.get(j), content.get(j).get(i), content.get(j).get(i + 1), content.get(j).get(i + 2));

                    Log.d(LOG_TAG, "MODULE##################" + modules.get(j));
//                    Log.d(LOG_TAG, "TITLE###################" + content.get(j).get(i));
//                    Log.d(LOG_TAG, "LINK####################" + content.get(j).get(i + 1));
//                    Log.d(LOG_TAG, "SIZE####################" + content.get(j).get(i + 2));
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(DownloadImageActivity.this, "Please be patient.", "Fetching data...");
    }

    @Override
    protected Void doInBackground(String... params)
    {


        if(params == null)
        {
            return null;
        }
        Utility utility = new Utility();

        ArrayList<ArrayList<String>> pages = new ArrayList<>();

        ArrayList<String> undergradModules;
        ArrayList<String> honoursModules;
        ArrayList<String> postgradModules;
        ArrayList<String> undergradModuleTitles = new ArrayList<>();
        ArrayList<String> honoursModuleTitles = new ArrayList<>();
        ArrayList<String> postgradModuleTitles = new ArrayList<>();

        ArrayList<String> undergradModulePages;
        ArrayList<String> honoursModulePages;
        ArrayList<String> postgradModulePages;

        ArrayList<ArrayList<ArrayList<String>>> allContent = new ArrayList<>();

        preferredModules = params[0];

        try
        {

            undergradPage = utility.fetchHTML(BASE_URL+UNDERGRAD);
            honoursPage = utility.fetchHTML(BASE_URL+HONOURS);
            postgradPage = utility.fetchHTML(BASE_URL+POSTGRAD);

            //get module+descriptions
            undergradModules = new ArrayList<>(utility.getModules(undergradPage, preferredModules));
            modules.add(undergradModules);

            honoursModules = new ArrayList<>(utility.getModules(honoursPage, preferredModules));
            modules.add(honoursModules);

            postgradModules = new ArrayList<>(utility.getModules(postgradPage, preferredModules));
            modules.add(postgradModules);

            ////////////////////////////////////////////////////////
            //get all undergrad pages
            undergradModulePages = new ArrayList<>(utility.collectAllPages(BASE_URL+COURSE,undergradModules));

            ////////////////////////////////////////////////////////
            //get all honours pages
            honoursModulePages = new ArrayList<>(utility.collectAllPages(BASE_URL+COURSE,honoursModules));
            ////////////////////////////////////////////////////////
            //get all postgrad pages
            postgradModulePages = new ArrayList<>(utility.collectAllPages(BASE_URL+COURSE,postgradModules));

//                for (int i = 0; i < undergradModulePages.size();i++) {
//                    Log.v(LOG_TAG, ">>>>>>>>>>>>>>>>>>>>" + undergradModulePages.get(i));
//                }

            pages.add(undergradModulePages);
            pages.add(honoursModulePages);
            pages.add(postgradModulePages);

            ////////////////////////////////////////////////////////
            //get each module's announcements
            //-----------
            //undergrad announcements
            undergradAnnouncements = new ArrayList<>(utility.collectAllAnnouncements(pages.get(0), modules.get(0)));
            //honours announcements
            honoursAnnouncements = new ArrayList<>(utility.collectAllAnnouncements(pages.get(1), modules.get(1)));
            //postgrad announcements
            postgradAnnouncements = new ArrayList<>(utility.collectAllAnnouncements(pages.get(2), modules.get(2)));

//                for (int i = 0; i < modules.get(0).size();i+=3)
//                {
//                    Log.v(LOG_TAG, "===================== " + modules.get(0).get(i));
//                }

            ////////////////////////////////////////////////////////
            //get each module's active assignments
            //-----------
            //undergrad announcements
            undergradAssignments = new ArrayList<>(utility.collectAllAssignments(pages.get(0), modules.get(0)));
            //honours announcements
            honoursAssignments = new ArrayList<>(utility.collectAllAssignments(pages.get(1), modules.get(1)));
            //postgrad announcements
            postgradAssignments = new ArrayList<>(utility.collectAllAssignments(pages.get(2), modules.get(2)));

            ////////////////////////////////////////////////////////
            //get each module's discussions
            //-----------
            //undergrad announcements
            undergradDiscussions = new ArrayList<>(utility.collectAllDiscussions(pages.get(0), modules.get(0)));
            //honours announcements
            honoursDiscussions = new ArrayList<>(utility.collectAllDiscussions(pages.get(1), modules.get(1)));
            //postgrad announcements
            postgradDiscussions = new ArrayList<>(utility.collectAllDiscussions(pages.get(2), modules.get(2)));

            ////////////////////////////////////////////////////////
            //get each module's course content
            //-----------
            //undergrad announcements
            undergradModuleContent = new ArrayList<>(utility.collectAllModuleContent(pages.get(0), modules.get(0)));
            //honours announcements
            honoursModuleContent = new ArrayList<>(utility.collectAllModuleContent(pages.get(1), modules.get(1)));
            //postgrad announcements
            postgradModuleContent = new ArrayList<>(utility.collectAllModuleContent(pages.get(2), modules.get(2)));

            //Add modules to database
            long moduleID;
            for (int j = 0; j < modules.size(); j++)
            {
                for (int i = 0; i < modules.get(j).size(); i+=3)
                {

                    if (preferredModules.contains(modules.get(j).get(i)))
                    {
                        moduleID = addModule(modules.get(j).get(i), modules.get(j).get(i + 1), true);
                        //Log.v(LOG_TAG, modules.get(j).get(i)+"->>>>>>>>>>>>>>>>>>>> Module subscribed: " + modules.get(j).get(i) + modules.get(j).get(i+1));
                    }
                    else
                    {
                        moduleID = addModule(modules.get(j).get(i), modules.get(j).get(i + 1), false);
                        //Log.v(LOG_TAG, modules.get(j).get(i)+"->>>>>>>>>>>>>>>>>>>> Module unsubscribed!!!!!!!: " + modules.get(j).get(i));
                    }


                    if(j == 0) undergradModuleTitles.add(modules.get(j).get(i));
                    if(j == 1) honoursModuleTitles.add(modules.get(j).get(i));
                    if(j == 2) postgradModuleTitles.add(modules.get(j).get(i));
                }
            }

            for (int j = 0; j < modules.size(); j++)
            {
                for (int i = 0; i < modules.get(j).size(); i+=3)
                {

                    if (preferredModules.contains(modules.get(j).get(i)))
                    {
                        updateModule(modules.get(j).get(i), true);
                        //Log.v(LOG_TAG, modules.get(j).get(i)+"->>>>>>>>>>>>>>>>>>>> Module subscribed: " + modules.get(j).get(i) + modules.get(j).get(i+1));
                    }
                    else// if (!preferredModules.contains(modules.get(j).get(i)) && modules.get(j).get(i+2).contains("false"))
                    {
                        updateModule(modules.get(j).get(i), false);
                        //Log.v(LOG_TAG, modules.get(j).get(i)+"->>>>>>>>>>>>>>>>>>>> Module unsubscribed!!!!!!!: " + modules.get(j).get(i));
                    }
                }
            }

            //add announcements to database
            bulkInsertAnnouncements(undergradAnnouncements, undergradModuleTitles);
            bulkInsertAnnouncements(honoursAnnouncements, honoursModuleTitles);
            bulkInsertAnnouncements(postgradAnnouncements, postgradModuleTitles);

            bulkInsertAssignments(undergradAssignments, undergradModuleTitles);
            bulkInsertAssignments(honoursAssignments, honoursModuleTitles);
            bulkInsertAssignments(postgradAssignments, postgradModuleTitles);

            bulkInsertDiscussions(undergradDiscussions, undergradModuleTitles);
            bulkInsertDiscussions(honoursDiscussions, honoursModuleTitles);
            bulkInsertDiscussions(postgradDiscussions, postgradModuleTitles);

            bulkInsertModuleContent(undergradModuleContent, undergradModuleTitles);
            bulkInsertModuleContent(honoursModuleContent, honoursModuleTitles);
            bulkInsertModuleContent(postgradModuleContent, postgradModuleTitles);
            Log.v(LOG_TAG,"->>>>>>>>>>>>>>>>>>>> All inserts completed." );



            //////////////////////////////
//            for (int i = 0; i < modules.get(0).size()-3; i++)
//            {
//                if(modules.get(0).get(i+2).equals("true")) {
//                    addModule(modules.get(0).get(i), modules.get(0).get(i+1), true);
//                }
//                else
//                {
//                    addModule(modules.get(0).get(i), modules.get(0).get(i+1), false);
//                }
//            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


//            for (int i = 0; i < undergradModulePages.size();i++)
//            {
//                Log.v(LOG_TAG, "Data from server: " + undergradModulePages.get(i));
//            }
        return null;
    }

}