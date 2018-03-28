package com.cky.icomic.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cky.icomic.R;
import com.cky.icomic.RequestComic.OnImageURLResponseListener;
import com.cky.icomic.RequestComic.RequestComic;
import com.cky.icomic.Util.Log;
import com.cky.icomic.widget.EventView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Admin on 2018/3/25.
 */

public class ComicShowActivity extends AppCompatActivity {

    private ArrayList<String> imgURLs;
    private ListView comic;
    private String name;
    private String comicID;
    private EventView event;

    private LinearLayout menu;
    private SeekBar seekBar;
    private TextView textView;

    private int minHeight = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_show);
        init();
    }

    private void init() {
        comic = findViewById(R.id.lv_comic_show);
        //recyclerView = findViewById(R.id.rv_show);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        comicID = intent.getStringExtra("id");

        if (name != null && comicID != null) {
            RequestComic.Instance.requestImageURL(comicID, name, new OnImageURLResponseListener() {
                @Override
                public void onResponse(ArrayList<String> mList) {
                    imgURLs = mList;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initListView();
                            //initRecyclerView();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "NOTHING", Toast.LENGTH_SHORT).show();
        }
    }

    private void initListView() {
        if (imgURLs != null) {
            comic.setAdapter(new MyAdapter());
        }

        initMenu();

        comic.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                seekBar.setProgress(firstVisibleItem);
                textView.setText(firstVisibleItem + "/" + imgURLs.size());
            }
        });
    }

    private void initMenu() {
        event = findViewById(R.id.event);
        menu = findViewById(R.id.ll_a_c_s);
        event.setITouchCallBack(new EventView.ITouchCallBack() {
            @Override
            public void click() {
                if (menu.getVisibility() == View.GONE) {
                    menu.setVisibility(View.VISIBLE);
                } else if (menu.getVisibility() == View.VISIBLE) {
                    menu.setVisibility(View.GONE);
                }
            }
        });

        seekBar = findViewById(R.id.sk_progress);
        textView = findViewById(R.id.tv_show);
        textView.setText(1 + "/" + imgURLs.size());
        seekBar.setMax(imgURLs.size());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText((progress) + "/" + imgURLs.size());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int p = seekBar.getProgress();
                comic.setSelection(p);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgURLs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_comic_show, null);

            if (minHeight != 0) {
                view.setMinimumHeight(minHeight);
            }

            PhotoView photoView = view.findViewById(R.id.pv);
            TextView tv = view.findViewById(R.id.tv_show);
            tv.setText((position + 1) + "/" + imgURLs.size());

            Glide.with(ComicShowActivity.this)
                    .load(imgURLs.get(position))
                    .into(photoView);

            if ((position > 0) && (minHeight == 0)) {
                view.measure(0, 0);
                minHeight = view.getMeasuredHeight();
            }
            Log.i(minHeight + " /");

            return view;
        }
    }

}
