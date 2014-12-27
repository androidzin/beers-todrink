package br.com.androidzin.brunomateus.beerstodrink;

import android.os.Bundle;
import android.util.Log;

import junit.framework.TestCase;

import java.util.ArrayList;

import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;
import br.com.androidzin.brunomateus.beerstodrink.util.FilterBuilder;

/**
 * Created by bruno on 27/12/14.
 */
public class BeerFilterTest extends TestCase {

    public static class FilterBuilderToTest extends FilterBuilder {

        public static void clearQuery(){
            FilterBuilder.nameSelection = null;
            FilterBuilder.currentFilterCountry = null;
            FilterBuilder.showDrinked = false;
        }

        public static String getQuery(Bundle args){
            String query = FilterBuilder.getQuery(args);
            clearQuery();
            return query;
        }
    }

    private Bundle getNameFilterBundle(String beerName) {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("criteria", BeerListActivity.BeerFilterCriteria.NAME.ordinal());
        if(!beerName.isEmpty()) {
            queryBundle.putString(BeerContract.BeerColumns.BEER_NAME, beerName);
        }
        return queryBundle;
    }

    private Bundle getCountryFilterBundle(ArrayList<String> countries) {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("criteria", BeerListActivity.BeerFilterCriteria.COUNTRY.ordinal());
        if(!countries.isEmpty()) {
            queryBundle.putStringArrayList(BeerContract.BeerColumns.BEER_COUNTRY, countries);
        }
        return queryBundle;
    }

    private Bundle getDrinkFilterBundle() {
        Bundle queryBundle = new Bundle();
        queryBundle.putInt("criteria", BeerListActivity.BeerFilterCriteria.DRINK.ordinal());
        return queryBundle;
    }

    public void testFilterByEmptyName(){
        String query = FilterBuilderToTest.getQuery(getNameFilterBundle(""));
        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0";
        assertEquals(expected, query);
    }

    public void testFilterByName(){
        String beerName = "teste";
        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0 AND " +
                BeerContract.BeerColumns.BEER_NAME + " LIKE '%" + beerName + "%'";
        String query = FilterBuilderToTest.getQuery(getNameFilterBundle(beerName));
        assertEquals(expected, query);
    }

    public void testFilterByOneCountry(){
        String[] arrayCountries = {"brazil"};
        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0 AND " +
                "( " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[0] + "')";
        ArrayList<String> countries = new ArrayList<String>();
        countries.add(arrayCountries[0]);
        String query = FilterBuilderToTest.getQuery(getCountryFilterBundle(countries));
        assertEquals(expected, query.trim());
    }

    public void testFilterByCountry(){
        String[] arrayCountries = {"brazil", "usa"};
        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0 AND " +
                "( " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[0] +
                "' OR " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[1] + "')";
        ArrayList<String> countries = new ArrayList<String>();
        countries.add(arrayCountries[0]);
        countries.add(arrayCountries[1]);
        String query = FilterBuilderToTest.getQuery(getCountryFilterBundle(countries));
        assertEquals(expected, query.trim());
    }

    public void testFilterByNameAfterCountry(){
        String beerName = "teste";
        FilterBuilder.getQuery(getNameFilterBundle(beerName));
        String[] arrayCountries = {"brazil", "usa"};
        ArrayList<String> countries = new ArrayList<String>();
        countries.add(arrayCountries[0]);
        countries.add(arrayCountries[1]);

        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0 AND " +
                BeerContract.BeerColumns.BEER_NAME + " LIKE '%" + beerName + "%'" +
                " AND ( " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[0] +
                "' OR " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[1] + "')";

        String query = FilterBuilderToTest.getQuery(getCountryFilterBundle(countries));
        assertEquals(expected, query.trim());
    }

    public void testFilterByCountryAfterName(){
        String[] arrayCountries = {"brazil", "usa"};
        ArrayList<String> countries = new ArrayList<String>();
        countries.add(arrayCountries[0]);
        countries.add(arrayCountries[1]);
        FilterBuilder.getQuery(getCountryFilterBundle(countries));

        String beerName = "teste";
        String query = FilterBuilderToTest.getQuery(getNameFilterBundle(beerName));

        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0 AND " +
                BeerContract.BeerColumns.BEER_NAME + " LIKE '%" + beerName + "%'" +
                " AND ( " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[0] +
                "' OR " + BeerContract.BeerColumns.BEER_COUNTRY + " = '" + arrayCountries[1] + "')";

        assertEquals(expected, query.trim());
    }

    public void testShowAll(){
        String query = FilterBuilderToTest.getQuery(getDrinkFilterBundle());
        assertNull(query);
    }

    public void testShowToDrink(){
        FilterBuilder.getQuery(getDrinkFilterBundle());
        String query = FilterBuilderToTest.getQuery(getDrinkFilterBundle());
        String expected = BeerContract.BeerColumns.BEER_DRANK + " = 0";
        assertEquals(expected, query);
    }
}
