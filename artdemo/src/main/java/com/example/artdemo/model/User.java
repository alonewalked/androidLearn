package com.example.artdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tj on 2018/1/4.
 */

public class User implements Parcelable {

    public int userId;
    public String userName;
    public boolean isMale;
    public Book book;

    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readInt() == 1;
        book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public User() {
    }

    public User(int userId, String userName, boolean isMale) {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userId);
        parcel.writeString(userName);
        parcel.writeInt(isMale ? 1 : 0);
        parcel.writeParcelable(book, 0);
    }

    @Override
    public String toString() {
        return String.format(
                "[userId:%s, userName:%s, isMale:%s, book:{%s}]",
                userId, userName, isMale, book);
    }
}
