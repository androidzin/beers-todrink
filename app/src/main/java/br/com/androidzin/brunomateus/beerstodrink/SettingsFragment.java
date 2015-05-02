package br.com.androidzin.brunomateus.beerstodrink;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import org.androidannotations.annotations.AfterPreferences;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.PreferenceByKey;
import org.androidannotations.annotations.PreferenceChange;
import org.androidannotations.annotations.PreferenceScreen;

/**
 * Created by bruno on 02/05/15.
 */
@SuppressLint("NewApi")
@EFragment
@PreferenceScreen(R.xml.settings)
public class SettingsFragment extends PreferenceFragment {

    @PreferenceByKey(R.string.pref_key_face_share_checkin)
    CheckBoxPreference sharing;

    @PreferenceByKey(R.string.pref_temperature)
    ListPreference temperatureOption;


}
