package com.cky.icomic.Bean;

import java.util.ArrayList;

/**
 * Created by Admin on 2018/3/19.
 */

public class ComicList {

    public ArrayList<ComicBean> mList;

    @Override
    public String toString() {
        return mList.toString();
    }

    public ComicList(ArrayList<ComicBean> list) {
        mList = list;
    }
}
