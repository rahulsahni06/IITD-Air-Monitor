package com.sahni.rahul.iitdair.Networking;

import com.google.gson.annotations.SerializedName;
import com.sahni.rahul.iitdair.Model.Result;

import java.util.ArrayList;

public class UbiDotsResponse {

    private boolean count;

    @SerializedName("previous")
    private String previousUrl;

    @SerializedName("results")
    private ArrayList<Result> resultList;

    @SerializedName("next")
    private String nextUrl;

    public UbiDotsResponse() {
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    public ArrayList<Result> getResultList() {
        return resultList;
    }

    public void setResultList(ArrayList<Result> resultList) {
        this.resultList = resultList;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }
}
