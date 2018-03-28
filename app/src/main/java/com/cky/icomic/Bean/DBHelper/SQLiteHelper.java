package com.cky.icomic.Bean.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 2018/3/22.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "iComic.db3";
    public final static String TABLE_SUBSCRIPTION = "subscription";
    public final static String FIRST_IMAGE = "first_image";
    public final static int VERSION = 6;

    //储存订阅漫画
    private final String CREATE_TABLE_SUBSCRIPTION = "create table " + TABLE_SUBSCRIPTION
            + "(comic_name primary key, comic_bean  BLOB);";

    //储存漫画对应的图片。。
    private final String CREATE_TABLE_FIRST_IMAGE = "create table " + FIRST_IMAGE
            + "(comic_name primary key, comic_img);";

    private static SQLiteHelper instance;

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context, DB_NAME, null, VERSION);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SUBSCRIPTION);
        db.execSQL(CREATE_TABLE_FIRST_IMAGE);
        Log.i("DATABASE","onCreate: "+VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists " + TABLE_SUBSCRIPTION);
            db.execSQL("drop table if exists " + FIRST_IMAGE);
            onCreate(db);
            Log.i("DATABASE","onUpgrade: "+newVersion);
        }
    }

    public static byte[] getObjectStream(Object o) {
        byte[] data = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            data = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Object parseObjectStream(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Object o = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            o = objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }
}
