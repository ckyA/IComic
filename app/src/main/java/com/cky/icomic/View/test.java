package com.cky.icomic.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cky.icomic.Bean.ComicList;
import com.cky.icomic.R;

/**
 * Created by Admin on 2018/3/19.
 */

public class test extends Fragment {

    TextView tv;
    ComicList res;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, null);
        tv= v.findViewById(R.id.tv);
//        RequestComic requestComic = new RequestComic();
//        requestComic.requestRecommendComicList(new OnResponseListener() {
//            @Override
//            public void onResponse(ComicList resRecommend) {
//                res=resRecommend;
//                //handler.sendEmptyMessage(1);
//                test.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tv.setText(res.toString());
//                    }
//                });
//            }
//        });

        Log.i("tag","finish");
        return v;
    }


}
