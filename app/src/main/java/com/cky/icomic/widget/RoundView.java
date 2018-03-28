package com.cky.icomic.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cky.icomic.R;


/**
 * Created by 煎鱼 on 2017/11/11.
 */

public class RoundView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "RoundView";

    private int D_Width;//圆的直径，D_Width = Math.min(width,heigh);

    private int src; //通过自定义属性传进来的bitmap的资源码
    private  Bitmap b;

    public RoundView(Context context) {
        super(context);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //获取自定义属性。
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundView);
        src = ta.getResourceId( R.styleable.RoundView_src,-1);

        if( src== -1 ){
            b = BitmapFactory.decodeResource(getResources(), R.drawable.jiabaili);//默认图
        }else{
            b = BitmapFactory.decodeResource(getResources(), src);
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heigh = MeasureSpec.getSize(heightMeasureSpec);

        D_Width = Math.min(width,heigh);


        alterBitmap();

        setMeasuredDimension(width, heigh);
    }

    private void alterBitmap() {
        //先缩小再裁剪：
        b = scaleBitmap( b , (float) D_Width / Math.min(b.getWidth(),b.getHeight()) );
        b = cropBitmap(b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //以bitmap为填充内容画一个圆
        mPaint.setShader(new BitmapShader(b, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
        canvas.drawCircle(D_Width / 2, D_Width / 2, D_Width / 2, mPaint);

        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xffdddddd);
        canvas.drawCircle(D_Width / 2, D_Width / 2, D_Width / 2, mPaint);
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 裁剪成正方形
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    private Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int cropWidth = w;
        int cropHeight = h;

        if(w>=h){
            cropWidth = h;
            return Bitmap.createBitmap(bitmap, (w-h)/2, 0, cropWidth, cropHeight , null, false);
        }else{
            cropHeight = w;
            return Bitmap.createBitmap(bitmap, 0 , (h-w)/2  , cropWidth, cropHeight , null, false);
        }
    }

}
