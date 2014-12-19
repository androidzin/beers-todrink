package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.androidzin.brunomateus.beerstodrink.R;
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;

/**
 * Created by bruno on 18/12/14.
 */
public class BeerViewHolder extends RecyclerView.ViewHolder {

    private TextView beerName;
    private TextView beerReleaseDate;
    private TextView beerCountry;
    private TextView beerABV;
    private TextView beerTemperatureToDrink;
    private TextView drinked;
    private Beer beer;
    private Context mContext;

    public BeerViewHolder(Context context, View itemView, final BeerAdapter.OnBeerCardClickListener listener, final Beer.Drinkable drinkable) {
        super(itemView);
        beerName = (TextView) itemView.findViewById(R.id.beer_name);
        beerReleaseDate = (TextView) itemView.findViewById(R.id.beer_release_date);
        beerCountry = (TextView) itemView.findViewById(R.id.beer_country);
        beerABV = (TextView) itemView.findViewById(R.id.beer_abv);
        beerTemperatureToDrink = (TextView) itemView.findViewById(R.id.beer_temperature_to_drink);
        drinked = (TextView) itemView.findViewById(R.id.drinked_button);
        mContext = context;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onBeerSeleteced(String.valueOf(beer.getId()));
                }
            }
        });

        itemView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!beer.isDrinked()) {
                    drinkable.onDrink(beer);
                } else {
                    drinkable.onNotDrank(beer);
                }
                setDrinked();
                return true;
            }
        });
    }

    private void setBeerName(String beerName) {
        this.beerName.setText(beerName);
    }

    private void setBeerReleaseDate(String releaseDate){
        this.beerReleaseDate.setText(releaseDate);
    }

    private void setBeerCountry(String country){
        this.beerCountry.setText(country);
    }

    private void setBeerABV(String beerABV) {
        this.beerABV.setText(beerABV);
    }

    private void setBeerTemperatureToDrink(String temperatureToDrink){
        this.beerTemperatureToDrink.setText(temperatureToDrink);
    }

    public void bindBeer(Cursor cursor) {
        beer = Beer.beerFromCursor(cursor);
        Resources resources = mContext.getResources();

        setBeerName(beer.getName());
        setBeerReleaseDate(
                resources.getString(R.string.label_release_date) +
                beer.getReleaseDate()
        );
        setBeerCountry(
                resources.getString(R.string.label_country) +
                beer.getCountry()
        );

        setBeerABV(
                resources.getString(R.string.label_abv) +
                beer.getAbv()
        );

        setBeerTemperatureToDrink(
                resources.getString(R.string.label_temperature_to_drink) +
                beer.getTemperatureToDrink()
        );

        setDrinked();
    }

    private void setDrinked() {
        if(beer.isDrinked()){
            drinked.setVisibility(View.VISIBLE);
        } else {
            drinked.setVisibility(View.GONE);
        }
    }
}