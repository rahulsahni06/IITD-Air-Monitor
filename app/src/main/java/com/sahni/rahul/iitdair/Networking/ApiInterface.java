package com.sahni.rahul.iitdair.Networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/v1.6/variables")
    Call<VariableListResponse> getDataSource(
            @Query("token") String token
    );

    @GET("api/v1.6/variables/{id}/values")
    Call<VariableDataResponse> getVariableData(
            @Path("id") String id,
            @Query("token") String token,
            @Query("page") int page
    );

    @GET("api/v1.6/variables/{id}/values")
    Call<VariableDataResponse> getVariableDataInTimeRange(
            @Path("id") String id,
            @Query("token") String token,
            @Query("page") int page,
            @Query("start") long startEpoch,
            @Query("end") long endEpoch
    );

    @GET("api/v1.6/devices/my-data-source/aqi/values")
    Call<VariableDataResponse> getUbiDotsResponse(
            @Query("token") String token,
            @Query("page") int page
    );

    @GET("api/v1.6/devices/{sourceLabel}/{variableLabel}/values")
    Call<VariableDataResponse> getUbiDotsResponse(
            @Path("sourceLabel") String sourceLabel,
            @Path("variableLabel") String variableLabel,
            @Query("token") String token,
            @Query("page") int page
    );

}
