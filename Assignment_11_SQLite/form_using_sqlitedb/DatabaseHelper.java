package com.example.form_using_sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StudentManagement.db";
    public static final String TABLE_NAME = "student_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "ROLL";
    public static final String COL_4 = "EMAIL";
    public static final String COL_5 = "PHONE";
    public static final String COL_6 = "GENDER";
    public static final String COL_7 = "DOB";
    public static final String COL_8 = "COURSE";
    public static final String COL_9 = "ADDRESS";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, ROLL TEXT, EMAIL TEXT, PHONE TEXT, GENDER TEXT, DOB TEXT, COURSE TEXT, ADDRESS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String roll, String email, String phone, String gender, String dob, String course, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, roll);
        contentValues.put(COL_4, email);
        contentValues.put(COL_5, phone);
        contentValues.put(COL_6, gender);
        contentValues.put(COL_7, dob);
        contentValues.put(COL_8, course);
        contentValues.put(COL_9, address);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    public boolean updateData(String id, String name, String roll, String email, String phone, String gender, String dob, String course, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, roll);
        contentValues.put(COL_4, email);
        contentValues.put(COL_5, phone);
        contentValues.put(COL_6, gender);
        contentValues.put(COL_7, dob);
        contentValues.put(COL_8, course);
        contentValues.put(COL_9, address);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
}
