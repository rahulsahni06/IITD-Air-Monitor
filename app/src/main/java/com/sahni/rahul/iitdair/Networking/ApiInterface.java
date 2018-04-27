package com.sahni.rahul.iitdair.Networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("get/dweets/for/myesp8266")
    Call<DataResponse> getDataResponse();

    @GET("api/v1.6/devices/my-data-source/aqi/values")
    Call<UbiDotsResponse> getUbiDotsResponse(
            @Query("token") String token,
            @Query("page") int page
    );

}
