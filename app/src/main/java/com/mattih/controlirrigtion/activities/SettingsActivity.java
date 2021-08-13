package com.mattih.controlirrigtion.activities;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.mattih.controlirrigtion.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        int commit = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        public static final String PREF_FONT_type = "font_type";
        public static final String PREF_LANGUAGE = "language_pref";
        private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if (key.equals(PREF_LANGUAGE)) {
                        ListPreference langPref = (ListPreference) findPreference(key);
                        langPref.setSummary(sharedPreferences.getString(key, "English"));
                        langPref.setValue(sharedPreferences.getString(key, "en"));
                    }
                }
            };
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
            ListPreference fontTypePref = (ListPreference) findPreference(PREF_LANGUAGE);
            fontTypePref.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_LANGUAGE, "English"));
            fontTypePref.setValue(getPreferenceScreen().getSharedPreferences().getString(PREF_LANGUAGE, "en"));
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

        }
    }
}