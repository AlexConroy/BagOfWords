package com.alex.bagofwords;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bag_of_words.db";
    public static final String TABLE_NAME = "bag_of_words_novice";
    public static final String COL_1 = "NUMBER";
    public static final String COL_2 = "first_word";
    public static final String COL_3 = "second_word";
    public static final String COL_4 = "third_word";
    public static final String COL_5 = "fourth_word";



    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (NUMBER INTEGER PRIMARY KEY AUTOINCREMENT, first_word TEXT, second_word TEXT, third_word TEXT, fourth_word TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String firstWord, String secondWord, String thirdWord, String fourthWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, firstWord);
        contentValues.put(COL_3, secondWord);
        contentValues.put(COL_4, thirdWord);
        contentValues.put(COL_5, fourthWord);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
