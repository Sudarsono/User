package com.sudarsono.user.dao.user;

import android.provider.BaseColumns;

public class UserContract implements BaseColumns {

    public static final String TABLE_NAME = "user";

    public static final String USERNAME = "username";

    public static final String AVATAR_URL = "avatar_url";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
            TABLE_NAME + "(" +
            _ID + " TEXT PRIMARY KEY, " +
            USERNAME + " TEXT, " +
            AVATAR_URL + " TEXT" +
            ");";

}
