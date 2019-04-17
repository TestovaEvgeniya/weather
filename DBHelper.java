package com.example.list;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "citydb.db";
    final static String TABLE_NAME = "cities";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME + " (_id INT PRIMARY KEY, name VARCHAR(30) NOT NULL, country VARCHAR(3) NOT NULL)");
        db.execSQL("INSERT INTO cities VALUES (2023469, \"Irkutsk\", \"RU\");");
        db.execSQL("INSERT INTO cities VALUES (524901, \"Moscow\", \"RU\");");
        db.execSQL("INSERT INTO cities VALUES (1016666, \"Botswana\", \"ZA\");");
        db.execSQL("INSERT INTO cities VALUES (5340687, \"Creston\", \"US\");");
        db.execSQL("INSERT INTO cities VALUES (4364519, \"Oliver Beach\", \"US\");");
        db.execSQL("INSERT INTO cities VALUES (7839805, \"Melbourne\", \"AU\");");
        db.execSQL("INSERT INTO cities VALUES (7839796, \"Kingston\", \"AU\");");
        db.execSQL("INSERT INTO cities VALUES (2154787, \"Nowra\", \"AU\");");
        db.execSQL("INSERT INTO cities VALUES (7839760, \"Oberon\", \"AU\");");
        db.execSQL("INSERT INTO cities VALUES (473537, \"Vinogradovo\", \"RU\");");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public boolean addData(int id, String name, String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", id);
        contentValues.put("name", name);
        contentValues.put("country", country);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
