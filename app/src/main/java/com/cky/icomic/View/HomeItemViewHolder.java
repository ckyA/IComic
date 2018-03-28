package com.cky.icomic.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cky.icomic.Bean.ComicBean;
import com.cky.icomic.Bean.DBHelper.ImageCacheHelper;
import com.cky.icomic.R;

/**
 * Created by Admin on 2018/3/19.
 */

public class HomeItemViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTextView;
    ComicBean comicBean;

    public HomeItemViewHolder(View itemView , int width) {
        super(itemView);

        mImageView = itemView.findViewById(R.id.iv_single);
        mImageView.getLayoutParams().height = width *4 / 3;

        mTextView = itemView.findViewById(R.id.name_single);
    }

    public void setComicBean(ComicBean comicBean) {
        this.comicBean = comicBean;
    }

    public void setText() {
        mTextView.setText(comicBean.getName() + "\n" + comicBean.getArea());
    }

    public void setImage(final Context context) {
        String url = ImageCacheHelper.getFirstComicImage(comicBean.getName(), context);
        if (url != null){
            Glide.with(context).load(url).thumbnail(0.3f).into(mImageView);
        }
    }
}
