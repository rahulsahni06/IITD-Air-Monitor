package com.sahni.rahul.iitdair.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sahni.rahul.iitdair.ErrorListener;
import com.sahni.rahul.iitdair.Helper.ContentUtils;
import com.sahni.rahul.iitdair.Model.DataSource;
import com.sahni.rahul.iitdair.Model.Result;
import com.sahni.rahul.iitdair.Model.Variable;
import com.sahni.rahul.iitdair.Networking.NetworkUtils;
import com.sahni.rahul.iitdair.Networking.RetrofitClient;
import com.sahni.rahul.iitdair.Networking.VariableDataResponse;
import com.sahni.rahul.iitdair.Networking.VariableListResponse;
import com.sahni.rahul.iitdair.R;
import com.sahni.rahul.iitdair.UI.MyScale;
import com.sahni.rahul.iitdair.UI.MySpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private LineGraphSeries<DataPoint> series;
    private ProgressBar mProgressBar;
    private LineChart mLineChart;
    private CardView mGaugeCardView, mGraphCardView;
    private LineDataSet mDataSet;
    private LineData mLineData;
    private ErrorListener mErrorListener;
    private ArrayList<Variable> mVariableArrayList;
    private ArrayList<DataSource> mDataSourceArrayList;
    private HashMap<DataSource, ArrayList<Variable>> sourceWithVariableMap;
    private MySpinner mSourceSpinner;
    private boolean isFetchingData = false;
    private ProgressBar mGraphProgressBar;
    private Toast toast;
    private boolean isMenuItemClicked = false;
    private static final int NO_SELECTION = -1;
    private int mLastSelectedSpinnerId = NO_SELECTION;
    private boolean isFirstTime = true;
    private MyScale myScale;
    private TextView mAqiTextView;
    private TextView mAqiStatusTextView;
    private Variable mCurrentVariable;
    private Handler mTaskHandler;
    private static final int REPEAT_INTERVAL = 10000;
    private TabLayout mTabLayout;

    private Runnable mRepetitiveTaskRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isFetchingData){
                Log.d(TAG, "Runnable: repeate: "+ mCurrentVariable.getName());

                getVariableData(mCurrentVariable.getId(), mTabLayout.getSelectedTabPosition());
                mTaskHandler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };




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
        Log.d(TAG, "onViewCreated");
        mSourceSpinner = view.findViewById(R.id.source_spinner);
        mVariableArrayList = new ArrayList<>();
        mDataSourceArrayList = new ArrayList<>();
        sourceWithVariableMap = new HashMap<>();

        myScale = view.findViewById(R.id.my_scale);
        mAqiTextView = view.findViewById(R.id.aqi_text_view);
        mAqiStatusTextView = view.findViewById(R.id.aqi_status_text_view);
        mGaugeCardView = view.findViewById(R.id.cardView);
        mGraphCardView = view.findViewById(R.id.cardView2);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mLineChart = view.findViewById(R.id.chart);
        mGraphProgressBar = view.findViewById(R.id.graph_progress_bar);
        mGraphProgressBar.setVisibility(View.INVISIBLE);
        mLineChart.setNoDataText("Data not available");
        ArrayList<Entry> entryList = new ArrayList<>();
        mDataSet = new LineDataSet(entryList, "Air Quality Index"); // add entries to dataset
        mLineData = new LineData(mDataSet);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        if (!entryList.isEmpty()) {
            mLineChart.setData(mLineData);
        }

        ArrayAdapter sourceAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mDataSourceArrayList);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSourceSpinner.setAdapter(sourceAdapter);
        mSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(TAG, "onItemSelected(): Item selected");
                isMenuItemClicked = false;
                if(mLastSelectedSpinnerId == NO_SELECTION){
                    mLastSelectedSpinnerId = position;
                }
                DataSource source = mDataSourceArrayList.get(position);
                mVariableArrayList.clear();
                mVariableArrayList.addAll(sourceWithVariableMap.get(source));

                if(isFirstTime){
                    mCurrentVariable = mVariableArrayList.get(0);
//                    getVariableData(mCurrentVariable.getId());
                    isFirstTime = false;
                    mTaskHandler.post(mRepetitiveTaskRunnable);
                } else {
                    PopupMenu popupMenu = new PopupMenu(getActivity(), mSourceSpinner);

                    for (Variable variable : mVariableArrayList) {
                        popupMenu.getMenu().add(0, mVariableArrayList.indexOf(variable), Menu.NONE, variable.getName());
                    }
                    popupMenu.show();


                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            isMenuItemClicked = true;
                            mLastSelectedSpinnerId = position;
                            Variable variable = mVariableArrayList.get(id);
                            mCurrentVariable = variable;
//                            getVariableData(variable.getId());
                            startRealtimeUpdates();
                            return true;
                        }
                    });

                    popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            if (!isMenuItemClicked) {
                                Log.d(TAG + "rahul", "Menu dismissed");
                                mSourceSpinner.setSelection(position, true);
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mTabLayout = view.findViewById(R.id.tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected(): Position ="+tab.getPosition());
                startRealtimeUpdates();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected(): Position ="+tab.getPosition());
            }
        });
        if(!mTabLayout.getTabAt(0).isSelected()){
            mTabLayout.getTabAt(0).select();
        }

        mTaskHandler = new Handler(getContext().getMainLooper());
        getDataSource();
    }


    public void getDataSource() {
        mProgressBar.setVisibility(View.VISIBLE);
        mGaugeCardView.setVisibility(View.INVISIBLE);
        mGraphCardView.setVisibility(View.INVISIBLE);
        RetrofitClient.getRetrofitClient()
                .getDataSource(NetworkUtils.TOKEN)
                .enqueue(new Callback<VariableListResponse>() {
                    @Override
                    public void onResponse(Call<VariableListResponse> call, Response<VariableListResponse> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "getDataSource(): data source fetched");
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mGaugeCardView.setVisibility(View.VISIBLE);
                            mGraphCardView.setVisibility(View.VISIBLE);
                            ArrayList<Variable> variableListResponse = response.body().getVariableArrayList();
                            HashMap<Variable, DataSource> variableMap = new HashMap<>();
                            for (Variable variable : variableListResponse) {
                                variableMap.put(variable, variable.getDataSource());
                            }

                            for (DataSource source : variableMap.values()) {
                                ArrayList<Variable> variableArrayList = new ArrayList<>();
                                for (Variable variable : variableListResponse) {
                                    if (variable.getDataSource().equals(source)) {
                                        variableArrayList.add(variable);
                                    }
                                }
                                sourceWithVariableMap.put(source, variableArrayList);
                            }
                            mDataSourceArrayList.addAll(sourceWithVariableMap.keySet());
                            ((ArrayAdapter) mSourceSpinner.getAdapter()).notifyDataSetChanged();

                        } else {
                            mProgressBar.setVisibility(View.INVISIBLE);
                            stopRealtimeUpdates();
                            handleError("Some error occurred!");
                        }
                    }

                    @Override
                    public void onFailure(Call<VariableListResponse> call, Throwable t) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        stopRealtimeUpdates();
                        handleError("Some error occurred! " + t.getMessage());
                        Log.d(TAG, "getDataSource(): Error: " + t.getMessage());
                    }
                });
    }

    private void getVariableData(String variableId, int tabPosition) {
        if (isFetchingData) {
            showToast("Wait for current data to load");
        } else {
            isFetchingData = true;
            mGraphProgressBar.setVisibility(View.VISIBLE);
            long endTime;
            switch (tabPosition){
                case 0 :
                    endTime = ContentUtils.getEndTime(ContentUtils.Time.WEEK);
                    break;
                case 1 :
                    endTime = ContentUtils.getEndTime(ContentUtils.Time.MONTH);
                    break;
                case 2:
                    endTime = ContentUtils.getEndTime(ContentUtils.Time.MONTHS_3);
                    break;
                case 3:
                    endTime = ContentUtils.getEndTime(ContentUtils.Time.YEAR);
                    break;
                default:
                    endTime = ContentUtils.getEndTime(ContentUtils.Time.WEEK);
            }
            RetrofitClient.getRetrofitClient()
                    .getVariableDataInTimeRange(variableId, NetworkUtils.TOKEN, 1,endTime,
                            System.currentTimeMillis())
                    .enqueue(new Callback<VariableDataResponse>() {
                        @Override
                        public void onResponse(Call<VariableDataResponse> call, Response<VariableDataResponse> response) {
                            isFetchingData = false;
                            if (response.isSuccessful()) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                mGraphProgressBar.setVisibility(View.INVISIBLE);
                                mGaugeCardView.setVisibility(View.VISIBLE);
                                mGraphCardView.setVisibility(View.VISIBLE);
                                VariableDataResponse variableDataResponse = response.body();
                                ArrayList<Result> resultArrayList = variableDataResponse.getResultList();
                                if (resultArrayList.size() > 0) {
                                    Collections.reverse(resultArrayList);
                                    Result latestResult = resultArrayList.get(resultArrayList.size() - 1);
                                    getAqiStatus(latestResult.getValue());
                                    mDataSet.clear();
                                    int i = 1;
                                    for (Result result : resultArrayList) {
                                        mDataSet.addEntry(new Entry(i * 10,
                                                result.getValue(), result.getCreatedAt()));
                                        i++;
                                    }
                                    if (mLineChart.getData() == null) {
                                        mLineChart.setData(mLineData);
                                        mLineData.notifyDataChanged();
                                        mLineChart.notifyDataSetChanged();
                                        mLineChart.invalidate();
                                    } else {
                                        mLineData.notifyDataChanged();
                                        mLineChart.notifyDataSetChanged();
                                        mLineChart.invalidate();
                                    }
                                } else {
                                    mGraphProgressBar.setVisibility(View.INVISIBLE);
                                    getAqiStatus(0);
                                    mLineChart.clear();
                                }
//                                if(mTaskHandler)
//                                mTaskHandler.postDelayed(mRepetitiveTaskRunnable, 10000);

                            } else {
                                handleError("Some error occurred!");
                                mGraphProgressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<VariableDataResponse> call, Throwable t) {
                            isFetchingData = false;
                            mGraphProgressBar.setVisibility(View.INVISIBLE);
                            handleError("Some error occurred! " + t.getMessage());
                            Log.d(TAG, "getVariableData(): Error: " + t.getMessage());
                        }
                    });
        }
    }

    public void retryFromStart(){
        isFirstTime = true;
        mLastSelectedSpinnerId = NO_SELECTION;
        isMenuItemClicked = false;
        isFetchingData = false;
        getDataSource();
    }

    private void getAqiStatus(float value){

        if(value == 0){
            mAqiTextView.setText("");
            mAqiStatusTextView.setText("");
            myScale.updateIndicator(0);
            return;
        }
        if(value <= 100) {
            int color = ContextCompat.getColor(getActivity(), R.color.level_1);
            mAqiStatusTextView.setTextColor(color);
            mAqiStatusTextView.setText("Good");
            mAqiTextView.setTextColor(color);
            mAqiTextView.setText(""+value);

        } else if(value <= 200){
            int color = ContextCompat.getColor(getActivity(), R.color.level_2);
            mAqiStatusTextView.setTextColor(color);
            mAqiStatusTextView.setText("Acceptable");
            mAqiTextView.setTextColor(color);
        } else if(value <=300){
            int color = ContextCompat.getColor(getActivity(), R.color.level_3);
            mAqiStatusTextView.setTextColor(color);
            mAqiStatusTextView.setText("Moderate");
            mAqiTextView.setTextColor(color);
        } else if(value <= 400){
            int color = ContextCompat.getColor(getActivity(), R.color.level_4);
            mAqiStatusTextView.setTextColor(color);
            mAqiStatusTextView.setText("Severe");
            mAqiTextView.setTextColor(color);
        } else {
            int color = ContextCompat.getColor(getActivity(), R.color.level_5);
            mAqiStatusTextView.setTextColor(color);
            mAqiStatusTextView.setText("Hazardous");
            mAqiTextView.setTextColor(color);
        }
        mAqiTextView.setText(""+value);
        myScale.updateIndicator(value);

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

    private void showToast(String msg) {
        if (toast != null) {
            if (toast.getView().isShown()) {
                toast.cancel();
            }
        }
        toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        stopRealtimeUpdates();
        super.onStop();
    }

    public void startRealtimeUpdates(){
        mTaskHandler.removeCallbacks(mRepetitiveTaskRunnable, null);
        mTaskHandler.post(mRepetitiveTaskRunnable);
    }

    public void stopRealtimeUpdates(){
        if(mTaskHandler != null) {
            mTaskHandler.removeCallbacks(mRepetitiveTaskRunnable, null);
        }
    }
}
