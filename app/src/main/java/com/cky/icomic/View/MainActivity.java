package com.cky.icomic.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cky.icomic.R;
import com.cky.icomic.RequestComic.OnRequestFinishListener;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<android.support.v4.app.Fragment> fragment_list;
    private List<String> fragment_title_list;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout = findViewById(R.id.tab_main);
        mViewPager = findViewById(R.id.vp_fragment);

        initTab();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mTabLayout.getSelectedTabPosition()==0)
            initTab();
        if(mTabLayout.getSelectedTabPosition()==2){
            initTab();
            mViewPager.setCurrentItem(2);
        }
    }

    private void initTab() {

        initList();

        mViewPager.setAdapter(new FragmentPagerAdapter(MainActivity.this.getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragment_list.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragment_title_list.get(position);
            }

            @Override
            public int getCount() {
                return fragment_list.size();
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);

        //自定义Tab
        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.tab_item);//给每一个tab设置view
            if (i == 0) {
                // 设置第一个tab的TextView是被选择的样式
                tab.getCustomView().findViewById(R.id.itv).setSelected(true);//第一个tab被选中
            }
            IconTextView textView = (IconTextView) tab.getCustomView().findViewById(R.id.itv);
            textView.setText(fragment_title_list.get(i));//设置tab上的文字
        }
    }

    private void initList() {
        fragment_list = new ArrayList<>();
        fragment_title_list = new ArrayList<>();
        fragment_title_list.add("{fa-home}\n主页");
        fragment_title_list.add("{fa-th-large}\n分类");
        fragment_title_list.add("{fa-star}\n书架");
        fragment_title_list.add("{fa-user}\n个人");

        HomeFragment homeFragment =new HomeFragment();
        homeFragment.setOnSubscriptionIconClickListener(new OnRequestFinishListener() {
            @Override
            public void onRequestFinish() {
                mViewPager.setCurrentItem(2);
            }
        });
        fragment_list.add(homeFragment);
        fragment_list.add(new ClassifyFragment());
        fragment_list.add(new SubscriptionFragment());
        fragment_list.add(new UserFragment());
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("...")
                .setMessage("确定要退出吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        System.exit(0);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_LONG).show();
                    }
                }).create().show();
    }

    protected void onClick(View view){
        Intent intent = new Intent(this, ClassifyActivity.class);
        switch (view.getId()){
            case R.id.c1:
                intent.putExtra("type","少年漫画");
                break;
            case R.id.c2:
                intent.putExtra("type","青年漫画");
                break;
            case R.id.c3:
                intent.putExtra("type","少女漫画" );
                break;
            case R.id.c4:
                intent.putExtra("type","耽美漫画");
                break;
        }
        startActivity(intent);
    }
}
