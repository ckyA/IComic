package com.cky.icomic.Bean;

import java.io.Serializable;

/**
 * Created by Admin on 2018/3/20.
 */

public class ComicInfo implements Serializable{

    private static final long serialVersionUID = 910348371207595185L;
    //            "name": "第01卷",/*章节名称*/
//            "id": 139833/*章节id*/
    private String name;
    private String ID;

    public ComicInfo(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "ComicInfo{" +
                "name='" + name + '\'' +
                ", ID='" + ID + '\'' +
                '}';
    }
}
