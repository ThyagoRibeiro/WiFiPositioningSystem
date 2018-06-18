package com.example.thyag.poiwifidata.view;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import com.example.thyag.poiwifidata.R;
import com.example.thyag.poiwifidata.controller.MainController;

public class PreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final ListPreference listPreference = (ListPreference) findPreference("pref_algorithm_list");
        MainController.requestAlgorithmsList(listPreference);
    }
}
