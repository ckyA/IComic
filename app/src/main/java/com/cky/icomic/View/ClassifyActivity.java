package com.cky.icomic.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.ComicList;
import com.cky.icomic.Bean.DBHelper.Subscription;
import com.cky.icomic.R;
import com.cky.icomic.RequestComic.OnResponseListener;
import com.cky.icomic.RequestComic.RequestComic;

import java.util.ArrayList;

public class ClassifyActivity extends Activity {

    String comicType;
    RecyclerView mRecyclerView;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        Intent intent = getIntent();
        comicType = intent.getStringExtra("type");
        if (comicType != null)
            init();
    }

    private void init() {
        mTextView = findViewById(R.id.tv_classify);
        mTextView.setText(comicType);

        RequestComic.Instance.requestTypeOfComic(comicType, new OnResponseListener() {
            @Override
            public void onResponse(final ComicList res) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView(res);
                    }
                });
            }
        });
    }

    private void initRecyclerView(final ComicList res) {
        mRecyclerView = findViewById(R.id.rv_classify);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        MyAdapter adapter = new MyAdapter(res.mList);
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ComicBean comicBean = res.mList.get(position);
                ComicBean cache = Subscription.getSubscriptionComicBean(ClassifyActivity.this, comicBean.getName());
                if (cache != null && cache.getUrl() != null) {
                    comicBean = cache;
                }
                Intent intent = new Intent(ClassifyActivity.this, ComicInfoActivity.class);
                intent.putExtra("name", comicBean.getName());
                intent.putExtra("bean", comicBean);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<HomeItemViewHolder> {

        private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
        private ArrayList<ComicBean> comicList;

        public MyAdapter(ArrayList<ComicBean> comicList) {
            super();
            if (comicList == null) {
                this.comicList = new ArrayList<ComicBean>();
            } else {
                this.comicList = comicList;
            }
        }

        @Override
        public HomeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ClassifyActivity.this)
                    .inflate(R.layout.item_home_single_comic, parent, false);
            DisplayMetrics dm = new DisplayMetrics();
            ClassifyActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int widthPixels = dm.widthPixels;
            return new HomeItemViewHolder(view,widthPixels/3);
        }

        @Override
        public void onBindViewHolder(HomeItemViewHolder holder, final int position) {
            ComicBean comicBean = comicList.get(position);
            holder.setComicBean(comicBean);
            holder.setText();
            holder.setImage(ClassifyActivity.this);
            if (mOnRecyclerViewItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnRecyclerViewItemClickListener.onItemClick(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return comicList.size();
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener) {
            this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
        }
    }
}
