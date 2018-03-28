package com.cky.icomic.View;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cky.icomic.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        compatScreen(view, R.id.c1, dm);
        compatScreen(view, R.id.c2, dm);
        compatScreen(view, R.id.c3, dm);
        compatScreen(view, R.id.c4, dm);
        return view;
    }

    private void compatScreen(View view, @IdRes int id, DisplayMetrics dm) {
        CardView cardView = view.findViewById(id);
        cardView.getLayoutParams().height = (dm.widthPixels - 60) / 2;
        cardView.getLayoutParams().width = (dm.widthPixels - 60) / 2;
    }

}
