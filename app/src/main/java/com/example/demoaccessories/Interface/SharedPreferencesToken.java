package com.example.demoaccessories.Interface;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesToken {
    Context context;
    SharedPreferences sharedPreferences;
    public static final String NAME_LOGIN = "isLogin";
    public static final String USERNAME = "email";
    public static final String PASSWORD = "token";
    public static final String SAVE = "isSave";

    public SharedPreferencesToken(Context context) {
        this.context = context;
    }

    //lưu đăng nhập
    public void isSaveLogin(String email, String token, boolean save) {
        sharedPreferences = context.getSharedPreferences(NAME_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, email);
        editor.putString(PASSWORD, token);
        editor.putBoolean(SAVE, save);
        editor.commit();
    }

    //lấy thông tin user
    public String getUser() {
        sharedPreferences = context.getSharedPreferences(NAME_LOGIN, 0);
        return sharedPreferences.getString(USERNAME, "");
    }

    public boolean isCheckSaveLogin(){
        sharedPreferences = context.getSharedPreferences(NAME_LOGIN, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SAVE,false);
    }

    //thoát
    public boolean loginOut() {
        sharedPreferences = context.getSharedPreferences(NAME_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SAVE, false);
        editor.commit();
        return sharedPreferences.getBoolean(SAVE, false);
    }
}
