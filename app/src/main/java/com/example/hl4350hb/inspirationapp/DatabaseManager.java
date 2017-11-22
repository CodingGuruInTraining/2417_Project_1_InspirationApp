package com.example.hl4350hb.inspirationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 */

public class DatabaseManager {

    private Context context;
    private SQLHelper helper;
    private SQLiteDatabase db;

    protected static final String DB_NAME = "inspires";
    protected static final int DB_VERSION = 2;
    protected static final String DB_TABLE = "inspirepics";
    protected static final String ID_COL = "_id";
    protected static final String NOTE_COL = "notes";
    protected static final String IMG_COL = "imageid";
    protected static final String DATE_COL = "datetaken";



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
        Cursor cursor = db.query(DB_TABLE, null, null, null, null, null, DATE_COL);
        return cursor;
    }

    public boolean addNote(String note, String imageid, long date) {
        ContentValues newProduct = new ContentValues();
        newProduct.put(NOTE_COL, note);
        newProduct.put(IMG_COL, imageid);
        newProduct.put(DATE_COL, date);
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


    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper(Context c) {
            super(c,DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createSQLbase = "CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER UNIQUE )";
            String createSQL = String.format(createSQLbase, DB_TABLE, ID_COL, NOTE_COL, IMG_COL, DATE_COL);
            db.execSQL(createSQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}
