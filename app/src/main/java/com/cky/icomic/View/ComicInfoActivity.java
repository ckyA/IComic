package com.cky.icomic.View;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.DBHelper.ImageCacheHelper;
import com.cky.icomic.Bean.DBHelper.Subscription;
import com.cky.icomic.R;
import com.cky.icomic.RequestComic.OnComicInfoResponseListener;
import com.cky.icomic.RequestComic.OnRequestFinishListener;
import com.cky.icomic.RequestComic.RequestComic;

/**
 * Created by Admin on 2018/3/21.
 */

public class ComicInfoActivity extends AppCompatActivity {

    private ComicBean comicBean;
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private ImageView mImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_info);
        comicBean = (ComicBean) getIntent().getSerializableExtra("bean");

        initFAB();
        initComicInfo();
        requestData();
    }

    private void initComicInfo() {
        TextView textView = findViewById(R.id.tv_info);
        mImageView = findViewById(R.id.iv_info);
        textView.setText("漫画名称:  " + comicBean.getName() + "\n\n" +
                "国家:  " + comicBean.getArea() + "\n\n" +
                "是否更新完:  " + comicBean.getFinish() + "\n\n" +
                "最新更新时间:  " + comicBean.getLastUpdate());

        TextView des = findViewById(R.id.tv_des);
        des.setText("简介\n" + comicBean.getDes());
    }

    private void initFAB() {
        mFloatingActionButton = findViewById(R.id.fab);
        if (Subscription.isSubscription(comicBean.getName(), this))
            mFloatingActionButton.setSelected(true);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    Subscription.removeSubscribe(comicBean.getName(), ComicInfoActivity.this);
                    Toast.makeText(ComicInfoActivity.this, "取消订阅", Toast.LENGTH_LONG).show();
                    v.setSelected(false);
                } else {
                    Subscription.subscribeComic(comicBean, ComicInfoActivity.this);
                    Toast.makeText(ComicInfoActivity.this, "成功订阅", Toast.LENGTH_LONG).show();
                    v.setSelected(true);
                }

            }
        });
    }

    private void requestData() {
        if (comicBean.comicList.size() > 0) {
            initRecyclerView();
            setImg();
            return;
        }
        RequestComic.Instance.requestComicInfo(comicBean, 0,
                new OnComicInfoResponseListener() {
                    @Override
                    public void OnComicInfoResponse(final ComicBean comicBean) {
                        ComicInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initRecyclerView();
                                setImg();
                            }
                        });
                    }
                });
    }

    private void setImg() {
        if (comicBean.getUrl() == null) {
            ImageCacheHelper.requestFirstComicImage(comicBean, this, new OnRequestFinishListener() {
                @Override
                public void onRequestFinish() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Subscription.updateComicBean(comicBean, ComicInfoActivity.this);
                            Glide.with(ComicInfoActivity.this).load(comicBean.getUrl()).thumbnail(0.3f).into(mImageView);
                        }
                    });
                }
            });
        } else {
            Glide.with(ComicInfoActivity.this).load(comicBean.getUrl()).thumbnail(0.3f).into(mImageView);
        }
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_comic_info);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyAdapter adapter = new MyAdapter();
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ComicInfoActivity.this, ComicShowActivity.class);
                intent.putExtra("name", comicBean.getName());
                intent.putExtra("id", comicBean.comicList.get(position).getID());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<ComicInfoViewHolder> {

        private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

        @Override
        public ComicInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ComicInfoActivity.this)
                    .inflate(R.layout.item_comic_info, parent, false);
            return new ComicInfoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ComicInfoViewHolder holder, final int position) {
            holder.textView.setText(comicBean.comicList.get(position).getName());
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
            return comicBean.comicList.size();
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener) {
            this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
        }
    }
}
