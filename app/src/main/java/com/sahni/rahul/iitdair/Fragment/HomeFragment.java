package com.sahni.rahul.iitdair.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.anastr.speedviewlib.Gauge;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sahni.rahul.iitdair.Activity.MainActivity;
import com.sahni.rahul.iitdair.ErrorListener;
import com.sahni.rahul.iitdair.Graph.DayAxisValueFormatter;
import com.sahni.rahul.iitdair.Helper.ContentUtils;
import com.sahni.rahul.iitdair.HourAxisValueFormatter;
import com.sahni.rahul.iitdair.Model.Device;
import com.sahni.rahul.iitdair.Model.Result;
import com.sahni.rahul.iitdair.MyMarkerView;
import com.sahni.rahul.iitdair.Networking.ApiInterface;
import com.sahni.rahul.iitdair.Networking.DataResponse;
import com.sahni.rahul.iitdair.Networking.NetworkUtils;
import com.sahni.rahul.iitdair.Networking.RetrofitClient;
import com.sahni.rahul.iitdair.Networking.UbiDotsResponse;
import com.sahni.rahul.iitdair.R;
import com.sahni.rahul.iitdair.TimerMarkerView;
import com.sahni.rahul.iitdair.UI.MyGauge;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
//    private GraphView graphView;

    private LineGraphSeries<DataPoint> series;
    private ProgressBar mProgressBar;
    private TextView mLastUpdatedTextView;
    private TextView mNameTextView;
    private MyGauge mGauge;
    private LineChart mLineChart;
    private CardView mGaugeCardView, mGraphCardView;
    private LineDataSet mDataSet;
    private LineData mLineData;
    private ErrorListener mErrorListener;
    private long referenceTimestamp;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        final SpeedView speedometer = view.findViewById(R.id.speedView);
        mGaugeCardView = view.findViewById(R.id.cardView);
        mGraphCardView = view.findViewById(R.id.cardView2);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mNameTextView = view.findViewById(R.id.device_name_text_view);
        mLastUpdatedTextView = view.findViewById(R.id.last_update_text_view);
        mGauge = view.findViewById(R.id.my_gauge);
        mLineChart = view.findViewById(R.id.chart);

        TimerMarkerView myMarkerView = new TimerMarkerView(getActivity(), R.layout.custom_marker_view, mLineChart);
        myMarkerView.setOffset(mLineChart.getMeasuredWidth(), mLineChart.getMeasuredHeight());
        final ArrayList<Entry> entryList = new ArrayList<>();
        mDataSet = new LineDataSet(entryList, "Air Quality Index"); // add entries to dataset
        mLineData = new LineData(mDataSet);
        XAxis xAxis = mLineChart.getXAxis();
//        xAxis.setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        mLineChart.setDrawMarkers(true);
        mLineChart.setMarker(myMarkerView);
//        xAxis.setValueFormatter(new DayAxisValueFormatter(referenceTimestamp));
        mLineChart.setData(mLineData);
        fetchData();

    }

    public void fetchData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mGaugeCardView.setVisibility(View.INVISIBLE);
        mGraphCardView.setVisibility(View.INVISIBLE);
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient();
        apiInterface.getUbiDotsResponse(NetworkUtils.TOKEN, 1)
                .enqueue(new Callback<UbiDotsResponse>() {
                    @Override
                    public void onResponse(Call<UbiDotsResponse> call, Response<UbiDotsResponse> response) {

                        if (response.isSuccessful()) {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mGaugeCardView.setVisibility(View.VISIBLE);
                            mGraphCardView.setVisibility(View.VISIBLE);
                            UbiDotsResponse ubiDotsResponse = response.body();
                            ArrayList<Result> resultArrayList = ubiDotsResponse.getResultList();
                            mNameTextView.setText("Air Quality Index");
                            Collections.reverse(resultArrayList);
                            Result latestResult = resultArrayList.get(resultArrayList.size() - 1);
                            referenceTimestamp = resultArrayList.get(0).getCreatedAt();
                            mLastUpdatedTextView.append(ContentUtils.epochToLastUpdated(latestResult.getTimestamp()));
                            Log.d(TAG, "Value =" + latestResult.getValue());
                            mGauge.updateIndicator(latestResult.getValue());

                            int i = 1;
//                                float
//                                ArrayList<DataPoint> dataPoints = new ArrayList<>();
//                            Log.d("Rahul", "Size =" + resultArrayList.size());
                            for (Result result : resultArrayList) {
//                                Log.d("Rahul", "temp =" + result.getValue());
//                                Log.d("Rahul", "temp =" + result.getCreatedAt());
//
                                mDataSet.addEntry(new Entry(i*10,
                                        result.getValue(), result.getCreatedAt()));
                                i++;
                            }
                            Log.d("Rahul", "I =" + i);
                            mLineData.notifyDataChanged();
                            mLineChart.notifyDataSetChanged();
                            mLineChart.invalidate();


                        } else {
                            Log.d(TAG, "Response unsuccessfull =" + response.errorBody());
                            handleError("Some error occured!");
                        }
                    }

                    @Override
                    public void onFailure(Call<UbiDotsResponse> call, Throwable t) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Error =" + t.getMessage());
                        handleError("Please check your internet connection");
                    }
                });
    }

    private void handleError(String message) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mErrorListener.onError();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finishAffinity();
                    }
                })
                .create();
        dialog.show();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ErrorListener) {
            mErrorListener = (ErrorListener) context;
        }
    }
}
