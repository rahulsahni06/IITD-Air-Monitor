package com.sahni.rahul.iitdair.Networking;

import com.google.gson.annotations.SerializedName;
import com.sahni.rahul.iitdair.Model.Variable;

import java.util.ArrayList;

public class VariableListResponse {

    @SerializedName("results")
    private ArrayList<Variable> variableArrayList;

    public VariableListResponse() {
    }

    public ArrayList<Variable> getVariableArrayList() {
        return variableArrayList;
    }

    public void setVariableArrayList(ArrayList<Variable> variableArrayList) {
        this.variableArrayList = variableArrayList;
    }
}
