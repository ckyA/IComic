package com.cky.icomic.Bean.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.RequestComic.OnFirstImageResponseListener;
import com.cky.icomic.RequestComic.OnRequestFinishListener;
import com.cky.icomic.RequestComic.RequestComic;

/**
 * Created by Admin on 2018/3/22.
 */

public class ImageCacheHelper {

    public static void requestFirstComicImage(final ComicBean comicBean, final Context context,
                                              @NonNull final OnRequestFinishListener onRequestFinishListener) {
        RequestComic.Instance.requestFirstComicImage(comicBean, new OnFirstImageResponseListener() {
            @Override
            public void onResponse(String url) {
                comicBean.setUrl(url);
                ContentValues values = new ContentValues();
                values.put("comic_name", comicBean.getName());
                values.put("comic_img", url);
                SQLiteHelper.getInstance(context).getWritableDatabase()
                        .insert(SQLiteHelper.FIRST_IMAGE, null, values);
                onRequestFinishListener.onRequestFinish();
            }
        });
    }

    public static String getFirstComicImage(String name, final Context context){

        Cursor cursor = SQLiteHelper.getInstance(context).getWritableDatabase()
                .rawQuery("select * from " + SQLiteHelper.FIRST_IMAGE
                + " where comic_name='" + name + "'", null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex("comic_img"));
        }else {
            return null;
        }
    }

}
