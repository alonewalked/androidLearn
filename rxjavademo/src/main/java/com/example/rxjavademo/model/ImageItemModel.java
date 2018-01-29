package com.example.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tj on 2017/10/16.
 */

public class ImageItemModel {

    @SerializedName("id")
    private String id;
    @SerializedName("qqface_down_url")
    private boolean qqface_down_url;
    @SerializedName("downurl")
    private boolean downurl;
    @SerializedName("rpmd5")
    private boolean rpmd5;
    @SerializedName("type")
    private int type;
    @SerializedName("src")
    private int src;
    @SerializedName("index")
    private int index;
    @SerializedName("title")
    private String title;
    @SerializedName("litetitle")
    private String litetitle;
    @SerializedName("width")
    private int width;

    public int getWidth() {
        return width;
    }

    @SerializedName("height")
    private int height;

    public int getHeight() {
        return height;
    }

    @SerializedName("imgsize")
    private String imgsize;
    @SerializedName("imgtype")
    private String imgtype;
    @SerializedName("key")
    private String key;
    @SerializedName("dspurl")
    private String dspurl;
    @SerializedName("link")
    private String link;
    @SerializedName("source")
    private int source;
    @SerializedName("img")
    private String img;

    public String getImg() {
        return img;
    }

    @SerializedName("thumb_bak")
    private String thumb_bak;
    @SerializedName("thumb")
    private String thumb;
    @SerializedName("_thumb_bak")
    private String _thumb_bak;
    @SerializedName("_thumb")
    private String _thumb;
    @SerializedName("thumbWidth")
    private int thumbWidth;
    @SerializedName("thumbHeight")
    private int thumbHeight;
    @SerializedName("grpcnt")
    private int grpcnt;
    @SerializedName("fixedSize")
    private boolean fixedSize;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ImageItemModel { ");
        str.append("id = " + id);
        str.append("qqface_down_url = " + qqface_down_url);
        str.append("downurl = " + downurl);
        str.append("rpmd5 = " + rpmd5);
        str.append("type = " + type);
        str.append("src = " + src);
        str.append("index = " + index);
        str.append("title = " + title);
        str.append("litetitle = " + litetitle);
        str.append("width = " + width);
        str.append("height = " + height);
        str.append("imgsize = " + imgsize);
        str.append("imgtype = " + imgtype);
        str.append("key = " + key);
        str.append("dspurl = " + dspurl);
        str.append("link = " + link);
        str.append("source = " + source);
        str.append("img = " + img);
        str.append("thumb_bak = " + thumb_bak);
        str.append("thumb = " + thumb);
        str.append("thumbWidth = " + thumbWidth);
        str.append("thumbHeight = " + thumbHeight);
        str.append("grpcnt = " + grpcnt);
        str.append("fixedSize = " + fixedSize);
        str.append(" }");
        return str.toString();
    }

}
