package com.moonhythe.songle.Structure;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kris on 05/11/17.
 *
 * USAGE:
 *
 *         Preference.setSharedPreferenceString(this, "Key", "Value");
 *         Preference.getSharedPreferenceString(this, "Key", "Default value");
 */
public class Preference {
    private final static String PREF_FILE = "PREF";
    private static final String TAG = Preference.class.getSimpleName();

    /**
     * Add a badge to shared preference
     *
     * @param context - Activity context
     * @param badge - Badge to add
     */

    public static void addBadge(Context context, Badge badge){
        int badges_count = getSharedPreferenceInt(context, "badges_count", 0);
        int current_badge = badges_count + 1;
        setSharedPreferenceInt(context, "badges_count", current_badge);

        Gson gson = new Gson();
        String json = gson.toJson(badge);
        setSharedPreferenceString(context, "badge_" + current_badge, json);
    }

    /**
     * Set a string shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     *
     * @param context - Activity context
     * @return badges - All current badges
     */
    public static List<Badge> getBadges(Context context){
        Gson gson = new Gson();
        String json;

        List<Badge> badges = new ArrayList<Badge>();
        int badges_count = getSharedPreferenceInt(context, "badges_count", 0);

        for(int i=1; i<=badges_count;i++){
            json = getSharedPreferenceString(context, "badge_" + i, "");
            badges.add(gson.fromJson(json, Badge.class));
        }
        Log.i(TAG, "Returning badges count is " + badges.size());
        return badges;
    }

    /**
     * Get a string shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    /**
     * Get a integer shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getSharedPreferenceInt(Context context, String key, int defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getInt(key, defValue);
    }

    /**
     * Get a boolean shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }
}