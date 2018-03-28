package com.cky.icomic.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.cky.icomic.Util.Log;

/**
 * Created by Admin on 2018/3/27.
 */

public class EventView extends View {

    private int touchSlop = 3;
    private int fx;
    private int fy;
    //private boolean flag = false;

    public EventView(Context context) {
        super(context);
    }

    public EventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                fx = (int) event.getX();
                fy = (int) event.getY();
                Log.i("Click");
                if (mIMiddleCallBack != null)
                    mIMiddleCallBack.click();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getX() - fx) > touchSlop)
                    fx = -1;
                if (Math.abs(event.getY() - fy) > touchSlop)
                    fx = -1;
                if (fx != -1) {

                }
                break;
        }
        return false;
    }

    public void setITouchCallBack(ITouchCallBack IMiddleCallBack) {
        mIMiddleCallBack = IMiddleCallBack;
    }

    private ITouchCallBack mIMiddleCallBack;

    public interface ITouchCallBack {
        void click();
    }

}
