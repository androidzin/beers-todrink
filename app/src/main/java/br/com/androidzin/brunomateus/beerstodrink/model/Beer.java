package br.com.androidzin.brunomateus.beerstodrink.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * Created by bruno on 11/12/14.
 */
public class Beer {

    private int id;
    private String name;
    private String temperatureToDrink;
    private String country;
    private String abv;
    private String releaseDate;
    private String color;
    private boolean drinked;

    private Beer(String name, String country, String temperatureToDrink, float abv,
                 String releaseDate, String color) {
        this.drinked = false;
        this.name = name;
        this.temperatureToDrink = temperatureToDrink;
        this.country = country;
        this.abv = String.valueOf(abv).concat("%");
        this.releaseDate = releaseDate;
        this.color = color;
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

    public String getCountry(){
        return country;
    }

    public String getAbv() {
        return abv;
    }

    public boolean isDrinked() {
        return drinked;
    }

    private int isDrinkedInt(){
        return isDrinked() == true ? 1 : 0 ;
    }

    public void setDrinked(boolean drinked) {
        this.drinked = drinked;
    }

    private void setDrinked(int drinked){
        setDrinked(drinked == 1 ? true : false);
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getColor() {
        return color;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(BeerColumns.BEER_NAME, getName());
        values.put(BeerColumns.BEER_COUNTRY, getCountry());
        values.put(BeerColumns.BEER_ABV, getAbv());
        values.put(BeerColumns.BEER_TEMPERATURE_TO_DRINK, getTemperatureToDrink());
        values.put(BeerColumns.BEER_DRINKED, isDrinkedInt());
        values.put(BeerColumns.BEER_RELEASE_DATE, getReleaseDate());
        values.put(BeerColumns.BEER_COLOR, getColor());

        return values;
    }

    public static Beer beerFromCursor(Cursor data){
        Beer beer = new Beer(data.getString(BeerColumns.Index.BEER_NAME),
                data.getString(BeerColumns.Index.BEER_COUNTRY),
                data.getString(BeerColumns.Index.BEER_DRINKED),
                data.getFloat(BeerColumns.Index.BEER_ABV),
                data.getString(BeerColumns.Index.BEER_RELEASE_DATE),
                data.getString(BeerColumns.Index.BEER_COLOR));

        beer.id = data.getInt(BeerColumns.Index.BEER_ID);
        beer.setDrinked(data.getInt(BeerColumns.Index.BEER_DRINKED));

        return beer;
    }
}
