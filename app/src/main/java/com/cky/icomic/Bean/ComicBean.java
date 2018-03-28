package com.cky.icomic.Bean;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 2018/3/19.
 */

public class ComicBean implements Serializable {

    private static final long serialVersionUID = 450597786086529938L;
    //              "name":         "大话降龙",/*漫画名称*/
//              "type":          "少年漫画",/*漫画类型*/
//              "area":         "国漫",/*国家*/
//              "des":          "",/*介绍*/
//              "finish":       false,/*是否更新完*/
//              "lastUpdate":    20150508/*最新更新时间*/
    private String name;//通过名字查找漫画的详情
    private String type;
    private String area;
    private String des;
    private String finish;
    private String lastUpdate;

    public ArrayList<ComicInfo> comicList;
    private int total;
    private String url;

    public ComicBean(String name, String type, String area, String des, String finish, String lastUpdate) {
        this.name = name;
        this.type = type;
        this.area = area;
        this.des = des;
        this.finish = finish;
        this.lastUpdate = lastUpdate;
        comicList = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ComicBean{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", area='" + area + '\'' +
                ", des='" + des + '\'' +
                ", finish='" + finish + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}' + "\n";
    }

    public String getName() {
        return name;
    }

    public ArrayList<ComicInfo> getComicList() {
        return comicList;
    }

    public void setComicList(ArrayList<ComicInfo> comicList) {
        this.comicList = comicList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void showComicInfo(@NonNull Context context) {
        Toast.makeText(context, name + comicList.toString(), Toast.LENGTH_LONG).show();
        Log.i("TTT", comicList.toString());
    }


//    protected ComicBean(Parcel in) {
//        name = in.readString();
//        type = in.readString();
//        area = in.readString();
//        des = in.readString();
//        finish = in.readString();
//        lastUpdate = in.readString();
//        total = in.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//        dest.writeString(type);
//        dest.writeString(area);
//        dest.writeString(des);
//        dest.writeString(finish);
//        dest.writeString(lastUpdate);
//        dest.writeInt(total);
//        dest.writeList(comicList);
//    }
//
//    public static final Parcelable.Creator<ComicBean> CREATOR = new Parcelable.Creator<ComicBean>() {
//
//        @Override
//        public ComicBean createFromParcel(Parcel source) {
//            return new ComicBean(source);
//        }
//
//        @Override
//        public ComicBean[] newArray(int size) {
//            return new ComicBean[size];
//        }
//    };
}
