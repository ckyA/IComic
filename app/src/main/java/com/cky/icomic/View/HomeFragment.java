package com.cky.icomic.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.ComicList;
import com.cky.icomic.Bean.DBHelper.Subscription;
import com.cky.icomic.R;

import com.cky.icomic.RequestComic.OnRequestFinishListener;
import com.cky.icomic.RequestComic.OnResponseListener;
import com.cky.icomic.RequestComic.RequestComic;
import com.joanzapata.iconify.widget.IconButton;
import com.joanzapata.iconify.widget.IconTextView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/3/19.
 */

public class HomeFragment extends Fragment {

    private Banner banner;
    private SearchView mSearchView;

    private RecyclerView subscriptionRecyclerView;
    private ComicList subscriptionComicList;
    private OnRequestFinishListener onSubscriptionIconClickListener;

    private RecyclerView recommendRecyclerView;
    private ComicList recommendComicList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);
        View subscription = view.findViewById(R.id.my_subscription);
        View recommend = view.findViewById(R.id.recommend);
        banner = view.findViewById(R.id.banner);
        mSearchView = view.findViewById(R.id.sv_home);

        initBanner();
        initSubscription(subscription);
        initRecommend(recommend, 0);
        initSearchView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        subscriptionRecyclerView.invalidate();

    }

    private void initBanner() {
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        })
                .setImages(getBannerImages())
                .start();
    }

    public List<Integer> getBannerImages() {
        List<Integer> res = new ArrayList<>();
        res.add(R.drawable.jiabaili);
        res.add(R.drawable.san_francisco_supreme);
        res.add(R.drawable.jiabaili);
        res.add(R.drawable.san_francisco_supreme);
        return res;
    }

    private void initRecommend(final View recommend, final int skip) {
        recommendRecyclerView = recommend.findViewById(R.id.rv_item);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        recommendRecyclerView.getLayoutParams().height = 7 * (int) (30 * dm.density + dm.widthPixels * 4 / 9);
        //(int) (7 * 190 * dm.density);
        IconTextView textView = recommend.findViewById(R.id.text);
        textView.setText("{fa-hand-rock-o}  推荐");

        RequestComic.Instance.requestRecommendComicList(skip, new OnResponseListener() {
            @Override
            public void onResponse(ComicList resRecommend) {
                recommendComicList = resRecommend;
                HomeFragment.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recommendRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                        MyAdapter adapter = new MyAdapter(recommendComicList, false);
                        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                ComicBean comicBean = recommendComicList.mList.get(position);
                                ComicBean cache = Subscription.getSubscriptionComicBean(getActivity(), comicBean.getName());
                                if (cache != null && cache.getUrl() != null) {
                                    comicBean = cache;
                                }
                                Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
                                intent.putExtra("name", comicBean.getName());
                                intent.putExtra("bean", comicBean);
                                startActivity(intent);
                            }
                        });
                        recommendRecyclerView.setAdapter(adapter);
                    }
                });
            }
        });

        IconButton iconButton = recommend.findViewById(R.id.ib_item);
        iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRecommend(recommend, skip + 20);
            }
        });
    }

    private void initSubscription(View subscription) {
        subscriptionRecyclerView = subscription.findViewById(R.id.rv_item);
        TextView textView = subscription.findViewById(R.id.text);
        textView.setText("{fa-hand-spock-o}  我的订阅");

        subscriptionRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        subscriptionComicList = Subscription.getSubscriptionComicBeans(getActivity());
        final MyAdapter adapter2 = new MyAdapter(subscriptionComicList, true);
        adapter2.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ComicBean comicBean = subscriptionComicList.mList.get(position);
                ComicBean cache = Subscription.getSubscriptionComicBean(getActivity(), comicBean.getName());
                if (cache != null && cache.getUrl() != null) {
                    comicBean = cache;
                }
                Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
                intent.putExtra("bean", comicBean);
                startActivity(intent);
            }
        });
        subscriptionRecyclerView.setAdapter(adapter2);

        IconButton iconButton = subscription.findViewById(R.id.ib_item);
        iconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubscriptionIconClickListener != null)
                    onSubscriptionIconClickListener.onRequestFinish();
            }
        });
    }

    public void setOnSubscriptionIconClickListener(OnRequestFinishListener onClickListener) {
        //借用OnResponseListener，懒得再写
        onSubscriptionIconClickListener = onClickListener;
    }

    private class MyAdapter extends RecyclerView.Adapter<HomeItemViewHolder> {

        private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
        private ComicList comicList;
        private boolean isSubscription;

        public MyAdapter(ComicList comicList, boolean isSubscription) {
            super();
            if (comicList == null) {
                this.comicList = new ComicList(new ArrayList<ComicBean>());
            } else {
                this.comicList = comicList;
            }
            this.isSubscription = isSubscription;
        }

        @Override
        public HomeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_home_single_comic, parent, false);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            int widthPixels = dm.widthPixels;
            return new HomeItemViewHolder(view, widthPixels / 3);
        }

        @Override
        public void onBindViewHolder(HomeItemViewHolder holder, final int position) {
            ComicBean comicBean = comicList.mList.get(position);
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
            if (isSubscription)//只显示三个已订阅
                return Math.min(comicList.mList.size(), 3);
            return comicList.mList.size();
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener) {
            this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
        }
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RequestComic.Instance.requestSearchComic(query, new OnResponseListener() {
                    @Override
                    public void onResponse(ComicList list) {
                        Intent intent = new Intent(getActivity(), SearchResponseActivity.class);
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

    public void runOnUiThread(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }
}
