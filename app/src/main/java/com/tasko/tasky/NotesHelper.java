package com.tasko.tasky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotesHelper {
    private DbManager notesdb;
    private Context context;
    private SQLiteDatabase database;
    public NotesHelper(Context c){
        context=c;
    }
    public NotesHelper open() throws SQLException{
        notesdb= new DbManager(context,"userdb",null,1);
        database= notesdb.getWritableDatabase();
        return this;
    }
    public void close(){
        notesdb.close();
    }
    public void insert(String name, String desc, String date, String email){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbManager.SUBJECT, name);
        contentValues.put(DbManager.DESC, desc);
        contentValues.put(DbManager.DATE,date);
        contentValues.put(DbManager.EMAIL, email);
        database.insert(DbManager.TABLENAME,null, contentValues);
    }
    public Cursor fetch(String email) {
        String[] columns = new String[]{DbManager._id, DbManager.SUBJECT, DbManager.DESC, DbManager.DATE, DbManager.EMAIL};

        Cursor cursor = database.query(DbManager.TABLENAME, columns, "email=?", new String[]{email},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
        public int update(long _id, String name,String desc, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbManager.SUBJECT,name);
        contentValues.put(DbManager.DESC,desc);
        contentValues.put(DbManager.DATE,date);
        int i = database.update(DbManager.TABLENAME, contentValues,
                DbManager._id+" = "+_id, null);

        return i;
        }

        public void delete(long _id){
        database.delete(DbManager.TABLENAME, DbManager._id+" = "+_id, null);
        }

}
