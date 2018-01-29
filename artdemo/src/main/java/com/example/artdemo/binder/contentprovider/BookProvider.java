package com.example.artdemo.binder.contentprovider;

import com.example.artdemo.db.DbOpenHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by tj on 2018/1/4.
 */

public class BookProvider extends ContentProvider {

    private static final String TAG = "BookProvider";
    // 关联Uri和Uri_Code

    private Context mContext;
    private SQLiteDatabase mDb;


    @Override
    public boolean onCreate() {
        Log.i(TAG, "provider___onCreate 当前线程: " + Thread.currentThread().getName());
        mContext = getContext();

        initProviderData(); // 初始化Provider数据

        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.i(TAG, "provider___query 当前线程: " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDb.query(tableName, strings, s, strings1, null, null, s1, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.i(TAG,  "provider___getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.i(TAG, "provider___insert");
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb.insert(table, null, values);

        // 插入数据后通知改变
        mContext.getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.i(TAG, "provider___delete");
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.delete(table, s, strings);
        if (count > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return count; // 返回删除的函数
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.i(TAG, "provider___update");
        String table = getTableName(uri);
        if (TextUtils.isEmpty(table)) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = mDb.update(table, contentValues, s, strings);
        if (row > 0) {
            mContext.getContentResolver().notifyChange(uri, null);
        }

        return row; // 返回更新的行数
    }

    /**
     * init database data
     */
    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'Android');");
        mDb.execSQL("insert into book values(4, 'iOS');");
        mDb.execSQL("insert into book values(5, 'HTML5');");
        mDb.execSQL("insert into user values(1, 'Spike', 1);");
        mDb.execSQL("insert into user values(2, 'Wang', 0);");
    }

    /**
     * get table name
     * @param uri
     * @return
     */
    private String getTableName(Uri uri) {
        String tableName = null;
        switch (ProviderConstant.sUriMatcher.match(uri)) {
            case ProviderConstant.BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case ProviderConstant.USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
