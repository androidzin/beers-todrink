package br.com.androidzin.brunomateus.beerstodrink.model;

/**
 * Created by bruno on 05/05/15.
 */
public class Statistics {

    private String country;
    private int numberOfBeers;
    private int numberOfDrankBeers;

    public Statistics(String country, int numberOfDrankBeers, int numberOfBeers) {
        this.country = country;
        this.numberOfDrankBeers = numberOfDrankBeers;
        this.numberOfBeers = numberOfBeers;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getNumberOfBeers() {
        return numberOfBeers;
    }

    public void setNumberOfBeers(int numberOfBeers) {
        this.numberOfBeers = numberOfBeers;
    }

    public int getNumberOfDrankBeers() {
        return numberOfDrankBeers;
    }

    public void setNumberOfDrankBeers(int numberOfDrankBeers) {
        this.numberOfDrankBeers = numberOfDrankBeers;
    }
}
