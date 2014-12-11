package br.com.androidzin.brunomateus.beerstodrink.model;

import android.content.ContentValues;
import android.content.Context;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * Created by bruno on 11/12/14.
 */
public class Beer {

    private String name;
    private String temperatureToDrink;
    private String country;
    private String abv;
    private boolean drinked;

    public Beer(String name, String country, String temperatureToDrink, float abv) {
        this.drinked = false;
        this.name = name;
        this.temperatureToDrink = temperatureToDrink;
        this.country = country;
        this.abv = String.valueOf(abv).concat("%");
    }

    public String getName() {
        return name;
    }

    public String getTemperatureToDrink() {
        return temperatureToDrink;
    }

    public String getCountry(Context context) {
        int resourceId = context.getResources().getIdentifier(country, "string", null);
        return context.getString(resourceId);
    }

    private String getCountry(){
        return country;
    }

    public String getAbv() {
        return abv;
    }

    public boolean isDrinked() {
        return drinked;
    }

    public void setDrinked(boolean drinked) {
        this.drinked = drinked;
    }

    private int isDrinkedInt(){
        return isDrinked() == true ? 1 : 0 ;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(BeerColumns.BEER_NAME, getName());
        values.put(BeerColumns.BEER_COUNTRY, getCountry());
        values.put(BeerColumns.BEER_ABV, getAbv());
        values.put(BeerColumns.BEER_TEMPERATURE_TO_DRINK, getTemperatureToDrink());
        values.put(BeerColumns.BEER_DRINKED, isDrinkedInt());

        return values;
    }
}
