package br.com.androidzin.brunomateus.beerstodrink;

import junit.framework.TestCase;

import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor;

import static br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor.MetricSystems;
import static br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor.ºC;
import static br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor.ºF;

/**
 * Created by bruno on 01/05/15.
 */
public class BeerShowTest extends TestCase{

    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final float ABV = 0.0f;
    public static final String RELEASE_DATE = "01/03/1986";
    public static final String COLOR = "blond";

   private class BeerTest extends Beer{

        public BeerTest(String temperatureToDrink) {
            super(NAME, COUNTRY, temperatureToDrink, ABV, RELEASE_DATE, COLOR);
        }

        public String getCelsiusTest(){
            return TemperatureConversor.getTemperatureToDrink(getRawTemperature(), MetricSystems.CELSIUS);
        }

        public String getFahrenheitTest(){
            return TemperatureConversor.getTemperatureToDrink(getRawTemperature(), MetricSystems.FAHRENHEIT);
        }


    }
    public void testOneTemperatureCelsius(){
        BeerTest beer = new BeerTest("10");
        assertEquals("10,0"+ºC, beer.getCelsiusTest());
    }

    public void testCelsiusInterval(){
        BeerTest beer = new BeerTest("10-12");
        assertEquals("10,0-12,0"+ºC, beer.getCelsiusTest());
        beer = new BeerTest("10 - 12");
        assertEquals("10,0-12,0"+ºC, beer.getCelsiusTest());
    }

    public void testOneTemperatureFahrenheit(){
        BeerTest beer = new BeerTest("0");
        assertEquals("32,0"+ºF, beer.getFahrenheitTest());
    }

    public void testFahrenheitInterval(){
        BeerTest beer = new BeerTest("0-100");
        assertEquals("32,0-212,0"+ºF, beer.getFahrenheitTest());
        beer = new BeerTest("0 - 100");
        assertEquals("32,0-212,0"+ºF, beer.getFahrenheitTest());
    }

}
