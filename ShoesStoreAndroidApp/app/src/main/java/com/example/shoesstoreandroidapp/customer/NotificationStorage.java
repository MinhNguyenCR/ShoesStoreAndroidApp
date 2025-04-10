package com.example.shoesstoreandroidapp.customer;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationStorage {
    private static final String PREF_NAME = "notification_prefs";
    private static final String KEY_LIST = "notifications";

    public static void saveNotification(Context context, Notification newNotification) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();

        List<Notification> list = getNotifications(context);
        list.add(0, newNotification); // thêm vào đầu danh sách

        String json = gson.toJson(list);
        prefs.edit().putString(KEY_LIST, json).apply();
    }

    public static List<Notification> getNotifications(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_LIST, null);
        if (json == null) return new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<List<Notification>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
