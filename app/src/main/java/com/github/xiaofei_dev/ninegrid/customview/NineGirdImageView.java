package com.github.xiaofei_dev.ninegrid.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by xiaofei on 2017/7/17.
 */

public class NineGirdImageView extends AppCompatImageView {

    private final Paint mPaint = new Paint();
    private final Path mPath=new Path();

    public NineGirdImageView(@NonNull Context context) {
        this(context,null);
    }

    public NineGirdImageView(@NonNull Context context, @Nullable  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGirdImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setAntiAlias(true);// 抗锯尺
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.moveTo(0,h/3);
        mPath.lineTo(w,h/3);
        mPath.moveTo(0,h/3*2);
        mPath.lineTo(w,h/3*2);
        mPath.moveTo(w/3,0);
        mPath.lineTo(w/3,h);
        mPath.moveTo(w/3*2,0);
        mPath.lineTo(w/3*2,h);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    public void setPaintWidth(int width){
        mPaint.setStrokeWidth(width);
        invalidate();
    }
}
