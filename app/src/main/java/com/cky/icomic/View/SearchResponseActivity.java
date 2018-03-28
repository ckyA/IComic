package com.cky.icomic.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.ComicList;
import com.cky.icomic.Bean.DBHelper.Subscription;
import com.cky.icomic.R;
import com.cky.icomic.RequestComic.OnResponseListener;
import com.cky.icomic.RequestComic.RequestComic;
import com.cky.icomic.Util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 2018/3/26.
 */

public class SearchResponseActivity extends AppCompatActivity {

    private ArrayList<ComicBean> mComicList;
    private String name;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSearchResult();
        initSearchView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_search);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        MyAdapter adapter = new MyAdapter(mComicList);
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ComicBean comicBean = mComicList.get(position);
                ComicBean cache = Subscription.getSubscriptionComicBean(SearchResponseActivity.this, comicBean.getName());
                if (cache != null  && cache.getUrl()!=null) {
                    comicBean = cache;
                }
                Intent intent = new Intent(SearchResponseActivity.this, ComicInfoActivity.class);
                intent.putExtra("name", comicBean.getName());
                intent.putExtra("bean", comicBean);
                startActivity(intent);
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    private void initSearchView() {
        mSearchView = findViewById(R.id.sv_search);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RequestComic.Instance.requestSearchComic(query, new OnResponseListener() {
                    @Override
                    public void onResponse(ComicList list) {
                        Intent intent = new Intent(SearchResponseActivity.this, SearchResponseActivity.class);
                        intent.putExtra("name", mSearchView.getQuery());
                        intent.putExtra("list", list.mList);
                        startActivity(intent);
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void getSearchResult() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mComicList = (ArrayList<ComicBean>) intent.getSerializableExtra("list");
        if (mComicList != null) {
            Log.i(mComicList.toString());
        } else {
            Log.i(null);
        }
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
            View view = LayoutInflater.from(SearchResponseActivity.this)
                    .inflate(R.layout.item_home_single_comic, parent, false);
            DisplayMetrics dm = new DisplayMetrics();
            SearchResponseActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
            int widthPixels = dm.widthPixels;
            return new HomeItemViewHolder(view,widthPixels/3);
        }

        @Override
        public void onBindViewHolder(HomeItemViewHolder holder, final int position) {
            ComicBean comicBean = comicList.get(position);
            holder.setComicBean(comicBean);
            holder.setText();
            holder.setImage(SearchResponseActivity.this);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy");
    }
}
