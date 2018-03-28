package com.cky.icomic.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cky.icomic.R;

/**
 * Created by Admin on 2018/3/21.
 */

public class ComicInfoViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public ComicInfoViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tv_item_comic_info);
    }
}
