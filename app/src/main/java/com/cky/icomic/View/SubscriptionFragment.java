package com.cky.icomic.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.ComicList;
import com.cky.icomic.Bean.DBHelper.Subscription;
import com.cky.icomic.R;

import java.util.ArrayList;

/**
 * Created by Admin on 2018/3/26.
 */

public class SubscriptionFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<ComicBean> mComicList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, null);
        mRecyclerView = view.findViewById(R.id.rv_subscription);

        initData();
        initRecyclerView();

        return view;
    }

    private void initData() {
        ComicList comicList = Subscription.getSubscriptionComicBeans(getActivity());
        mComicList = comicList.mList;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        MyAdapter adapter = new MyAdapter(mComicList);
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ComicBean comicBean = mComicList.get(position);
                ComicBean cache = Subscription.getSubscriptionComicBean(getActivity(), comicBean.getName());
                if (cache != null  && cache.getUrl()!=null) {
                    comicBean = cache;
                }
                Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
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
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_home_single_comic, parent, false);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int widthPixels = dm.widthPixels;
            return new HomeItemViewHolder(view,widthPixels/3);
        }

        @Override
        public void onBindViewHolder(HomeItemViewHolder holder, final int position) {
            ComicBean comicBean = comicList.get(position);
            holder.setComicBean(comicBean);
            holder.setText();
            holder.setImage(getActivity());
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
