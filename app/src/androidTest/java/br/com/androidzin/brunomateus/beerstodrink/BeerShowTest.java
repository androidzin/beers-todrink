package br.com.androidzin.brunomateus.beerstodrink;

import junit.framework.TestCase;

import br.com.androidzin.brunomateus.beerstodrink.model.Beer;

/**
 * Created by bruno on 01/05/15.
 */
public class BeerShowTest extends TestCase{

    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final float ABV = 0.0f;
    public static final String RELEASE_DATE = "01/03/1986";
    public static final String COLOR = "blond";

    public class BeerTest extends Beer{

        private String[] testTemperature;
        public BeerTest(String temperatureToDrink) {
            super(NAME, COUNTRY, temperatureToDrink, ABV, RELEASE_DATE, COLOR);
            testTemperature = temperatureToDrink.split("-");
        }

        public String getCelsiusTest(){
            return this.getCelsius(this.testTemperature);
        }

        public String getFahrenheitTest(){
            return this.getFahrenheit(this.testTemperature);
        }


    }
    public void testOneTemperatureCelsius(){
        BeerTest beer = new BeerTest("10");
        assertEquals("10 ºC", beer.getCelsiusTest());
    }

    public void testCelsiusInterval(){
        BeerTest beer = new BeerTest("10-12");
        assertEquals("10-12 ºC", beer.getCelsiusTest());
        beer = new BeerTest("10 - 12");
        assertEquals("10-12 ºC", beer.getCelsiusTest());
    }

    public void testOneTemperatureFahrenheit(){
        BeerTest beer = new BeerTest("0");
        assertEquals("32.0 ºF", beer.getFahrenheitTest());
    }

    public void testFahrenheitInterval(){
        BeerTest beer = new BeerTest("0-100");
        assertEquals("32.0-212.0 ºF", beer.getFahrenheitTest());
        beer = new BeerTest("0 - 100");
        assertEquals("32.0-212.0 ºF", beer.getFahrenheitTest());
    }

}
