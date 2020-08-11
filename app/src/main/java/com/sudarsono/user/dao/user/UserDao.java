package com.sudarsono.user.dao.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sudarsono.user.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {

    public void insert(SQLiteDatabase db, List<User> users) {
        ContentValues contentValues;
        for (User user : users) {
            contentValues = new ContentValues();
            contentValues.put(UserContract._ID, UUID.randomUUID().toString());
            contentValues.put(UserContract.USERNAME, user.getUsername());
            contentValues.put(UserContract.AVATAR_URL, user.getAvatarUrl());
            db.insert(UserContract.TABLE_NAME, null, contentValues);
        }
    }

    public List<User> select(SQLiteDatabase db, String name) {
        String selectQuery = "SELECT  * FROM " + UserContract.TABLE_NAME +
                " WHERE " + UserContract.USERNAME + " LIKE ?";

        String[] selectionArgs = {
                "%" + name + "%",
        };

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        List<User> users = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UserContract._ID);
            int usernameIndex = cursor.getColumnIndex(UserContract.USERNAME);
            int avatarUrlIndex = cursor.getColumnIndex(UserContract.AVATAR_URL);

            do {
                User user = new User();
                user.setId(cursor.getString(idIndex));
                user.setUsername(cursor.getString(usernameIndex));
                user.setAvatarUrl(cursor.getString(avatarUrlIndex));
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return users;
    }

    public void update(SQLiteDatabase db, String id, User user) {
        String whereClause = UserContract._ID + " = ?";
        String[] args = new String[]{
                id
        };

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserContract.USERNAME, user.getUsername());
        contentValues.put(UserContract.AVATAR_URL, user.getAvatarUrl());

        db.update(UserContract.TABLE_NAME, contentValues, whereClause, args);
    }

    public void delete(SQLiteDatabase db, String id) {
        String whereClause = UserContract._ID + " = ?";
        String[] whereArgs = new String[]{
                id
        };
        db.delete(UserContract.TABLE_NAME, whereClause, whereArgs);
    }

    public User selectById(SQLiteDatabase db, String id) {
        String selectQuery = "SELECT * FROM " + UserContract.TABLE_NAME +
                " WHERE " + UserContract._ID + " = ? LIMIT 100";

        String[] selectionArgs = {
                id
        };

        User user = null;

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UserContract._ID);
            int usernameIndex = cursor.getColumnIndex(UserContract.USERNAME);
            int avatarUrlIndex = cursor.getColumnIndex(UserContract.AVATAR_URL);

            user = new User();
            user.setId(cursor.getString(idIndex));
            user.setUsername(cursor.getString(usernameIndex));
            user.setAvatarUrl(cursor.getString(avatarUrlIndex));
        }
        cursor.close();

        return user;
    }

}
