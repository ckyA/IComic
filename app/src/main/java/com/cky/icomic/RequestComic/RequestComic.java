package com.cky.icomic.RequestComic;

import android.support.annotation.NonNull;
import android.util.Log;

import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.ComicInfo;
import com.cky.icomic.Bean.ComicList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 2018/3/19.
 */

public class RequestComic {

    public static RequestComic Instance = new RequestComic();

    private static OkHttpClient client = new OkHttpClient();

    private RequestComic() {
    }

    //    返回漫画列表
//    key	    是	string	应用APPKEY
//    name	    否	string	漫画名称
//    type	    否	string	漫画类别
//    skip	    否	int	    跳过的数量
//    finish	否	int	    0代表未完结,1代表已完结,默认所有
//    {
//        "error_code": 200,
//            "reason": "请求成功！",
//            "result": {
//        "total": 20492,
//                "limit": 20,
//                "bookList": [
//        {
//            "name": "大话降龙",/*漫画名称*/
//                "type": "少年漫画",/*漫画类型*/
//                "area": "国漫",/*国家*/
//                "des": "",/*介绍*/
//                "finish": false,/*是否更新完*/
//                "lastUpdate": 20150508/*最新更新时间*/
//        },
//        {
//            "name": "秀逗高校",
//                "type": "少年漫画",
//                "area": "日本漫画",
//                "des": "",
//                "finish": false,
//                "lastUpdate": 20150504
//        },
//...]}}
    private final static String book_url =
            "http://japi.juhe.cn/comic/book?key=fd15f7935999e6399aa326faf7aac121";


    //    返回单独漫画的详情
//    key	        是	string	应用APPKEY
//    comicName	    是	string	漫画名称
//    skip	        否	int	    跳过条数
//    最大20
//    {
//        "error_code": 200,
//            "reason": "请求成功！",
//            "result": {
//                "total": 558,
//                "limit": 20,
//                "comicName": "火影忍者",
//                "chapterList": [
//        {
//            "name": "第01卷",/*章节名称*/
//                "id": 139833/*章节id*/
//        },
//        {
//            "name": "第02卷",
//                "id": 139834
//        },
//        {
//            "name": "第03卷",
//                "id": 139836
//        },
//        {
//            "name": "第04卷",
//                "id": 139837
//        },
//]}}
    private final static String chapter_url =
            "http://japi.juhe.cn/comic/chapter?key=fd15f7935999e6399aa326faf7aac121";


    //    返回单独漫画的单独话的图片URL
//    key	        是	string	应用APPKEY
//    comicName	    是	string	漫画名称
//    id	        是	int	    章节ID
//    {
//        "error_code": 200,
//            "reason": "请求成功！",
//            "result": {
//        "imageList": [
//        {
//            "imageUrl": "http://imgs.juheapi.com/comic_xin/6L6b5be06L6%2B55qE5YaS6Zmp/237401/0-MjM3NDAxMA==.jpg",/*图片URL*/
//                "id": 1/*id*/
//        },
//        {
//            "imageUrl": "http://imgs.juheapi.com/comic_xin/6L6b5be06L6%2B55qE5YaS6Zmp/237401/1-MjM3NDAxMQ==.jpg",
//                "id": 2
//        },
//...}}
    private final static String content_url =
            "http://japi.juhe.cn/comic/chapterContent?key=fd15f7935999e6399aa326faf7aac121";

