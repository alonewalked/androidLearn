package com.example.artdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tj on 2018/1/4.
 */

public class Book implements Parcelable {

    public int bookId;
    public String bookName;

    public Book() {

    }

    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    protected Book(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bookId);
        parcel.writeString(this.bookName);
    }

    @Override
    public String toString() {
        return String.format("[bookId:%s, bookName:%s]", bookId, bookName);
    }
}
