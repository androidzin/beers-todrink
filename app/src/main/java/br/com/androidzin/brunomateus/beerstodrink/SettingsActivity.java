package br.com.androidzin.brunomateus.beerstodrink;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.androidannotations.annotations.EActivity;


/**
 * Created by bruno on 02/05/15.
 */
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends ActionBarActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        getFragmentManager().beginTransaction()
                .replace(R.id.preference_container, new SettingsFragment_())
                .commit();
    }
}
