package com.cky.icomic.Bean.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.ComicList;

import java.util.ArrayList;

/**
 * Created by Admin on 2018/3/22.
 */

public class Subscription {

    public static void subscribeComic(ComicBean comicBean, Context context) {
        ContentValues values = new ContentValues();
        values.put("comic_name", comicBean.getName());
        values.put("comic_bean", SQLiteHelper.getObjectStream(comicBean));
        SQLiteDatabase db = SQLiteHelper.getInstance(context).getWritableDatabase();
        db.insert(SQLiteHelper.TABLE_SUBSCRIPTION, null, values);
        db.close();
    }

    public static void removeSubscribe(String name, Context context) {
        SQLiteDatabase db = SQLiteHelper.getInstance(context).getWritableDatabase();
        db.delete(SQLiteHelper.TABLE_SUBSCRIPTION, "comic_name = ?", new String[]{name});
        db.close();
    }

    public static void updateComicBean(ComicBean comicBean, Context context) {
        ContentValues values = new ContentValues();
        values.put("comic_name", comicBean.getName());
        values.put("comic_bean", SQLiteHelper.getObjectStream(comicBean));
        SQLiteDatabase db = SQLiteHelper.getInstance(context).getWritableDatabase();
        db.update(SQLiteHelper.TABLE_SUBSCRIPTION, values,
                "comic_name = '" + comicBean.getName() + "';", null);
        db.close();
    }

    public static ComicBean getSubscriptionComicBean(Context context, String name) {
        SQLiteDatabase db = SQLiteHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + SQLiteHelper.TABLE_SUBSCRIPTION
                + " where comic_name='" + name + "'", null);
//                .query(SQLiteHelper.TABLE_SUBSCRIPTION, new String[]{"*"},
//                        "comic_name = '" + name + "';", null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        } else {
            byte[] data = cursor.getBlob(cursor.getColumnIndex("comic_bean"));
            cursor.close();
            return (ComicBean) SQLiteHelper.parseObjectStream(data);
        }
    }

    public static ComicList getSubscriptionComicBeans(Context context) {
        SQLiteDatabase db = SQLiteHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(SQLiteHelper.TABLE_SUBSCRIPTION, new String[]{"*"},
                        null, null, null, null, null);
        return parseCursor(cursor);
    }

    private static ComicList parseCursor(Cursor cursor) {
        if (cursor.getCount() == 0) return null;

        ArrayList<ComicBean> list = new ArrayList<>();
        ComicBean comicBean = null;
        byte[] data = null;
        cursor.moveToFirst();
        data = cursor.getBlob((cursor.getColumnIndex("comic_bean")));
        comicBean = (ComicBean) SQLiteHelper.parseObjectStream(data);
        list.add(comicBean);

        while (cursor.moveToNext()) {
            data = cursor.getBlob((cursor.getColumnIndex("comic_bean")));
            comicBean = (ComicBean) SQLiteHelper.parseObjectStream(data);
            list.add(comicBean);
        }
        cursor.close();
        return new ComicList(list);
    }

    public static boolean isSubscription(String name, Context context) {
        SQLiteDatabase db = SQLiteHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(SQLiteHelper.TABLE_SUBSCRIPTION, new String[]{"comic_name"},
                        "comic_name = '" + name + "';", null, null, null, null);
        if (cursor.getCount() == 0) return false;
        return true;
    }
}
