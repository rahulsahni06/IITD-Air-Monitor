package com.sahni.rahul.iitdair.Model;

import com.google.gson.annotations.SerializedName;

public class Result {

    private long timestamp;

    @SerializedName("created_at")
    private long createdAt;

    private float value;

    public Result() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
