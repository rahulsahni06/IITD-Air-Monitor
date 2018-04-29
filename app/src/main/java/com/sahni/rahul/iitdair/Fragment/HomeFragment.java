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
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sahni.rahul.iitdair.ErrorListener;
import com.sahni.rahul.iitdair.Model.DataSource;
import com.sahni.rahul.iitdair.Model.Result;
import com.sahni.rahul.iitdair.Model.Variable;
import com.sahni.rahul.iitdair.Networking.ApiInterface;
import com.sahni.rahul.iitdair.Networking.NetworkUtils;
import com.sahni.rahul.iitdair.Networking.RetrofitClient;
import com.sahni.rahul.iitdair.Networking.VariableDataResponse;
import com.sahni.rahul.iitdair.Networking.VariableListResponse;
import com.sahni.rahul.iitdair.R;
import com.sahni.rahul.iitdair.TimerMarkerView;

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
    private long referenceTimestamp;
    private ArrayList<Variable> mVariableArrayList;
    private ArrayList<DataSource> mDataSourceArrayList;
    private HashMap<DataSource, ArrayList<Variable>> sourceWithVariableMap;
    private Spinner mSourceSpinner;
    private Spinner mVariableSpinner;
    private boolean isFetchingData = false;
    private ProgressBar mGraphProgressBar;
    private Toast toast;


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

        mSourceSpinner = view.findViewById(R.id.source_spinner);
        mVariableSpinner = view.findViewById(R.id.device_spinner);
        mVariableArrayList = new ArrayList<>();
        mDataSourceArrayList = new ArrayList<>();
        sourceWithVariableMap = new HashMap<>();

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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        if(!entryList.isEmpty()) {
            mLineChart.setData(mLineData);
        }
//        mLineChart.setData(mLineData);
//        getDataSource();


        final ArrayAdapter variableAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mVariableArrayList);
        variableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mVariableSpinner.setAdapter(variableAdapter);

        ArrayAdapter sourceAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mDataSourceArrayList);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSourceSpinner.setAdapter(sourceAdapter);
        mSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected(): Item selected");
                DataSource source = mDataSourceArrayList.get(position);
                mVariableArrayList.clear();
                mVariableArrayList.addAll(sourceWithVariableMap.get(source));
                variableAdapter.notifyDataSetChanged();
//                if(mVariableArrayList.size() > 0){
//                    mVariableSpinner.setSelection(0, true);
//                }

