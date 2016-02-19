package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import br.com.androidzin.brunomateus.beerstodrink.R;
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor;

import static br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor.MetricSystems;

/**
 * Created by bruno on 18/12/14.
 */
public class BeerViewHolder extends RecyclerView.ViewHolder {

    private MetricSystems currentSystem;
    private TextView beerName;
    private TextView beerReleaseDate;
    //private TextView beerCountry;
    private ImageView beerFlag;
    private TextView beerABV;
    private TextView beerTemperatureToDrink;
    private ImageView beerImage;
    private Beer beer;
    private Context mContext;

    private static HashMap<String, Drawable> beerImages = new HashMap<>();

    public BeerViewHolder(Context context, View itemView, final BeerAdapter.OnBeerCardClickListener listener, final Beer.Drinkable drinkable) {
        super(itemView);
        beerName = (TextView) itemView.findViewById(R.id.beer_name);
        beerReleaseDate = (TextView) itemView.findViewById(R.id.beer_release_date);
        //beerCountry = (TextView) itemView.findViewById(R.id.beer_country);
        beerFlag = (ImageView) itemView.findViewById(R.id.beer_country);
        beerABV = (TextView) itemView.findViewById(R.id.beer_abv);
        beerTemperatureToDrink = (TextView) itemView.findViewById(R.id.beer_temperature_to_drink);
        beerImage = (ImageView) itemView.findViewById(R.id.beer_image);
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

        currentSystem = TemperatureConversor.verifyCurrentTemperatureSystem(mContext);

    }



    private void setBeerName(String beerName) {
        this.beerName.setText(beerName);
    }

    private void setBeerReleaseDate(String releaseDate){
        this.beerReleaseDate.setText(releaseDate);
    }

    /*private void setBeerCountry(String country){
        this.beerCountry.setText(country);
    }*/

    private void setBeerFlag(String country){
        final int id = mContext.getResources().getIdentifier(country+"_small",
                "drawable",mContext.getPackageName());
        if(id != 0) {
            beerFlag.setImageResource(id);
        }
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
        /*
        setBeerCountry(
                resources.getString(R.string.label_country) +
                beer.getCountry()
        );*/

        setBeerFlag(beer.getCountry());

        setBeerABV(
                resources.getString(R.string.label_abv) +
                beer.getAbv()
        );

        setBeerTemperatureToDrink(
                resources.getString(R.string.label_temperature_to_drink) +
                TemperatureConversor.getTemperatureToDrink(beer.getRawTemperature(), currentSystem)
        );

        setDrinked();
    }

    private void setDrinked() {
       beerImage.setImageDrawable(BeerViewHolder.getIcon(mContext, beer));
    }

    private static Drawable getIcon(Context context, Beer beer) {
        String beerColorImageId = null;
        if(beer.isDrinked()){
            beerColorImageId = "empty";
        } else {
            beerColorImageId = beer.getColor();
            //TODO: Remover
            if(beerColorImageId.equals("0")) beerColorImageId = "beer_sample";
        }
        Log.d("icon", beer.getName() + beerColorImageId);
        Drawable beerIcon = beerImages.get(beerColorImageId);
        if(beerIcon == null){
            final int imageIdentifier = context.getResources().getIdentifier(beerColorImageId,
                    "drawable", context.getPackageName());
            beerIcon = context.getResources().getDrawable(imageIdentifier);
            beerImages.put(beerColorImageId, beerIcon);
        }
        return beerIcon;

    }
}