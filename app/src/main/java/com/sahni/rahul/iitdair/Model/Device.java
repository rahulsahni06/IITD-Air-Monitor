package com.sahni.rahul.iitdair.Model;

import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("thing")
    private String name;
    private String created;

    @SerializedName("content")
    private Data data;

    public Device() {
    }

    public Device(String name, String created, Data data) {
        this.name = name;
        this.created = created;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
