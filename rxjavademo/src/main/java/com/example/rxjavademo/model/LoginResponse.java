package com.example.rxjavademo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tj on 2017/10/16.
 */

public class LoginResponse {
    public LoginResponse() {

    }
    public LoginResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }
    @SerializedName("Message")
    private String message;
    @SerializedName("Code")
    private int code;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoginResponse { ");
        builder.append("message="+message);
        builder.append("code="+code);
        builder.append(" } ");
        return builder.toString();
    }
}
