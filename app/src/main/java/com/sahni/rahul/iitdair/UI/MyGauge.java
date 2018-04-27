package com.sahni.rahul.iitdair.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.sahni.rahul.iitdair.UI.Model.InternalHeading;
import com.sahni.rahul.iitdair.UI.Model.Indicator;
import com.sahni.rahul.iitdair.R;

import java.util.ArrayList;

public class MyGauge extends View {

    private static final int STROKE_WIDTH = 80;
    private RectF mRect;
    private int width;
    private int height;
    private float centerX;
    private float centerY;
    private float radius;
    private Paint mRectPaint;
    private Paint mArcPaint;
    private Paint mHeadingPaint;
    private Paint mIndicatorPaint;
    private ArrayList<InternalHeading> headingList;
    private float startX;
    private float startY;
    private Indicator indicator;

    public MyGauge(Context context) {
        super(context);
        Log.d("Rahul", "MyArcView(Context context)");
        init();
    }

    public MyGauge(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("Rahul", "MyArcView(Context context, @Nullable AttributeSet attrs)");
        init();
    }

    public MyGauge(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("Rahul", "MyArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)");
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("Rahul", "onDraw()");
        super.onDraw(canvas);
        if (mRect == null)
        {
            // take the minimum of width and height here to be on he safe side:
//            centerX = getMeasuredWidth()/ 2;
//            centerY = getMeasuredHeight()/ 2;
//            radius = Math.min(centerX,centerY);

            // mRect will define the drawing space for drawArc()
            // We have to take into account the STROKE_WIDTH with drawArc() as well as drawCircle():
            // circles as well as arcs are drawn 50% outside of the bounds defined by the radius (radius for arcs is calculated from the rectangle mRect).
            // So if mRect is too large, the lines will not fit into the View
            int startTop = STROKE_WIDTH/2;
            int startLeft = startTop;

            int endBottom = (int) (2 * radius - startTop);
            int endRight = endBottom;

            mRect = new RectF(startLeft, startTop, endRight, endBottom);
            centerX = mRect.centerX();
            centerY = mRect.centerY();
            radius = Math.min(centerX,centerY);
        }

        // just to show the rectangle bounds:
//        canvas.drawRect(mRect, mRectPaint);
        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.level_1));
        canvas.drawArc(mRect, 135, 54, false, mArcPaint);

        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.level_2));
        canvas.drawArc(mRect, 189, 54, false, mArcPaint);

        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.level_3));
        canvas.drawArc(mRect, 243, 54, false, mArcPaint);

        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.level_4));
        canvas.drawArc(mRect, 297, 54, false, mArcPaint);

        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.level_5));
        canvas.drawArc(mRect, 351, 54, false, mArcPaint);

        headingList = new ArrayList<>();
        headingList.add(new InternalHeading("0", calculateX(135), calculateY(135)));
        headingList.add(new InternalHeading("100", calculateX(189), calculateY(189)));
        headingList.add(new InternalHeading("200", calculateX(243), calculateY(243)));
//        headingList.add(new InternalHeading("300", calculateX(297)-25, calculateY(297)+25));
        headingList.add(new InternalHeading("300", calculateX(297), calculateY(297)));
//        headingList.add(new InternalHeading("400", calculateX(351)-30, calculateY(351)+30));
        headingList.add(new InternalHeading("400", calculateX(351), calculateY(351)));
//        headingList.add(new InternalHeading("500", calculateX(405)-10, calculateY(405)-10));
        headingList.add(new InternalHeading("500", calculateX(405), calculateY(405)));

        for(InternalHeading heading : headingList){
            canvas.drawText(heading.getHeading(), heading.getCoordX(), heading.getCoordY(), mHeadingPaint);
        }

//        canvas.drawLine(mRect.centerX(), mRect.centerY(), calculateX(208), calculateY(208), mRectPaint);
        Log.d("Rahul", "indicator ="+indicator.valueToAngle());
        canvas.drawLine(mRect.centerX(), mRect.centerY(), calculateX(indicator.valueToAngle(), mRect.width()/2),
                calculateY(indicator.valueToAngle(),mRect.width()/2), mRectPaint);
        canvas.drawCircle(mRect.centerX(), mRect.centerY(), 30f, mRectPaint);
        canvas.drawText(indicator.getValue()+" AQI", mRect.centerX(), mRect.bottom, mIndicatorPaint);

    }

    private void init(){
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setStrokeWidth(5);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(STROKE_WIDTH);
        mArcPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        mHeadingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHeadingPaint.setTextSize(spToPixel(getContext(), 11));
        mHeadingPaint.setTextAlign(Paint.Align.CENTER);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setTextSize(spToPixel(getContext(), 16));
        mIndicatorPaint.setTextAlign(Paint.Align.CENTER);
//        mHeadingPaint.
        indicator = new Indicator(0);
//        mHeadingPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth() / 3;
        height = getMeasuredWidth() / 3;
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        radius = Math.min(centerX, centerY);
        setMeasuredDimension((int) (2*radius), (int) (2*radius));
    }

    public void updateIndicator(float value){
        indicator.setValue(value);
        invalidate();
    }

    public double getRadianAngle(double angle){
        return angle * (Math.PI / 180);
    }

    public int calculateX(double angle){
        return (int) (mRect.centerX() + (((mRect.width()/2) - STROKE_WIDTH) * Math.cos(getRadianAngle(angle))));
    }

    public int calculateY(double angle){
        return (int) (mRect.centerY() + (((mRect.width()/2) - STROKE_WIDTH)  * Math.sin(getRadianAngle(angle))));
    }

    public int calculateX(double angle, float radius){
        return (int) (mRect.centerX() + ((radius - STROKE_WIDTH) * Math.cos(getRadianAngle(angle))));
    }

    public int calculateY(double angle, float radius){
        return (int) (mRect.centerY() + ((radius) - STROKE_WIDTH)  * Math.sin(getRadianAngle(angle)));
    }

    public static float spToPixel(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
}
