package com.example.finalmobile2025.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "finalmobile2025.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_PARTICIPANTS = "participants";

    // Participant table columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PARTICIPANT_ID = "participant_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_JOIN_CODE = "join_code";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Create table statements
    private static final String CREATE_TABLE_PARTICIPANTS = 
            "CREATE TABLE " + TABLE_PARTICIPANTS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_PARTICIPANT_ID + " TEXT UNIQUE NOT NULL," +
            COLUMN_USERNAME + " TEXT NOT NULL," +
            COLUMN_JOIN_CODE + " TEXT NOT NULL," +
            COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PARTICIPANTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        onCreate(db);
    }
}
