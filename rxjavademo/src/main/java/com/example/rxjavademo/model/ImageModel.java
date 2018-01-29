package com.example.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tj on 2017/10/16.
 */

public class ImageModel {
    @SerializedName("total")
    private int total;
    @SerializedName("end")
    private boolean end;
    @SerializedName("sid")
    private String sid;
    @SerializedName("lastindex")
    private  int lastindex;
    @SerializedName("ceg")
    private int ceg;
    @SerializedName("boxresult")
    private String boxresult;
    @SerializedName("wordguess")
    private String wordguess;

    @SerializedName("list")
    private ArrayList<ImageItemModel> itemModels;

    public ArrayList<ImageItemModel> getItemModels() {
        return itemModels;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("ImageModel { ");
        str.append("total=" + total);
        str.append("end=" + end);
        str.append("sid=" + sid);
        str.append("lastindex=" + lastindex);
        str.append("ceg=" + ceg);
        str.append("boxresult=" + boxresult);
        str.append("wordguess=" + wordguess);
        str.append("list=" + itemModels);
        str.append(" }");

        return str.toString();
    }
}
