package com.sahni.rahul.iitdair.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataSource {

    private String id;
    private String name;
    private String label;
    private String url;

    @Expose(serialize = false, deserialize = false)
    private ArrayList<Variable> variableArrayList;

    public DataSource() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Variable> getVariableArrayList() {
        return variableArrayList;
    }

    public void setVariableArrayList(ArrayList<Variable> variableArrayList) {
        this.variableArrayList = variableArrayList;
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
        return obj instanceof DataSource && label.equals(((DataSource)obj).getLabel());
    }

    @Override
    public String toString() {
        return name;
    }
}
