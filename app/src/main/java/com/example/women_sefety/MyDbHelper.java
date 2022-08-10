package com.example.women_sefety;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public MyDbHelper(Context context) {
        super(context, "Women_Safety.DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contactsDb (id INTEGER PRIMARY key AUTOINCREMENT,name text,mobile text UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists contactsDb");
    }

    public long insertContact(String name, String number) {
        try {
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("mobile", number);
            return (db.insertOrThrow("contactsDb", null, contentValues));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateContact(String name, String number) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("mobile", number);
        long n = db.update("contactsDB", contentValues, "Mobile=?", new String[]{number});
        if (n == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<EmergencyContact> getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select *from contactsDb", null);
        ArrayList<EmergencyContact> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            arrayList.add(new EmergencyContact(cursor.getString(1), cursor.getString(2)));
        }

        return arrayList;
    }

    public Cursor MobileNumber() {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select *from contactsDB", null);
        return cursor;
    }

    public int DeleteContact(String number) {
        db = this.getWritableDatabase();
        int flag = db.delete("contactsDB", "Mobile=?", new String[]{number});
        if (flag > 0)
            return flag;
        return -1;
    }
}
