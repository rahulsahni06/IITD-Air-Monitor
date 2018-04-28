package com.sahni.rahul.iitdair;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerMarkerView extends MarkerView {
    private final TextView tvContent;
    private final SimpleDateFormat mDataFormat;
    private int width;
    private int height;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public TimerMarkerView(Context context, int layoutResource, LineChart lineChart) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.mDataFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        width = lineChart.getMeasuredWidth();
        height = lineChart.getMeasuredHeight();
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        long timeStamp = (long) e.getData();
        tvContent.setText(mDataFormat.format(new Date(timeStamp)));
//        highlight.
    }
//
//    @Override
//    public void setOffset(float offsetX, float offsetY) {
//        super.setOffset(width/2, height/2);
//    }

//    @Override
//    public void setOffset(MPPointF offset) {
//        super.setOffset(offset);
//    }

}
