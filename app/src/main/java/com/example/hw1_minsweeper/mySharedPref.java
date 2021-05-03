package com.example.hw1_minsweeper;
import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;


public class mySharedPref {

        private SharedPreferences prefs;

        public mySharedPref (Context context) {
            prefs = context.getSharedPreferences("MyPref2", MODE_PRIVATE);

        }

        public int getInt(String key, int defaultValue) {
            return prefs.getInt(key, defaultValue);
        }

        public void putInt(String key, int value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(key, value);
            editor.apply();
        }

        public String getString(String key, String defaultValue) {
            return prefs.getString(key, defaultValue);
        }

        public void putString(String key, String value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public long getLong(String key, long defaultValue) {
            return prefs.getLong(key, defaultValue);
        }

        public void putLong(String key, long value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(key, value);
            editor.apply();
        }

        public void removeKey(String key) {
            prefs.edit().remove(key);
        }
}
