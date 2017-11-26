package com.example.hl4350hb.inspirationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 *
 */

public class DatabaseManager {

    private Context context;
    private SQLHelper helper;
    private SQLiteDatabase db;

    protected static final String DB_NAME = "inspires";
    protected static final int DB_VERSION = 3;
    protected static final String DB_TABLE = "inspirepics";
    protected static final String ID_COL = "_id";
    protected static final String NOTE_COL = "notes";
    protected static final String IMG_COL = "imageid";
    protected static final String DATE_COL = "datetaken";
    protected static final String HASH_COL = "hashtags";



    public DatabaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
//        this.db.delete(DB_TABLE, null, null);
    }

    public void close() {
        helper.close();
    }

    public Cursor getAllPics() {
        // Queries database for all entries and sorts them with newest entry first.
        return db.query(DB_TABLE, null, null, null, null, null, DATE_COL + " DESC");
    }

    public boolean addNote(String note, String imageid, long date, String hashtags) {
        ContentValues newProduct = new ContentValues();
        newProduct.put(NOTE_COL, note);
        newProduct.put(IMG_COL, imageid);
        newProduct.put(DATE_COL, date);
        newProduct.put(HASH_COL, hashtags);
        try {
            db.insertOrThrow(DB_TABLE, null, newProduct);
            return true;
        } catch (SQLiteConstraintException err) {
            return false;
        }
    }

    public boolean updateNote(int rowID, String newNote) {
        ContentValues changeNote = new ContentValues();
        changeNote.put(NOTE_COL, newNote);

        String where = ID_COL + " = ? ";
        String[] whereArgs = {Integer.toString(rowID)};
        int rowsMod = db.update(DB_TABLE, changeNote, where, whereArgs);
        if (rowsMod == 1) {
            return true;
        }
        return false;
    }

    public String findNote(int rowId) {
//        ContentValues queryNote = new ContentValues();
        Cursor cursor = null;
        String result;
        String where = ID_COL + " = ? ";
        String[] whereArgs = {Integer.toString(rowId)};
        try {
            cursor = db.query(DB_TABLE, null, where, whereArgs, null, null, null);

            cursor.moveToFirst();
            int noteCol = cursor.getColumnIndex(NOTE_COL);
            result = cursor.getString(noteCol);
        } finally {
            cursor.close();
        }
        return result;
    }

    public ArrayList<PictureEntry> findNote(String search, int field) {
        String where = "";
        String[] whereArgs = {"%" + search + "%"};

        switch (field) {
            case 1:
                where = NOTE_COL + " LIKE ? ";
                break;
            case 2:
                where = HASH_COL + " LIKE ? ";
                break;
        }
        Cursor cursor = db.query(DB_TABLE, null, where, whereArgs, null, null, null);
        if (cursor.getCount() > 0) {
            ArrayList<PictureEntry> results = new ArrayList<PictureEntry>();
            int noteCol = cursor.getColumnIndex(NOTE_COL);
            int imgCol = cursor.getColumnIndex(IMG_COL);
            int dateCol = cursor.getColumnIndex(DATE_COL);
            int hashCol = cursor.getColumnIndex(HASH_COL);
            while (cursor.moveToNext()) {
                String note = cursor.getString(noteCol);
                String img = cursor.getString(imgCol);
                long date = cursor.getLong(dateCol);
                String hash = cursor.getString(hashCol);
                results.add(new PictureEntry(note, img, date, hash));
            }
            return results;
        }
        return null;
    }

    public boolean deleteNote(int id) {
        db.delete(DB_TABLE, ID_COL + "=" + id, null);
        return true;
    }


    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper(Context c) {
            super(c,DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createSQLbase = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER UNIQUE, %s TEXT )";
            String createSQL = String.format(createSQLbase, DB_TABLE, ID_COL, NOTE_COL, IMG_COL, DATE_COL, HASH_COL);
            db.execSQL(createSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
