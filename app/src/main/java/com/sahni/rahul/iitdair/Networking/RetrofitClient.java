package com.sahni.rahul.iitdair.Networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static ApiInterface apiInterface;

    public static ApiInterface getRetrofitClient(){
        if(apiInterface == null){
            apiInterface = new Retrofit.Builder()
                    .baseUrl(NetworkUtils.BASE_URL_PRODUCTION)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiInterface.class);
        }
        return apiInterface;
    }
}
