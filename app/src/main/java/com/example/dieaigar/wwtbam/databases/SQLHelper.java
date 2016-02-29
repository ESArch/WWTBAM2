package com.example.dieaigar.wwtbam.databases;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pablo on 28/02/2016.
 */
public class SQLHelper extends SQLiteOpenHelper {
    // Singleton pattern to centralize access to the database
    private static SQLHelper instance;

    public synchronized static SQLHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLHelper(context.getApplicationContext());
        }
        return instance;
    }

    /*
        Create a helper object to manage a database
    */
    private SQLHelper(Context context) {
        // Parameters:
        //  context
        //  filename of the database, or null for in-memory database
        //  factory to create cursor objects, default if null
        //  version of the database (upgrades/downgrades existing ones)
        super(context, "score_database", null, 1);
    }

    /*
        This method is only called to create the database the first time it is accessed
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create a QuotationTable table with
        //  autoincremental integer primary key: id
        //  String unique: text
        //  String: author
        db.execSQL("CREATE TABLE ScoreTable " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, score INTEGER NOT NULL);");
    }

    /*
        This is method is only called when the database needs to be upgraded,
        so it has been left blank
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<HashMap<String, String>> getScores() {

        ArrayList<HashMap<String, String>> result = new ArrayList<>();
        HashMap<String, String> item;

        // Get access to the database in read mode
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                "ScoreTable", new String[]{"name", "score"}, null, null, null, null, null);
        // Go through the resulting cursor
        while (cursor.moveToNext()) {
            item = new HashMap<>();
            item.put("name", cursor.getString(0));
            item.put("score", cursor.getString(1));
            // Add the object to the result list
            result.add(item);
        }
        // Close cursor and database helper
        cursor.close();
        database.close();

        return result;
    }

    /*
        Insert a new quotation into the database
    */
    public void addScore(String name, String score) {

        // Get access to the database in write mode
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("score", score);
        database.insert("ScoreTable", null, values);
        // Close the database helper
        database.close();
    }

    /*
        Remove all entries from the database
    */
    public void clearAllScores() {

        // Get access to the database in write mode
        SQLiteDatabase database = getWritableDatabase();
        // Delete all entries from the table
        database.delete("ScoreTable", null, null);
        // Close the database helper
        database.close();
    }

    /*
        Delete a given quotation from the database
    */
    public void deleteScore(String score) {

        // Get access to the database in write mode
        SQLiteDatabase database = getWritableDatabase();
        // Delete from the table any entry with the given score
        database.delete("ScoreTable", "name=?", new String[]{score});
        // Close the database helper
        database.close();
    }
}