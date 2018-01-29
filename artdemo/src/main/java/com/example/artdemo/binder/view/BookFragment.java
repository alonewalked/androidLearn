package com.example.artdemo.binder.view;

import com.example.artdemo.R;
import com.example.artdemo.binder.contentprovider.ProviderConstant;
import com.example.artdemo.model.Book;
import com.example.artdemo.model.User;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by tj on 2018/1/5.
 */

public class BookFragment extends Fragment {

    private static final String TAG = "BookFragment";
    private TextView mTvShowBooks; // 显示书籍
    private TextView mTvShowUsers; // 显示用户
    private Button btnAddBook;
    private Button btnShowBook;
    private Button btnShowUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book, container,
                false);
        this.initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        mTvShowBooks = (TextView) view.findViewById(R.id.main_tv_show_books);
        mTvShowUsers = (TextView) view.findViewById(R.id.main_tv_show_users);
        btnShowBook = (Button) view.findViewById(R.id.btn_show_book);
        btnShowBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookFragment.this.showBooks(view);
            }
        });
        btnShowUser = (Button) view.findViewById(R.id.btn_show_user);
        btnShowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookFragment.this.showUsers(view);
            }
        });
        btnAddBook = (Button) view.findViewById(R.id.btn_add_book);
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookFragment.this.addBooks(view);
            }
        });
    }

    /**
     * 添加书籍的事件监听
     *
     * @param view 视图
     */
    public void addBooks(View view) {
        Uri bookUri = ProviderConstant.BOOK_CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "信仰上帝");
        getActivity().getContentResolver().insert(bookUri, values);
    }

    /**
     * 显示书籍
     *
     * @param view 视图
     */
    public void showBooks(View view) {
        String content = "";
        Uri bookUri = ProviderConstant.BOOK_CONTENT_URI;
        Cursor bookCursor = getActivity().getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        if (bookCursor != null) {
            while (bookCursor.moveToNext()) {
                Book book = new Book();
                book.bookId = bookCursor.getInt(0);
                book.bookName = bookCursor.getString(1);
                content += book.toString() + "\n";
                Log.i(TAG, "provider___query book: " + book.toString());
                mTvShowBooks.setText(content);
            }
            bookCursor.close();
        }
    }

    /**
     * 显示用户
     *
     * @param view 视图
     */
    public void showUsers(View view) {
        String content = "";
        Uri userUri = ProviderConstant.USER_CONTENT_URI;
        Cursor userCursor = getActivity().getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        if (userCursor != null) {
            while (userCursor.moveToNext()) {
                User user = new User();
                user.userId = userCursor.getInt(0);
                user.userName = userCursor.getString(1);
                user.isMale = userCursor.getInt(2) == 1;
                content += user.toString() + "\n";
                Log.e(TAG, "query user:" + user.toString());
                mTvShowUsers.setText(content);
            }
            userCursor.close();
        }
    }
}
