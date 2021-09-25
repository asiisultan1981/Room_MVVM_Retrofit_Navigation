package com.asii.room_mvvm_retrofit.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.asii.room_mvvm_retrofit.model.DogDatabase;

public class SharedPreferencesHelper {
    private static final String PREF_TIME = "pref time";
    private static SharedPreferencesHelper INSTANCE;
    private SharedPreferences prefs;

    public SharedPreferencesHelper(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static SharedPreferencesHelper getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (SharedPreferencesHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SharedPreferencesHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void saveUpdateTime(long time){
        prefs.edit().putLong(PREF_TIME, time).apply();
    }

    public long getUpdateTime(){
        return prefs.getLong(PREF_TIME,0);
    }
}