    public void requestRecommendComicList(int skip, final OnResponseListener onResponseListener) {
        String url = book_url;
        if (skip != 0) {
            url+="&skip="+skip;
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "requestRecommendComicList");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ComicList resRecommend = parseComicList(response.body().string());
                Log.i("tag", "response");
                if (onResponseListener != null)
                    onResponseListener.onResponse(resRecommend);
            }
        });
    }

    private ComicList parseComicList(String input) {
        ArrayList<ComicBean> mList = new ArrayList<>();
        try {
            JSONObject array = new JSONObject(input);
            JSONObject result = array.getJSONObject("result");
            JSONArray bookList = result.getJSONArray("bookList");
            for (int i = 0; i < bookList.length(); i++) {
                JSONObject o = (JSONObject) bookList.get(i);
                //String , String , String , String , String , String
                ComicBean comicBean = new ComicBean(o.getString("name"), o.getString("type")
                        , o.getString("area"), o.getString("des")
                        , o.getString("finish"), o.getString("lastUpdate"));
                mList.add(comicBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ComicList(mList);
    }

    public void requestComicInfo(@NonNull final ComicBean comicBean, int skip, @NonNull final OnComicInfoResponseListener onComicInfoResponseListener) {

        if (comicBean == null) return;

        Request request = new Request.Builder()
                .url(chapter_url + "&comicName=" + comicBean.getName())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "requestComicInfo");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseChapterList(response.body().string(), comicBean);
                onComicInfoResponseListener.OnComicInfoResponse(comicBean);
            }

        });
    }

    private ArrayList<ComicInfo> parseChapterList(@NonNull String input, @NonNull ComicBean comicBean) {
        ArrayList<ComicInfo> mList = comicBean.comicList;
        try {
            JSONObject array = new JSONObject(input);
            JSONObject result = array.getJSONObject("result");
            comicBean.setTotal(result.getInt("total"));
            JSONArray chapterList = result.getJSONArray("chapterList");
            for (int i = 0; i < chapterList.length(); i++) {
                JSONObject obj = (JSONObject) chapterList.get(i);
                mList.add(new ComicInfo(obj.getString("name"), obj.getString("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public void requestFirstComicImage(ComicBean comicBean, final OnFirstImageResponseListener onFirstImageResponseListener) {

        if (comicBean.comicList.size() == 0) return;

        ComicInfo comicInfo = comicBean.comicList.get(0);
        Request request = new Request.Builder()
                .url(content_url + "&comicName=" + comicBean.getName() + "&id=" + comicInfo.getID())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String input = response.body().string();
                String url = null;
                try {
                    JSONObject array = new JSONObject(input);
                    JSONObject result = array.getJSONObject("result");
                    JSONArray imageList = result.getJSONArray("imageList");
                    JSONObject obj = (JSONObject) imageList.get(0);
                    url = obj.getString("imageUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onFirstImageResponseListener.onResponse(url);
            }
        });
    }

    public void requestImageURL(String id, String name, final OnImageURLResponseListener onImageURLResponseListener) {
        Request request = new Request.Builder()
                .url(content_url + "&comicName=" + name + "&id=" + id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                ArrayList<String> arr = parseImageURL(res);
                onImageURLResponseListener.onResponse(arr);
            }
        });
    }

    private ArrayList<String> parseImageURL(String input) {
        ArrayList<String> mList = new ArrayList<>();

        try {
            JSONObject array = new JSONObject(input);
            JSONObject result = array.getJSONObject("result");
            JSONArray imageList = result.getJSONArray("imageList");
            for (int i = 0; i < imageList.length(); i++) {
                JSONObject obj = (JSONObject) imageList.get(i);
                mList.add(obj.getString("imageUrl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mList;
    }

    public void requestSearchComic(@NonNull String name, final OnResponseListener onResponseListener) {
        Request request = new Request.Builder()
                .url(book_url + "&name=" + name)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ComicList resRecommend = parseComicList(response.body().string());
                Log.i("tag", "response");
                if (onResponseListener != null)
                    onResponseListener.onResponse(resRecommend);
            }
        });
    }

    public void requestTypeOfComic(String type, final OnResponseListener onResponseListener) {
        Request request = new Request.Builder()
                .url(book_url + "&type=" + type)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ComicList resRecommend = parseComicList(response.body().string());
                Log.i("tag", "response");
                if (onResponseListener != null)
                    onResponseListener.onResponse(resRecommend);
            }
        });
    }
}
