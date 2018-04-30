package com.sahni.rahul.iitdair.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.sahni.rahul.iitdair.R;
import com.sahni.rahul.iitdair.UI.Model.Indicator;

public class MyScale extends View {


    private int width;
    private int height;
    private float part;
    private static final int PADDING = 20;
    private static final int NO_OF_PARTITION = 5;
    private Paint mScalePaint;
    private Paint mRectPaint;
    private int color[];
    private Paint mHeadingPaint;
    private Bitmap mIndicatorBitmap;
    private int mIndicatorHeight;
    private Indicator mIndicator;


    public MyScale(Context context) {
        super(context);
        init();
    }

    public MyScale(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScale(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerY = height/2;
        for(int i =0; i<NO_OF_PARTITION; i++){
            canvas.drawLine(i*part+50,centerY, (i+1)*part + 50, centerY, getPaint(i));
            canvas.drawText("" + (i * 100), i * part +50, centerY + (2 * PADDING), mHeadingPaint);
        }
        canvas.drawText("500", 5*part+50, centerY + (2 * PADDING), mHeadingPaint);
        canvas.drawBitmap(mIndicatorBitmap, getIndicatorPosition(mIndicator.getValue()), centerY - mIndicatorHeight, mRectPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight()/2;
        part = ((float)(width - 100))/NO_OF_PARTITION;
        setMeasuredDimension(width, height);

    }

    private void init() {
        mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScalePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeWidth(PADDING/2);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);

        mRectPaint.setStrokeWidth(5);

        mHeadingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeadingPaint.setTextSize(spToPixel(getContext(), 11));
        mHeadingPaint.setTextAlign(Paint.Align.CENTER);

        mIndicatorBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_scale_icon);
        mIndicatorHeight = mIndicatorBitmap.getHeight();

        mIndicator = new Indicator(0);

        color = new int[]{R.color.level_1, R.color.level_2, R.color.level_3, R.color.level_4, R.color.level_5};
    }

    private float getIndicatorPosition(float value){
        if(value > 500){
            value = 500;
        } else if(value < 0){
            value = 0;
        }
        float scale = part/101f;
        float position =  scale*value + 50;
        return Math.abs(position - mIndicatorBitmap.getWidth()/2);
    }

    public void updateIndicator(float value){
        mIndicator.setValue(value);
        invalidate();
    }

    private Paint getPaint(int index){
        mScalePaint.setColor(ContextCompat.getColor(getContext(), color[index]));
        return mScalePaint;
    }

    public static float spToPixel(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
}
