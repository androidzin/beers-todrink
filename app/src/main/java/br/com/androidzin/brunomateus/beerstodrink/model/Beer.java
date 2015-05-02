package br.com.androidzin.brunomateus.beerstodrink.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * Created by bruno on 11/12/14.
 */
public class Beer {

    public static final String ºF = " ºF";
    public static final String ºC = " ºC";
    private int id;
    private String name;
    private String rawTemperature;
    private String country;
    private String abv;
    private String releaseDate;
    private String color;
    private boolean drinked;

    public interface Drinkable {
        public void onDrink(Beer beer);
        public void onNotDrank(Beer beer);
    }

    public Beer(String name, String country, String temperatureToDrink, float abv,
                 String releaseDate, String color) {
        this.drinked = false;
        this.name = name;
        this.rawTemperature = temperatureToDrink;
        this.country = country;
        this.abv = String.valueOf(abv).concat("%");
        this.releaseDate = releaseDate;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private String getRawTemperature(){
        return rawTemperature;
    }

    public String getTemperatureToDrink() {
        String[] temperatures = getRawTemperature().split("-");
        if(temperatures.length > 2){
            throw new RuntimeException("Wrong temperature values, more than 2");
        }

        return getCelsius(temperatures);
    }

    protected String getCelsius(String[] temps){
        if (temps.length == 1){
            return temps[0]  + ºC;
        } else{
            return temps[0].trim() + "-" + temps[1].trim() + ºC;
        }
    }

    protected String getFahrenheit(String[] celsiusTemps){

        if (celsiusTemps.length == 1){
            return String.valueOf(
                    TemperatureConversor.celsiusToFarenheit(Float.parseFloat(celsiusTemps[0]))) + ºF;
        } else{
            return TemperatureConversor.celsiusToFarenheit(Float.parseFloat(celsiusTemps[0].trim())) + "-" +
                   TemperatureConversor.celsiusToFarenheit(Float.parseFloat(celsiusTemps[1].trim())) + ºF;
        }
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
        if(getId() != 0){
            values.put(BeerColumns._ID, getId());
        }
        values.put(BeerColumns.BEER_NAME, getName());
        values.put(BeerColumns.BEER_COUNTRY, getCountry());
        values.put(BeerColumns.BEER_ABV, getAbv());
        values.put(BeerColumns.BEER_TEMPERATURE_TO_DRINK, getRawTemperature());
        values.put(BeerColumns.BEER_DRANK, isDrinkedInt());
        values.put(BeerColumns.BEER_RELEASE_DATE, getReleaseDate());
        values.put(BeerColumns.BEER_COLOR, getColor());

        return values;
    }

    public static Beer beerFromCursor(Cursor data){
        Beer beer = new Beer(data.getString(BeerColumns.Index.BEER_NAME),
                data.getString(BeerColumns.Index.BEER_COUNTRY),
                data.getString(BeerColumns.Index.BEER_TEMPERATURE_TO_DRINK),
                data.getFloat(BeerColumns.Index.BEER_ABV),
                data.getString(BeerColumns.Index.BEER_RELEASE_DATE),
                data.getString(BeerColumns.Index.BEER_COLOR));

        beer.id = data.getInt(BeerColumns.Index.BEER_ID);
        beer.setDrinked(data.getInt(BeerColumns.Index.BEER_DRANK));

        return beer;
    }
}
