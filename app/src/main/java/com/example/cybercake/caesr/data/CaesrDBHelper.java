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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cybercake.caesr.data.DataContract.AnnouncementEntry;
import com.example.cybercake.caesr.data.DataContract.ModuleEntry;
import com.example.cybercake.caesr.data.DataContract.AssignmentEntry;
import com.example.cybercake.caesr.data.DataContract.DiscussionEntry;
import com.example.cybercake.caesr.data.DataContract.ContentEntry;

/**
 * Manages a local database for weather data.
 */
public class CaesrDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "caesr.db";

    public CaesrDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        createModuleTable(sqLiteDatabase);
        createAnnouncementTable(sqLiteDatabase);
        createAssignmentTable(sqLiteDatabase);
        createDiscussionTable(sqLiteDatabase);
        createModuleContentTable(sqLiteDatabase);
    }

    private void createModuleTable(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + ModuleEntry.TABLE_NAME + " (" +
                ModuleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ModuleEntry.COLUMN_MODULE + " TEXT UNIQUE NOT NULL, " +
                ModuleEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ModuleEntry.COLUMN_SUB + " BOOLEAN NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    private void createAnnouncementTable(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + AnnouncementEntry.TABLE_NAME + " (" +
                AnnouncementEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AnnouncementEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                AnnouncementEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                AnnouncementEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                AnnouncementEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                AnnouncementEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                AnnouncementEntry.COLUMN_BODY + " TEXT NOT NULL, " +
                AnnouncementEntry.COLUMN_VIEWED + " BOOLEAN DEFAULT FALSE, " +

        // Set up the module column as a foreign key to module table.
        " FOREIGN KEY (" + AnnouncementEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                ModuleEntry.TABLE_NAME + " (" + ModuleEntry._ID + "), " +

        // To avoid duplicate announcements when an announcement has been updated
        " UNIQUE (" + AnnouncementEntry.COLUMN_TITLE + ", " +
        AnnouncementEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    private void createAssignmentTable(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + AssignmentEntry.TABLE_NAME + " (" +
                AssignmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AssignmentEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                AssignmentEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                AssignmentEntry.COLUMN_DUEDATE + " INTEGER NOT NULL, " +
                AssignmentEntry.COLUMN_DUETIME + " TEXT NOT NULL, " +
                AssignmentEntry.COLUMN_VIEWED + " BOOLEAN DEFAULT FALSE, " +

                // Set up the module column as a foreign key to module table.
                " FOREIGN KEY (" + AssignmentEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                ModuleEntry.TABLE_NAME + " (" + ModuleEntry._ID + "), " +

                // To avoid duplicate assignments when an assignments has been updated
                " UNIQUE (" + AssignmentEntry.COLUMN_TITLE + ", " +
                AssignmentEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    private void createDiscussionTable(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + DiscussionEntry.TABLE_NAME + " (" +
                DiscussionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DiscussionEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                DiscussionEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                DiscussionEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                DiscussionEntry.COLUMN_LUDATE + " INTEGER NOT NULL, " +
                DiscussionEntry.COLUMN_LUTIME + " TEXT NOT NULL, " +
                DiscussionEntry.COLUMN_VIEWED + " BOOLEAN DEFAULT FALSE, " +

                // Set up the module column as a foreign key to module table.
                " FOREIGN KEY (" + DiscussionEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                ModuleEntry.TABLE_NAME + " (" + ModuleEntry._ID + "), " +

                // To avoid duplicate assignments when an assignments has been updated
                " UNIQUE (" + DiscussionEntry.COLUMN_TITLE + ", " +
                DiscussionEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    private void createModuleContentTable(SQLiteDatabase sqLiteDatabase)
    {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + ContentEntry.TABLE_NAME + " (" +
                ContentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContentEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
//                ContentEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                ContentEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContentEntry.COLUMN_LINK+ " TEXT NOT NULL, " +
                ContentEntry.COLUMN_SIZE+ " TEXT NOT NULL, " +
                ContentEntry.COLUMN_VIEWED + " BOOLEAN DEFAULT FALSE, " +

                // Set up the module column as a foreign key to module table.
                " FOREIGN KEY (" + ContentEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                ModuleEntry.TABLE_NAME + " (" + ModuleEntry._ID + "), " +

                // To avoid duplicate assignments when an assignments has been updated
                " UNIQUE (" + ContentEntry.COLUMN_TITLE + ", " +
                ContentEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    public void resetDB(SQLiteDatabase sqLiteDatabase)
    {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ModuleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AnnouncementEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AssignmentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DiscussionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ModuleEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AnnouncementEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AssignmentEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DiscussionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}