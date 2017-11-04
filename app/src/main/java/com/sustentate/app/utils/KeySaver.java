package com.sustentate.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.sustentate.app.SustentateApplication;

import java.util.Map;

public class KeySaver {

    private static final String KEY = "sustentate";
    private static final String PREFIX = "sustentate_";

    private static final String KEY_PERMISSION ="permission";
    private static final String PREFIX_PERMISSION ="permission_";

    public static String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getDeviceID(Activity activity) {
        return Secure.getString(activity.getBaseContext().getContentResolver(), Secure.ANDROID_ID);
    }

    public static String getDeviceID() {
        return Secure.getString(SustentateApplication.CONTEXT.getContentResolver(), Secure.ANDROID_ID);
    }

    public static void saveShare(Activity activity, String keyname, boolean f) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFIX + keyname, f);
        editor.apply();
    }

    public static void saveShare(Activity activity, String keyname, int f) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(PREFIX + keyname, f);
        editor.apply();
    }

    public static void saveShare(Activity activity, String keyname, String f) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFIX + keyname, f);
        editor.apply();
    }

    public static void saveShare(Context activity, String keyname, String f) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFIX + keyname, f);
        editor.apply();
    }

    public static void savePermission(Context context, String keyname, boolean f) {
        SharedPreferences settings = context.getSharedPreferences(KEY_PERMISSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY_PERMISSION + keyname, f);
        editor.apply();
    }

    public static boolean getPermission(Context context, String keyname) {
        SharedPreferences settings = context.getSharedPreferences(KEY_PERMISSION, Context.MODE_PRIVATE);
        return settings.getBoolean(KEY_PERMISSION + keyname, false);
    }

    public static boolean getBoolSavedShare(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getBoolean(PREFIX + keyname, false);
    }

    public static boolean getBoolTrueSavedShare(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getBoolean(PREFIX + keyname, true);
    }

    public static int getIntSavedShare(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getInt(PREFIX + keyname, -1);
    }

    public static int getIntMinSavedShare(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getInt(PREFIX + keyname, 0);
    }

    public static String getStringSavedShare(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getString(PREFIX + keyname, "");
    }

    public static String getStringSavedShare(Context context, String keyname) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getString(PREFIX + keyname, null);
    }

    public static Map<String, ?> getAll(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.getAll();
    }

    public static void deleteAll(Activity activity) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    public static String getPrefix() {
        return PREFIX;
    }

    public static SharedPreferences getShared(Activity activity) {
        return activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static void removeKey(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        if (isExist(activity, keyname)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(PREFIX + keyname);
            editor.apply();
        }
    }

    public static boolean isExist(Activity activity, String keyname) {
        SharedPreferences settings = activity.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.contains(PREFIX + keyname);
    }

    public static boolean isExist(Context context, String keyname) {
        SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return settings.contains(PREFIX + keyname);
    }


}

