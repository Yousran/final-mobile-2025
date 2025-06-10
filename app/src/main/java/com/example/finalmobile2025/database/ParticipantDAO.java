package com.example.finalmobile2025.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ParticipantDAO {
    private DatabaseHelper dbHelper;

    public ParticipantDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertParticipant(String participantId, String username, String joinCode) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PARTICIPANT_ID, participantId);
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_JOIN_CODE, joinCode);
        
        long result = db.insert(DatabaseHelper.TABLE_PARTICIPANTS, null, values);
        db.close();
        return result;
    }

    public String getLatestParticipantId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String participantId = null;
        
        Cursor cursor = db.query(
            DatabaseHelper.TABLE_PARTICIPANTS,
            new String[]{DatabaseHelper.COLUMN_PARTICIPANT_ID},
            null, null, null, null,
            DatabaseHelper.COLUMN_CREATED_AT + " DESC",
            "1"
        );
        
        if (cursor.moveToFirst()) {
            participantId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARTICIPANT_ID));
        }
        
        cursor.close();
        db.close();
        return participantId;
    }

    public boolean isParticipantExists(String participantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;
        
        Cursor cursor = db.query(
            DatabaseHelper.TABLE_PARTICIPANTS,
            new String[]{DatabaseHelper.COLUMN_PARTICIPANT_ID},
            DatabaseHelper.COLUMN_PARTICIPANT_ID + "=?",
            new String[]{participantId},
            null, null, null
        );
        
        exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public void deleteParticipant(String participantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_PARTICIPANTS, 
                 DatabaseHelper.COLUMN_PARTICIPANT_ID + "=?", 
                 new String[]{participantId});
        db.close();
    }
}
