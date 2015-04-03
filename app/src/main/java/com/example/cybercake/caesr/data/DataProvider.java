/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cybercake.caesr.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DataProvider extends ContentProvider {

    private final String LOG_TAG = DataProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private CaesrDBHelper mOpenHelper;

    static final int ANNOUNCEMENT = 100;
    static final int ANNOUNCEMENT_WITH_MODULE = 101;
    static final int ANNOUNCEMENT_WITH_MODULE_AND_TITLE = 102;

    static final int ASSIGNMENT = 200;
    static final int ASSIGNMENT_WITH_MODULE = 201;
//    static final int ASSIGNMENT_WITH_MODULE_AND_TITLE = 202;

    static final int DISCUSSION = 300;
    static final int DISCUSSION_WITH_MODULE = 301;
//    static final int DISCUSSION_WITH_MODULE_AND_TITLE = 302;

    static final int CONTENT = 400;
    static final int CONTENT_WITH_MODULE = 401;
//    static final int CONTENT_WITH_MODULE_AND_TITLE = 302;

    static final int MODULE = 500;
    static final int MODULE_WITH_SUB = 501;


    //class to help construct queries
    //serves as base for sqlite queries we built
    //->ANNOUNCEMENT BY MODULE



    private static final String sModuleSubscriptionSelection =
            DataContract.ModuleEntry.TABLE_NAME+
                    "." + DataContract.ModuleEntry.COLUMN_SUB + " = ? ";

    private static final String sAnnouncementModuleSelection =
            DataContract.AnnouncementEntry.TABLE_NAME+
                    "." + DataContract.AnnouncementEntry.COLUMN_LOC_KEY + " = ? ";

    private static final String sAnnouncementModuleAndTitleSelection =
            DataContract.AnnouncementEntry.TABLE_NAME+
                    "." + DataContract.AnnouncementEntry.COLUMN_LOC_KEY + " = ? AND "
                    + DataContract.AnnouncementEntry.COLUMN_TITLE + " = ?";

    private static final String sAssignmentModuleSelection =
            DataContract.AssignmentEntry.TABLE_NAME+
                    "." + DataContract.AssignmentEntry.COLUMN_LOC_KEY + " = ? ";

    private static final String sDiscussionModuleSelection =
            DataContract.DiscussionEntry.TABLE_NAME+
                    "." + DataContract.DiscussionEntry.COLUMN_LOC_KEY + " = ? ";

    private static final String sContentModuleSelection =
            DataContract.ContentEntry.TABLE_NAME+
                    "." + DataContract.ContentEntry.COLUMN_LOC_KEY + " = ? ";

    private Cursor getModulesBySubscriptionSetting(Uri uri, String[] projection, String sortOrder) {
        String moduleSetting = DataContract.ModuleEntry.getSubscriptionFromUri(uri);

        String[] selectionArgs = new String[]{moduleSetting};
        String selection= sModuleSubscriptionSelection;

//        Log.d(LOG_TAG, "))))))))))" + sModuleSubscriptionSelection+ "-" + moduleSetting + " | " + projection.toString()
//                + "\nSelectionArgs: " + selectionArgs.toString()
//                + "\nSelection: " + selection);

        return mOpenHelper.getReadableDatabase().query(DataContract.ModuleEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAnnouncementByModuleSetting(Uri uri, String[] projection, String sortOrder) {
        String moduleSetting = DataContract.AnnouncementEntry.getModuleFromUri(uri);

        String[] selectionArgs = new String[]{moduleSetting};
        String selection= sAnnouncementModuleSelection;

//        Log.d(LOG_TAG, "))))))))))" + sAnnouncementModuleSelection+ "-" + moduleSetting + " | " + projection.toString()
//                + "\nSelectionArgs: " + selectionArgs.toString()
//                + "\nSelection: " + selection);

        return mOpenHelper.getReadableDatabase().query(DataContract.AnnouncementEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAnnouncementByModuleSettingAndTitle(Uri uri, String[] projection, String sortOrder)
    {
        String moduleSetting = DataContract.AnnouncementEntry.getModuleFromUri(uri);
        String title = DataContract.AnnouncementEntry.getTitleUri(uri);

        Log.d(LOG_TAG, "))))))))))" + uri
                + "\nSelectionArgs: " + sAnnouncementModuleAndTitleSelection
                + "\nSelection: " + moduleSetting + " | " + title);

        return mOpenHelper.getReadableDatabase().query(DataContract.AnnouncementEntry.TABLE_NAME,
                projection,
                sAnnouncementModuleAndTitleSelection,
                new String[]{moduleSetting, title},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAssignmentByModuleSetting(Uri uri, String[] projection, String sortOrder) {
        String moduleSetting = DataContract.AnnouncementEntry.getModuleFromUri(uri);

        String[] selectionArgs = new String[]{moduleSetting};
        String selection= sAssignmentModuleSelection;

//        Log.d(LOG_TAG, "))))))))))" + sAnnouncementModuleSelection+ "-" + moduleSetting + " | " + projection.toString()
//                + "\nSelectionArgs: " + selectionArgs.toString()
//                + "\nSelection: " + selection);

        return mOpenHelper.getReadableDatabase().query(DataContract.AssignmentEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDiscussionByModuleSetting(Uri uri, String[] projection, String sortOrder) {
        String moduleSetting = DataContract.AnnouncementEntry.getModuleFromUri(uri);

        String[] selectionArgs = new String[]{moduleSetting};
        String selection= sDiscussionModuleSelection;

//        Log.d(LOG_TAG, "))))))))))" + sAnnouncementModuleSelection+ "-" + moduleSetting + " | " + projection.toString()
//                + "\nSelectionArgs: " + selectionArgs.toString()
//                + "\nSelection: " + selection);

        return mOpenHelper.getReadableDatabase().query(DataContract.DiscussionEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getContentByModuleSetting(Uri uri, String[] projection, String sortOrder) {
        String moduleSetting = DataContract.AnnouncementEntry.getModuleFromUri(uri);

        String[] selectionArgs = new String[]{moduleSetting};
        String selection= sContentModuleSelection;

//        Log.d(LOG_TAG, "))))))))))" + sAnnouncementModuleSelection+ "-" + moduleSetting + " | " + projection.toString()
//                + "\nSelectionArgs: " + selectionArgs.toString()
//                + "\nSelection: " + selection);

        return mOpenHelper.getReadableDatabase().query(DataContract.ContentEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DataContract.PATH_ANNOUNCEMENT, ANNOUNCEMENT);
        matcher.addURI(authority, DataContract.PATH_ANNOUNCEMENT + "/*", ANNOUNCEMENT_WITH_MODULE);
        matcher.addURI(authority, DataContract.PATH_ANNOUNCEMENT + "/*/*", ANNOUNCEMENT_WITH_MODULE_AND_TITLE);

        matcher.addURI(authority, DataContract.PATH_ASSIGNMENTS, ASSIGNMENT);
        matcher.addURI(authority, DataContract.PATH_ASSIGNMENTS + "/*", ASSIGNMENT_WITH_MODULE);

        matcher.addURI(authority, DataContract.PATH_DISCUSSIONS, DISCUSSION);
        matcher.addURI(authority, DataContract.PATH_DISCUSSIONS + "/*", DISCUSSION_WITH_MODULE);

        matcher.addURI(authority, DataContract.PATH_CONTENT, CONTENT);
        matcher.addURI(authority, DataContract.PATH_CONTENT + "/*", CONTENT_WITH_MODULE);

        matcher.addURI(authority, DataContract.PATH_MODULE , MODULE);
        matcher.addURI(authority, DataContract.PATH_MODULE + "/*", MODULE_WITH_SUB);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CaesrDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match)  //determine which mime types to send
        {

            case ANNOUNCEMENT_WITH_MODULE_AND_TITLE:
                return DataContract.AnnouncementEntry.CONTENT_ITEM_TYPE;
            case ANNOUNCEMENT_WITH_MODULE:
                return DataContract.AnnouncementEntry.CONTENT_TYPE;
            case ANNOUNCEMENT:
                return DataContract.AnnouncementEntry.CONTENT_TYPE;
            case ASSIGNMENT_WITH_MODULE:
                return DataContract.AssignmentEntry.CONTENT_TYPE;
            case ASSIGNMENT:
                return DataContract.AssignmentEntry.CONTENT_TYPE;
            case DISCUSSION_WITH_MODULE:
                return DataContract.DiscussionEntry.CONTENT_TYPE;
            case DISCUSSION:
                return DataContract.DiscussionEntry.CONTENT_TYPE;
            case CONTENT_WITH_MODULE:
                return DataContract.ContentEntry.CONTENT_TYPE;
            case CONTENT:
                return DataContract.ContentEntry.CONTENT_TYPE;
            case MODULE:
                return DataContract.ModuleEntry.CONTENT_TYPE;
            case MODULE_WITH_SUB:
                return DataContract.ModuleEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder)
    {
        Cursor retCursor;

        //Log.d(LOG_TAG, "matcher==========" + sUriMatcher.match(uri));

        switch (sUriMatcher.match(uri))
        {

            case ANNOUNCEMENT_WITH_MODULE_AND_TITLE:
            {
                //Log.v(LOG_TAG, "+++++++++++++" + uri);
                retCursor = getAnnouncementByModuleSettingAndTitle(uri, projection, sortOrder);
                break;
            }
            case ANNOUNCEMENT_WITH_MODULE:
            {
                retCursor = getAnnouncementByModuleSetting(uri, projection, sortOrder);
                break;
            }
            case ANNOUNCEMENT:
            {
                //pass on params provided my content provider
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.AnnouncementEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case ASSIGNMENT_WITH_MODULE:
            {
                retCursor = getAssignmentByModuleSetting(uri, projection, sortOrder);
                break;
            }
            case ASSIGNMENT:
            {
                //pass on params provided my content provider
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.AssignmentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case DISCUSSION_WITH_MODULE:
            {
                retCursor = getDiscussionByModuleSetting(uri, projection, sortOrder);
                break;
            }
            case DISCUSSION:
            {
                //pass on params provided my content provider
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.DiscussionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case CONTENT_WITH_MODULE:
            {
                retCursor = getContentByModuleSetting(uri, projection, sortOrder);
                break;
            }
            case CONTENT:
            {
                //pass on params provided my content provider
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ContentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MODULE_WITH_SUB:
            {
                retCursor = getModulesBySubscriptionSetting(uri, projection, sortOrder);
                break;
            }
            case MODULE:
            {
                //pass on params provided my content provider
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DataContract.ModuleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        //Log.d(LOG_TAG, "---------> INSERTING"+uri);
        switch (match)
        {
            case MODULE:
            {
                long _id = db.insert(DataContract.ModuleEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.ModuleEntry.buildModuleUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ANNOUNCEMENT:
            {
                long _id = db.insert(DataContract.AnnouncementEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.AnnouncementEntry.buildAnnouncementUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ASSIGNMENT:
            {
                long _id = db.insert(DataContract.AssignmentEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.AssignmentEntry.buildAssignmentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case DISCUSSION:
            {
                long _id = db.insert(DataContract.DiscussionEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.DiscussionEntry.buildDiscussionUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CONTENT:
            {
                long _id = db.insert(DataContract.ContentEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = DataContract.ContentEntry.buildContentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
//        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if(selection == null)
        {
            selection = "1";
        }
        switch (match)
        {
            case MODULE:
            {
                rowsDeleted = db.delete(DataContract.ModuleEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case ANNOUNCEMENT:
            {

                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsDeleted = db.delete(DataContract.AnnouncementEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case ASSIGNMENT:
            {

                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsDeleted = db.delete(DataContract.AssignmentEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case DISCUSSION:
            {
                //normalizeDate(values);
                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsDeleted = db.delete(DataContract.DiscussionEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            case CONTENT:
            {
                //normalizeDate(values);
                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsDeleted = db.delete(DataContract.ContentEntry.TABLE_NAME,selection,selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;


        switch (match)
        {
            case MODULE:
            {
                rowsUpdated = db.update(DataContract.ModuleEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case ANNOUNCEMENT:
            {
               // normalizeANNDate(values);
                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsUpdated = db.update(DataContract.AnnouncementEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            }
            case ASSIGNMENT:
            {
                //normalizeASDate(values);
                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsUpdated = db.update(DataContract.AssignmentEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            }

            case DISCUSSION:
            {
                //normalizeDDate(values);
                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsUpdated = db.update(DataContract.DiscussionEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            }

            case CONTENT:
            {
                //normalizeANNDate(values);
                //long _id = db.insert(DataContract.WeatherEntry.TABLE_NAME, null, values);
                rowsUpdated = db.update(DataContract.ContentEntry.TABLE_NAME, values, selection, selectionArgs);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
//        db.close();

        return rowsUpdated;
    }
}