package br.com.androidzin.brunomateus.beerstodrink.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.com.androidzin.brunomateus.beerstodrink.R;

/**
 * Created by bruno on 01/05/15.
 */
public class TemperatureConversor {

    public static float celsiusToFarenheit(float celsius){
        return 1.8f * celsius + 32;
    }

    public static final String ºF = " ºF";
    public static final String ºC = " ºC";

    public enum MetricSystems {CELSIUS, FAHRENHEIT};

    public static String getTemperatureToDrink(String rawTemperatures, MetricSystems current) {
        String[] temperatures = rawTemperatures.split("-");
        if(temperatures.length > 2){
            throw new RuntimeException("Wrong temperature values, more than 2");
        }
        if(current == MetricSystems.FAHRENHEIT) {
            return getFahrenheit(temperatures);
        }
        return getCelsius(temperatures);
    }

    private static String getCelsius(String[] temps){
        if (temps.length == 1){
            return String.format("%.1f", Float.parseFloat(temps[0]))  + ºC;
        } else{
            return String.format("%.1f", Float.parseFloat(temps[0]))
                    + "-" + String.format("%.1f", Float.parseFloat(temps[1])) + ºC;
        }
    }

    private static String getFahrenheit(String[] celsiusTemps){

        if (celsiusTemps.length == 1){
            return String.valueOf(
                    String.format("%.1f", TemperatureConversor.celsiusToFarenheit(Float.parseFloat(celsiusTemps[0])))) + ºF;
        } else{
            float temperature1 = TemperatureConversor.celsiusToFarenheit(Float.parseFloat(celsiusTemps[0]));
            float temperature2 = TemperatureConversor.celsiusToFarenheit(Float.parseFloat(celsiusTemps[1]));
            return String.format("%.1f", temperature1) + "-" + String.format("%.1f", temperature2) + ºF;
        }
    }

    public static MetricSystems verifyCurrentTemperatureSystem(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultTemperatureSystem = context.getString(R.string.celsius);
        if(!prefs.getString(context.getString(R.string.pref_temperature),
                defaultTemperatureSystem).equalsIgnoreCase(defaultTemperatureSystem)){
            return MetricSystems.FAHRENHEIT;
        } else {
            return MetricSystems.CELSIUS;
        }
    }
}
