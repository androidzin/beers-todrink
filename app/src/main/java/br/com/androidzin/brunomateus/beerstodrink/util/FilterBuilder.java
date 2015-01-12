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

    public static final String CRITERIA = "criteria";
    public static final String AND = " AND ";
    public static final String LIKE = "LIKE";
    public static final String OR = "OR";
    protected static String nameSelection;
    protected static String currentFilterCountry;
    protected static boolean showDrank = false;

    public static boolean showAll(){
        return !showDrank;
    }

    public static String getQuery(Bundle args) {
        if(args != null && args.containsKey(CRITERIA))
        {
            BeerListActivity.BeerFilterCriteria criteria =
                    BeerListActivity.BeerFilterCriteria.values()[args.getInt(CRITERIA)];
            switch(criteria){
                case NAME:
                    addNameCriteria(args);
                    break;
                case COUNTRY:
                    addCountryCriteria(args);
                    break;
                case DRINK:
                    addDrinkCriteria();
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

        if(!showDrank){
            selection = BeerContract.NOT_DRANK_BEERS;
        }
        if(nameSelection != null){
            if(selection == null){
                selection = nameSelection;
            } else {
                selection = selection.concat(AND).concat(nameSelection);
            }
        }

        if(currentFilterCountry != null){
            if(selection == null){
                selection = currentFilterCountry;
            } else{
                selection = selection.concat(AND).concat(currentFilterCountry);
            }
        }
        return selection;
    }

    private static void addDrinkCriteria() {
        showDrank = !showDrank;
    }

    private static void addCountryCriteria(Bundle args) {
        if(args.containsKey(BeerContract.BeerColumns.BEER_COUNTRY)){
            ArrayList<String> countries = args.getStringArrayList(BeerContract.BeerColumns.BEER_COUNTRY);
            StringBuffer query = new StringBuffer();
            for(String country: countries){
                query.append(BeerContract.BeerColumns.BEER_COUNTRY)
                        .append(" = '")
                        .append(country)
                        .append("' " + OR + " ");
            }
            if(query.length() > 0){
                currentFilterCountry = "( " + query.substring(0, query.lastIndexOf(" " + OR)) + ")";
            }
        } else {
            currentFilterCountry = null;
        }
    }

    private static void addNameCriteria(Bundle args) {
        if(args.containsKey(BeerContract.BeerColumns.BEER_NAME)) {
            nameSelection = BeerContract.BeerColumns.BEER_NAME + " " + LIKE + " '%" +
                    args.getString(BeerContract.BeerColumns.BEER_NAME) + "%'";
        } else {
            nameSelection = null;
        }
    }
}
