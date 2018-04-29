package com.sahni.rahul.iitdair.Model;

import com.google.gson.annotations.SerializedName;

public class Variable {
    private String id;
    private String name;
    private String label;

    @SerializedName("last_value")
    private Result latestData;

    @SerializedName("datasource")
    private DataSource dataSource;

    public Variable() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Result getLatestData() {
        return latestData;
    }

    public void setLatestData(Result latestData) {
        this.latestData = latestData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int h = 7;
        final int len = label.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                h = 31 * h + label.charAt(i);
            }
        }
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Variable && label.equals(((Variable)obj).getLabel());
    }

}
