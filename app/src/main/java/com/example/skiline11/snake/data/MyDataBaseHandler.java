package com.example.skiline11.snake.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "maxResults.db";
    public static final String TABLE = "maxResults";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RESULT = "maxResult";

    public MyDataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("*", db.isOpen() + "");
        String query = "CREATE TABLE " + TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_RESULT + " INTEGER" +
                ");";
        db.execSQL(query);
        query = "INSERT INTO " + TABLE + " VALUES (1, 0)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("*", "Wykonuje się onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public int getMaxResult() {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE + " WHERE " + COLUMN_ID + " = 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int maxResult = c.getInt(c.getColumnIndex(COLUMN_RESULT));
        c.close();
        db.close();
        Log.i("getMaxResult zwróci : ", maxResult + "");
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
        String query = "UPDATE " + TABLE + " SET " + COLUMN_RESULT + " = " + maxResult + " WHERE " + COLUMN_ID + " = 1";
        db.execSQL(query);
        int max = getMaxResult();
        db.close();
        Log.i("*", "Max po update wynosi " + max);
    }
}