//                PopupMenu popupMenu = new PopupMenu(getActivity(), mSourceSpinner);
//                SubMenu subMenu = popupMenu.getMenu().addSubMenu(0, -1, 0, source.getName());
//
//                for(Variable variable : mVariableArrayList) {
//                    subMenu.add(0, mVariableArrayList.indexOf(variable), Menu.NONE,variable.getName());
//                }
//                popupMenu.show();
//
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        int id = item.getSubMenu().getItem().getItemId();
//                        Variable variable = mVariableArrayList.get(id);
//                        getVariableData(variable.getId());
//                        return true;
//                    }
//                });


                PopupMenu popupMenu = new PopupMenu(getActivity(), mSourceSpinner);

                for(Variable variable : mVariableArrayList) {
                    popupMenu.getMenu().add(0, mVariableArrayList.indexOf(variable), Menu.NONE,variable.getName());
                }
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        Variable variable = mVariableArrayList.get(id);
                        getVariableData(variable.getId());
                        return true;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected");
            }
        });

        mVariableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "variable spinner, getting data");
                Variable variable = mVariableArrayList.get(position);
                getVariableData(variable.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        getDataSource();

    }

    public void fetchData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mGaugeCardView.setVisibility(View.INVISIBLE);
        mGraphCardView.setVisibility(View.INVISIBLE);
        ApiInterface apiInterface = RetrofitClient.getRetrofitClient();
        apiInterface.getUbiDotsResponse(NetworkUtils.TOKEN, 1)
                .enqueue(new Callback<VariableDataResponse>() {
                    @Override
                    public void onResponse(Call<VariableDataResponse> call, Response<VariableDataResponse> response) {

                        if (response.isSuccessful()) {
                            Log.d(TAG, "graph data fetched");
                            mProgressBar.setVisibility(View.INVISIBLE);
                            mGaugeCardView.setVisibility(View.VISIBLE);
                            mGraphCardView.setVisibility(View.VISIBLE);
                            VariableDataResponse variableDataResponse = response.body();
                            ArrayList<Result> resultArrayList = variableDataResponse.getResultList();
//                            mNameTextView.setText("Air Quality Index");
                            Collections.reverse(resultArrayList);
                            Result latestResult = resultArrayList.get(resultArrayList.size() - 1);
                            referenceTimestamp = resultArrayList.get(0).getCreatedAt();
//                            mLastUpdatedTextView.append(ContentUtils.epochToLastUpdated(latestResult.getTimestamp()));
                            Log.d(TAG, "Value =" + latestResult.getValue());
//                            mGauge.updateIndicator(latestResult.getValue());

                            int i = 1;
//                                float
//                                ArrayList<DataPoint> dataPoints = new ArrayList<>();
//                            Log.d("Rahul", "Size =" + resultArrayList.size());
                            for (Result result : resultArrayList) {
//                                Log.d("Rahul", "temp =" + result.getValue());
//                                Log.d("Rahul", "temp =" + result.getCreatedAt());
//
                                mDataSet.addEntry(new Entry(i * 10,
                                        result.getValue(), result.getCreatedAt()));
                                i++;
                            }
//                            Log.d("Rahul", "I =" + i);
                            mLineData.notifyDataChanged();
                            mLineChart.notifyDataSetChanged();
                            mLineChart.invalidate();


                        } else {
                            Log.d(TAG, "Response unsuccessfull =" + response.errorBody());
                            handleError("Some error occurred!");
                        }
                    }

                    @Override
                    public void onFailure(Call<VariableDataResponse> call, Throwable t) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Error =" + t.getMessage());
                        handleError("Please check your internet connection");
                    }
                });
    }


    private void getDataSource() {
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
                            handleError("Some error occurred!");
                        }
                    }

                    @Override
                    public void onFailure(Call<VariableListResponse> call, Throwable t) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        handleError("Some error occurred! " + t.getMessage());
                        Log.d(TAG, "getDataSource(): Error: " + t.getMessage());
                    }
                });
    }

    private void getVariableData(String variableId){
        if(isFetchingData){
            showToast("Wait for current data to load");
            Log.d(TAG, "getVariableData: wait for previous result to finish");
        } else {
            isFetchingData = true;
            Log.d(TAG, "getVariableData(): Getting new data, id =" + variableId);
            mGraphProgressBar.setVisibility(View.VISIBLE);
            RetrofitClient.getRetrofitClient()
                    .getVariableData(variableId, NetworkUtils.TOKEN, 1)
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
                                    referenceTimestamp = resultArrayList.get(0).getCreatedAt();
                                    Log.d(TAG, "getVariableData(): Value =" + latestResult.getValue());
                                    mDataSet.clear();
                                    int i = 1;
                                    for (Result result : resultArrayList) {
                                        mDataSet.addEntry(new Entry(i * 10,
                                                result.getValue(), result.getCreatedAt()));
                                        i++;
                                    }
                                    if (mLineChart.getData() == null) {
                                        Log.d(TAG, "getVariableData(): mDataSet is null");
                                        mLineChart.setData(mLineData);
                                        mLineData.notifyDataChanged();
                                        mLineChart.notifyDataSetChanged();
                                        mLineChart.invalidate();
                                    } else {
                                        Log.d(TAG, "getVariableData(): Notifying changes");
                                        mLineData.notifyDataChanged();
                                        mLineChart.notifyDataSetChanged();
                                        mLineChart.invalidate();
                                    }
                                } else {
                                    Log.d(TAG, "getVariableData(): No data available");
                                    mGraphProgressBar.setVisibility(View.INVISIBLE);
                                    mLineChart.clear();
                                }

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

    private void showToast(String msg){
        if(toast != null){
            if(toast.getView().isShown()){
                toast.cancel();
            }
        }
        toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
