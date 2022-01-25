package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.passwordmanager.Password;

import java.util.ArrayList;
import java.util.List;

public class DBhandler extends SQLiteOpenHelper {
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String TABLE="Passwords";
    public DBhandler(@Nullable Context context)
    {
        super(context,"test.db",null,3);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("Create table %s (%s Integer PRIMARY KEY AUTOINCREMENT,%s Text," +
                " %s Text,%s Text, %s Text);",TABLE,ID,USERNAME,EMAIL,PASSWORD,ACCOUNT_TYPE);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int neww) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(sqLiteDatabase);
    }
    public void insert(Password pass)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(PASSWORD,pass.password);
        cv.put(USERNAME,pass.userName);
        cv.put(EMAIL,pass.email);
        cv.put(ACCOUNT_TYPE,pass.accountType);
        sqLiteDatabase.insert(TABLE,null,cv);
        sqLiteDatabase.close();
    }

    public void update(Password password)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String query=String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s' ",TABLE,PASSWORD,password.password,EMAIL,password.email,ACCOUNT_TYPE,password.accountType);
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }


    public void delete(String email, String account_type)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        String query=String.format("Delete from %s where %s='%s' AND %s='%s'",TABLE,EMAIL,email,ACCOUNT_TYPE,account_type);
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }
    public List<Password> getPasswords()
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query=String.format("SELECT %s,%s,%s,%s FROM %s ORDER BY %s",USERNAME,EMAIL,PASSWORD,ACCOUNT_TYPE,TABLE,ACCOUNT_TYPE);
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        List<Password> list=new ArrayList<Password>();
        if(cursor.moveToFirst())
        {
            do{
                Password p=new Password(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                list.add(p);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public Password getPassword(String accountType)
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query=String.format("SELECT %s,%s,%s,%s FROM %s WHERE %s='%s'",USERNAME,EMAIL,PASSWORD,ACCOUNT_TYPE,TABLE,ACCOUNT_TYPE,accountType);
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        List<Password> list=new ArrayList<Password>();
        if(cursor.moveToFirst())
        {
            return new Password(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        }
        cursor.close();
        return null;
    }
    public boolean isAccountPresent(String accountType){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query=String.format("SELECT %s FROM %s WHERE %s = '%s'",ACCOUNT_TYPE,TABLE,ACCOUNT_TYPE,accountType);
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        if(cursor.moveToFirst()){
            return true;
        }
        return false;
    }
}
