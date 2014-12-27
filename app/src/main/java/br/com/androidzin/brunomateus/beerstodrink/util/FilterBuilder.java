package br.com.androidzin.brunomateus.beerstodrink.util;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import br.com.androidzin.brunomateus.beerstodrink.BeerListActivity;
import br.com.androidzin.brunomateus.beerstodrink.BuildConfig;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;

/**
 * Created by bruno on 27/12/14.
 */
public class FilterBuilder {

    protected static String nameSelection;
    protected static String currentFilterCountry;

    public static String getQuery(Bundle args) {
        if(args != null && args.containsKey("criteria"))
        {
            BeerListActivity.BeerFilterCriteria criteria =
                    BeerListActivity.BeerFilterCriteria.values()[args.getInt("criteria")];
            switch(criteria){
                case NAME:
                    addNameCriteria(args);
                    break;
                case COUNTRY:
                    addCountryCriteria(args);
                    break;
            }
        }

        String selection = buildQuery();

        if(selection != null && BuildConfig.DEBUG){
            Log.d(FilterBuilder.class.getSimpleName(), "SQL " + selection);
        }
        return selection;
    }

    private static String buildQuery() {
        String selection = null;
        if(nameSelection != null){
            selection = nameSelection;
        }

        if(currentFilterCountry != null){
            if(selection == null){
                selection = currentFilterCountry;
            } else{
                selection = nameSelection.concat(" AND ").concat(currentFilterCountry);
            }
        }
        return selection;
    }

    private static void addCountryCriteria(Bundle args) {
        if(args.containsKey(BeerContract.BeerColumns.BEER_COUNTRY)){
            ArrayList<String> countries = args.getStringArrayList(BeerContract.BeerColumns.BEER_COUNTRY);
            StringBuffer query = new StringBuffer();
            for(String country: countries){
                query.append(BeerContract.BeerColumns.BEER_COUNTRY)
                        .append(" = '")
                        .append(country)
                        .append("' OR ");
            }
            currentFilterCountry = query.substring(0, query.lastIndexOf("OR"));
        } else {
            currentFilterCountry = null;
        }
    }

    private static void addNameCriteria(Bundle args) {
        if(args.containsKey(BeerContract.BeerColumns.BEER_NAME)) {
            nameSelection = BeerContract.BeerColumns.BEER_NAME + " LIKE '%" +
                    args.getString(BeerContract.BeerColumns.BEER_NAME) + "%'";
        } else {
            nameSelection = null;
        }
    }
}
