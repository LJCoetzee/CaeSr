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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class DataContract
{
    //The ""Content Authority" is a name of the entire content provider, similar to relationship
    //between a domain name and its website.
    //A convenient string to use for the content authority is the package name for the app,
    // which is guaranteed to be unique on the device.
    public static final String CONTENT_AUTHORITY = "com.example.cybercake.caesr";

    //CONTENT_AUTHORITY is used to create base of all URI's used by apps to contact the
    //content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Possible paths that can be appended to base uri:
    //-The content provider is aware of these different paths.
    //-If we are looking for modules:
    public static final String PATH_MODULE = "modules";
    //-If we want to tell the CP(content provider) we want announcements:
    public static final String PATH_ANNOUNCEMENT = "announcements";
    //-If we are looking for assignments:
    public static final String PATH_ASSIGNMENTS = "assignments";
    //-If we are looking for discussions:
    public static final String PATH_DISCUSSIONS = "discussions";
    //-If we are looking for module content:
    public static final String PATH_CONTENT = "content";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the modules table */
    public static final class ModuleEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MODULE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_MODULE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_MODULE;

        public static final String TABLE_NAME = "modules";

        //Column to identify module, stored as STRING
        public static final String COLUMN_MODULE = "module";

        //Description of the module, stored as STRING
        public static final String COLUMN_DESCRIPTION = "description";

        //A boolean field indicating if the user is subscribed to the module, stored as BOOLEAN
        public static final String COLUMN_SUB = "subscribed";

        //helper functions to help build URIs
        public static Uri buildModuleUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildModules(String module)
        {
            return CONTENT_URI.buildUpon().appendPath(module).build();
        }

        public static String getSubscriptionFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }
    }

    /*
        Inner class that defines the table contents of the announcements table
     */
    public static final class AnnouncementEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ANNOUNCEMENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_ANNOUNCEMENT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_ANNOUNCEMENT;

        public static final String TABLE_NAME = "announcements";

        //Column with the foreign key into the modules table.
        public static final String COLUMN_LOC_KEY = "module_id";

        //Title of the announcement, stored as STRING
        public static final String COLUMN_TITLE = "title";

        //Author who posted announcement, stored as STRING
        public static final String COLUMN_AUTHOR = "author";

        //Date announcement was posted, stored as LONG, in milliseconds since epoch
        public static final String COLUMN_DATE = "date";

        //Time announcement was posted, stored as LONG in seconds since start of day
        public static final String COLUMN_TIME = "time";

        //Announcement body, stored was STRING
        public static final String COLUMN_BODY = "body";

        //Whether the announcement has been viewed or not, stored as boolean
        public static final String COLUMN_VIEWED = "viewed";

        //helper functions to help build URIs
        public static Uri buildAnnouncementUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAnnouncementWithModule(String module)
        {
            return CONTENT_URI.buildUpon().appendPath(module).build();
        }

        public static Uri buildAnnouncementModuleWithTitle(String module, String title)
        {
            return CONTENT_URI.buildUpon().appendPath(module)
                    .appendPath(title).build();
        }

        public static String getModuleFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getTitleUri(Uri uri)
        {

            return uri.getPathSegments().get(2);
        }
    }

    /*
        Inner class that defines the table contents of the assignments table
     */
    public static final class AssignmentEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ASSIGNMENTS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_ASSIGNMENTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_ASSIGNMENTS;

        public static final String TABLE_NAME = "assignments";

        //Column with the foreign key into the modules table.
        public static final String COLUMN_LOC_KEY = "module_id";

        //Title of the assignments, stored as STRING
        public static final String COLUMN_TITLE = "title";

        //Date assignments is due, stored as LONG, in milliseconds since epoch
        public static final String COLUMN_DUEDATE = "due_date";

        //Time assignments is due, stored as LONG in secons since start of day
        public static final String COLUMN_DUETIME = "due_time";

        //Whether the assignments has been viewed or not, stored as boolean
        public static final String COLUMN_VIEWED = "viewed";

        //helper functions to help build URIs
        public static Uri buildAssignmentUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildAssignmentsWithModule(String module)
        {
            return CONTENT_URI.buildUpon().appendPath(module).build();
        }

        public static Uri buildAssignmentModuleWithTitle(String module, String title)
        {
            return CONTENT_URI.buildUpon().appendPath(module)
                    .appendPath(title).build();
        }

        public static String getModuleFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getTitleUri(Uri uri)
        {

            return uri.getPathSegments().get(2);
        }
    }

    /*
        Inner class that defines the table contents of the discussions table
     */
    public static final class DiscussionEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DISCUSSIONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_DISCUSSIONS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_DISCUSSIONS;

        public static final String TABLE_NAME = "discussions";

        // Column with the foreign key into the modules table.
        public static final String COLUMN_LOC_KEY = "module_id";

        //Title of the discussions, stored as STRING
        public static final String COLUMN_TITLE = "title";

        //Link to the module, stored as STRING
        public static final String COLUMN_LINK = "link";

        //Date discussions was last updated, stored as LONG, in milliseconds since epoch
        public static final String COLUMN_LUDATE = "lu_date";

        //Time discussions was last updated, stored as LONG in secons since start of day
        public static final String COLUMN_LUTIME = "lu_time";

        //Whether the discussions has been viewed or not, stored as boolean
        public static final String COLUMN_VIEWED = "viewed";

        //helper functions to help build URIs
        public static Uri buildDiscussionUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDiscussionWithModule(String module)
        {
            return CONTENT_URI.buildUpon().appendPath(module).build();
        }

        public static Uri buildDiscussionModuleWithTitle(String module, String title)
        {
            return CONTENT_URI.buildUpon().appendPath(module)
                    .appendPath(title).build();
        }

        public static String getModuleFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getTitleUri(Uri uri)
        {

            return uri.getPathSegments().get(2);
        }
    }

    /*
        Inner class that defines the table contents of the content table
     */
    public static final class ContentEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_CONTENT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/"
                        + PATH_CONTENT;

        public static final String TABLE_NAME = "content";

        // Column with the foreign key into the modules table.
        public static final String COLUMN_LOC_KEY = "module_id";

        //Category of the content, stored as STRING
        public static final String COLUMN_CATEGORY = "category";

        //Title of the content, stored as STRING
        public static final String COLUMN_TITLE = "title";

        //Link to the module website, stored as STRING
        public static final String COLUMN_LINK = "link";

        //Size of the content, stored as STRING
        public static final String COLUMN_SIZE = "size";

        //Whether the content has been viewed or not, stored as boolean
        public static final String COLUMN_VIEWED = "viewed";

        //helper functions to help build URIs
        public static Uri buildContentUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildContentWithModule(String module)
        {
            return CONTENT_URI.buildUpon().appendPath(module).build();
        }

        public static Uri buildContentModuleWithTitle(String module, String title)
        {
            return CONTENT_URI.buildUpon().appendPath(module)
                    .appendPath(title).build();
        }

        public static String getModuleFromUri(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String getCategoryFromUri(Uri uri)
        {
            return uri.getPathSegments().get(2);
        }

        public static String getTitleUri(Uri uri)
        {

            return uri.getPathSegments().get(3);
        }
    }
}