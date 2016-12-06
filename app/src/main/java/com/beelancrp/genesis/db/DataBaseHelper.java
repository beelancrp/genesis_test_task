package com.beelancrp.genesis.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.beelancrp.genesis.App;
import com.beelancrp.genesis.data.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilan on 06.12.2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DEBUG_TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final int NO_LIMIT = -1;
    private static final String DATABASE_NAME = "app_data.db";

    // Search results table
    private static final String RESULTS_TABLE_NAME = "Results";

    private static final String RESULTS_COLUMN_ID = "_id";
    private static final String RESULTS_COLUMN_TITLE = "title";
    private static final String RESULTS_COLUMN_URL = "url";

    private static final String SQL_CREATE_RESULTS_TABLE =
            "CREATE TABLE " + RESULTS_TABLE_NAME + " ("
                    + RESULTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RESULTS_COLUMN_TITLE + " TEXT, "
                    + RESULTS_COLUMN_URL + " TEXT);";

    // Terms table
    private static final String TERMS_TABLE_NAME = "Terms";

    private static final String TERMS_COLUMN_ID = "id";
    private static final String TERMS_COLUMN_NAME = "term";

    private static final String SQL_CREATE_TERMS_TABLE =
            "CREATE TABLE " + TERMS_TABLE_NAME + " ("
                    + TERMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TERMS_COLUMN_NAME + " TEXT);";

    private static DataBaseHelper dbHelper = null;
    private SQLiteDatabase database = null;

    public static DataBaseHelper getDatabaseHelper() {
        if (dbHelper != null)
            return dbHelper;
        dbHelper = new DataBaseHelper(App.getInstance().getApplicationContext());
        dbHelper.database = dbHelper.getWritableDatabase();
        return dbHelper;
    }

    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createLogsTable(db);
    }

    private void createLogsTable(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RESULTS_TABLE);
        db.execSQL(SQL_CREATE_TERMS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RESULTS_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TERMS_TABLE_NAME + ";");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public boolean isResultsIDSaved(int resultID) {
        Cursor c = database.query(
                DataBaseHelper.RESULTS_TABLE_NAME,
                null,
                DataBaseHelper.RESULTS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(resultID)},
                null,
                null,
                null
        );

        if (!c.moveToFirst()) {
            c.close();
            return false;
        }

        return true;
    }

    public void addResult(SearchItem resultEntity) {

        ContentValues values = new ContentValues();
        values.put(RESULTS_COLUMN_TITLE, resultEntity.getItemTitle());
        values.put(RESULTS_COLUMN_URL, resultEntity.getItemLink());

        database.insert(RESULTS_TABLE_NAME, null, values);
    }


    public List<Integer> getResultsIDList() {
        return getResultsIDList(NO_LIMIT);
    }

    public List<Integer> getResultsIDList(int limit) {
        List<Integer> comments = new ArrayList<>();

        Cursor cursor = database.query(true,
                DataBaseHelper.RESULTS_TABLE_NAME,
                new String[]{DataBaseHelper.RESULTS_COLUMN_ID},
                null,
                null,
                null,
                null,
                DataBaseHelper.RESULTS_COLUMN_ID + " DESC",
                null
        );

        if (!cursor.moveToFirst()) {
            cursor.close();
            return comments;
        }

        while (!cursor.isAfterLast()) {
            Integer resultID = cursor.getInt(0);
            comments.add(resultID);

            //stop if limit
            if (comments.size() == limit && limit != NO_LIMIT) {
                break;
            }

            cursor.moveToNext();
        }

        return comments;
    }

    public SearchItem getResult(int resultID) {
        SearchItem resultEntity = null;

        Cursor cursor = database.query(
                DataBaseHelper.RESULTS_TABLE_NAME,
                null,
                DataBaseHelper.RESULTS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(resultID)},
                null,
                null,
                null
        );

        if (!cursor.moveToFirst()) {
            cursor.close();
            return resultEntity;
        }

        resultEntity = new SearchItem();
        resultEntity.setItemTitle(cursor.getString(1));
        resultEntity.setItemLink(cursor.getString(2));

        return resultEntity;
    }

    public List<SearchItem> getAllResults() {
        List<SearchItem> items = new ArrayList<>();
        for (int i : getResultsIDList()) {
            items.add(getResult(i));
        }
        return items;
    }

    public boolean isTermSaved(int termID) {
        Cursor c = database.query(
                DataBaseHelper.TERMS_TABLE_NAME,
                null,
                DataBaseHelper.TERMS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(termID)},
                null,
                null,
                null
        );


        if (!c.moveToFirst()) {
            c.close();
            return false;
        }

        return true;
    }

    public void addTerm(String term) {

        ContentValues values = new ContentValues();
        values.put(TERMS_COLUMN_NAME, term);

        database.insert(TERMS_TABLE_NAME, null, values);
    }

    public String getTerm(int termID) {
        String term = null;

        Cursor cursor = database.query(
                DataBaseHelper.TERMS_TABLE_NAME,
                null,
                DataBaseHelper.TERMS_COLUMN_ID + " = ?",
                new String[]{String.valueOf(termID)},
                null,
                null,
                null
        );


        if (!cursor.moveToFirst()) {
            cursor.close();
            return term;
        }

        term = "";
        term = cursor.getString(1);

        return term;
    }

    public void clearTermTable() {
        database.execSQL("DELETE FROM " + TERMS_TABLE_NAME);
    }

    public void clearResultTable() {
        database.execSQL("DELETE FROM " + RESULTS_TABLE_NAME);
    }

    public void clearAllTables() {
        database.execSQL("DELETE FROM " + TERMS_TABLE_NAME);
        database.execSQL("DELETE FROM " + RESULTS_TABLE_NAME);
    }
}
