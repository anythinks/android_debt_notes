package com.android.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLite extends SQLiteOpenHelper {

    public SQLite(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE USER (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR, PHONE VARCHAR, HUTANG INTEGER, TANGGAL_UPDATE VARCHAR)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void insert(String name, String phone, Integer hutang, String tgl_update){
        SQLiteDatabase dbWrite = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("NAME",name);
        contentValues.put("PHONE",phone);
        contentValues.put("HUTANG",hutang);
        contentValues.put("TANGGAL_UPDATE", tgl_update);

        dbWrite.insert("USER",null,contentValues);
        dbWrite.close();
    }

    void  update(String id, String name, String phone, Integer hutang, String tgl_update){
        SQLiteDatabase dbWrite = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("NAME",name);
        values.put("PHONE",phone);
        values.put("HUTANG",hutang);
        values.put("TANGGAL_UPDATE", tgl_update);

        dbWrite.update("USER", values, "ID = ?",new String[]{id});
        dbWrite.close();
    }

    void delete(String id){
        SQLiteDatabase dbWrite = getWritableDatabase();

        dbWrite.delete("USER","ID = ?",new String[]{id});
        dbWrite.close();
    }

    Cursor read(){
        SQLiteDatabase dbRead = getReadableDatabase();
        String query  = "SELECT * FROM USER";

        Cursor cursor = dbRead.rawQuery(query,null);
        return cursor;
    }

    Cursor readHutang(){
        SQLiteDatabase dbRead = getReadableDatabase();
        String query = "SELECT SUM(HUTANG) FROM USER";

        Cursor cursor = dbRead.rawQuery(query,null);
        return cursor;
    }
}
