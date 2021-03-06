package br.com.androidzin.brunomateus.beerstodrink;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;
import br.com.androidzin.brunomateus.beerstodrink.util.InformationFromResource;

/**
 * Created by bruno on 26/12/14.
 */
public class BeerFilterCountryDialog extends DialogFragment {

    private static final int LOADER_ID = 1;
    private static String[] countries;
    private static boolean[] checked;
    private FilterCountryListener listener;

    public interface FilterCountryListener {
        public void onFilter(Bundle queryBundler);
    }

    public void setListener(FilterCountryListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), R.style.AppTheme);
        getCountries();
        b.setTitle(R.string.filter_country)
                .setMultiChoiceItems(countries, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checked[which] = isChecked;
                    }
                }).setPositiveButton(getActivity().getString(R.string.filter),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> coutriesList = new ArrayList<String>();
                for (int i = 0; i < checked.length; i++) {
                    if (checked[i]) {
                        coutriesList.add(countries[i].toLowerCase().replace(" ", "_"));
                    }
                }
                Bundle queryBundle = new Bundle();
                if(!coutriesList.isEmpty()) {
                    queryBundle.putStringArrayList(BeerContract.BeerColumns.BEER_COUNTRY,
                            coutriesList);
                }
                listener.onFilter(queryBundle);
            }
        }).setNegativeButton(getActivity().getString(R.string.clear),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < checked.length; i++){
                            checked[i] = false;
                        }
                        listener.onFilter(new Bundle());
                    }
                });
        return b.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void getCountries(){
        if(countries == null) {
            String[] projection = {BeerContract.BeerColumns.BEER_COUNTRY};
            Cursor data = getActivity().getContentResolver().query(
                    BeerContract.BeerColumns.CONTENT_URI,
                    projection,
                    BeerContract.BEER_COUNTRIES,
                    null,
                    BeerContract.BeerColumns.BEER_COUNTRY + " asc");

            if(data.getCount() > 0) {
                countries = new String[data.getCount()];
                checked = new boolean[data.getCount()];
                int i = 0;
                while (data.moveToNext()) {
                    countries[i] = InformationFromResource.getBeerCountry(getActivity(), data.getString(0));
                    checked[i] = false;
                    i++;
                }
            }
        }
    }
}
