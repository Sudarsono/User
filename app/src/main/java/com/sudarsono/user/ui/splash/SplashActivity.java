package com.sudarsono.user.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.sudarsono.user.R;
import com.sudarsono.user.dao.DbHelper;
import com.sudarsono.user.dao.user.UserDao;
import com.sudarsono.user.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btDelete:
                onDeleteButtonClick();
                break;
            case R.id.btInsert:
                onInsertButtonClick();
                break;
            case R.id.btUpdate:
                onUpdateButtonClick();
                break;
            case R.id.btSelect:
                onSelectButtonClick();
                break;
        }
    }

    private void onSelectButtonClick() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        UserDao dao = new UserDao();
        List<User> users = dao.select(db, "");

        db.close();
    }

    private List<User> createDummyUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setUsername("User" + i);
            user.setAvatarUrl("http://user.com/avatar/" + i);
            users.add(user);
        }
        return users;
    }

    private void onUpdateButtonClick() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        UserDao dao = new UserDao();
        List<User> users = dao.select(db, "User0");
        User user = new User();
        user.setAvatarUrl("http://user.com/avatar_url/0");
        user.setUsername("User0");
        dao.update(db, users.get(0).getId(), user);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void onInsertButtonClick() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        List<User> users = createDummyUsers();
        UserDao dao = new UserDao();
        dao.insert(db, users);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    private void onDeleteButtonClick() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        UserDao dao = new UserDao();
        List<User> users = dao.select(db, "User1");
        User user = users.get(0);
        dao.delete(db, user.getId());

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

}