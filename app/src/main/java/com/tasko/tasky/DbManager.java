package com.tasko.tasky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbManager extends SQLiteOpenHelper {

    public static final String TABLENAME= "notes";
    public static final String _id="_id";
    public static final String SUBJECT="subject";
    public static final String DESC="description";
    public static final String DATE="date";
    public static final String EMAIL="email";

    private static final String CREATE_TABLE="CREATE TABLE "+TABLENAME+"("+ _id+
            " INTEGER PRIMARY KEY AUTOINCREMENT, "+ SUBJECT+ " TEXT NOT NULL, "+DESC+" TEXT, "
            +DATE+" TEXT, "+EMAIL+" TEXT, FOREIGN KEY(email) REFERENCES userdb(email));";
    public DbManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE userdb(name TEXT, email TEXT PRIMARY KEY)";
        db.execSQL(create);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = String.valueOf("DROP TABLE IF EXISTS userdb");
        db.execSQL(drop);
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        onCreate(db);
    }

    public void onLogout(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        onCreate(db);
    }
    public void addUserData(signup usern) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", usern.getUsername());
        contentValues.put("email", usern.getEmail());
        long k = db.insert("userdb", null, contentValues);
        Log.d("tag", Long.toString(k));
        db.close();
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("userdb", new String[]{"name", "email"}, "email=?",
                new String[]{email}, null, null, null);
        String Error = "User";
        if (cursor != null && cursor.moveToFirst()) {
            Log.d("tag", cursor.getString(0));
            return cursor.getString(0);
        } else {
            Log.d("tag", "some error found");
            return Error;
        }
    }

}
