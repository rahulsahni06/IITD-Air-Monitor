package com.sahni.rahul.iitdair.Networking;

import com.google.gson.annotations.SerializedName;
import com.sahni.rahul.iitdair.Model.Device;

import java.util.ArrayList;

public class DataResponse {

    @SerializedName("this")
    private String status;

    private String by;

    private String the;

    @SerializedName("with")
    private ArrayList<Device> deviceDataList;

    public DataResponse() {
    }

    public DataResponse(String status, String by, String the, ArrayList<Device> deviceDataList) {
        this.status = status;
        this.by = by;
        this.the = the;
        this.deviceDataList = deviceDataList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getThe() {
        return the;
    }

    public void setThe(String the) {
        this.the = the;
    }

    public ArrayList<Device> getDeviceDataList() {
        return deviceDataList;
    }

    public void setDeviceDataList(ArrayList<Device> deviceDataList) {
        this.deviceDataList = deviceDataList;
    }
}
