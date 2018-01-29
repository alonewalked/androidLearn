package com.example.artdemo.binder.contentprovider;

import android.content.UriMatcher;
import android.net.Uri;

/**
 * Created by tj on 2018/1/4.
 */

public class ProviderConstant {

    public static final String AUTHORITY = "com.example.artdemo.book.provider";    // 同ApplicationManifest
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // 关联Uri和Uri_Code
    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }


}
