package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.androidzin.brunomateus.beerstodrink.R;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerAdapter extends CursorRecyclerViewAdapter<BeerAdapter.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private OnBeerCardClickListener mBeerListener;
    private Resources mResources;

    public BeerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        setLayoutInflater(context);
        mResources = context.getResources();
    }

    public void setOnBeerCardListener(OnBeerCardClickListener listener){
        this.mBeerListener = listener;
    }

    public interface OnBeerCardClickListener {

        public void onBeerSeleteced(String beerId);
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        private String beerId;
        private TextView beerName;
        private TextView beerReleaseDate;
        private TextView beerCountry;
        private TextView beerABV;
        private TextView beerTemperatureToDrink;
        public ViewHolder(View itemView, final OnBeerCardClickListener listener) {
            super(itemView);
            beerName = (TextView) itemView.findViewById(R.id.beer_name);
            beerReleaseDate = (TextView) itemView.findViewById(R.id.beer_release_date);
            beerCountry = (TextView) itemView.findViewById(R.id.beer_country);
            beerABV = (TextView) itemView.findViewById(R.id.beer_abv);
            beerTemperatureToDrink = (TextView) itemView.findViewById(R.id.beer_temperature_to_drink);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onBeerSeleteced(beerId);
                    }
                }
            });
        }

        public void setBeerID(String beerId) {
            this.beerId = beerId;
        }

        public void setBeerName(String beerName) {
            this.beerName.setText(beerName);
        }

        public void setBeerReleaseDate(String releaseDate){
            this.beerReleaseDate.setText(releaseDate);
        }

        public void setBeerCountry(String country){
            this.beerCountry.setText(country);
        }

        public void setBeerABV(String beerABV) {
            this.beerABV.setText(beerABV);
        }

        public void setBeerTemperatureToDrink(String temperatureToDrink){
            this.beerTemperatureToDrink.setText(temperatureToDrink);
        }
    }

    private void setLayoutInflater(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = mLayoutInflater.inflate(R.layout.beer_list_item, parent, false);
        ViewHolder holder = new ViewHolder(cardView, mBeerListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final Cursor cursor) {
        viewHolder.setBeerID(cursor.getString(BeerColumns.Index.BEER_ID));
        viewHolder.setBeerName(cursor.getString(BeerColumns.Index.BEER_NAME));

        viewHolder.setBeerReleaseDate(
                mResources.getString(R.string.label_release_date) +
                cursor.getString(BeerColumns.Index.BEER_RELEASE_DATE)
        );

        viewHolder.setBeerCountry(
                mResources.getString(R.string.label_country) +
                        cursor.getString(BeerColumns.Index.BEER_COUNTRY)
        );

        viewHolder.setBeerABV(
                mResources.getString(R.string.label_abv) +
                cursor.getString(BeerColumns.Index.BEER_ABV)
        );

        viewHolder.setBeerTemperatureToDrink(
                mResources.getString(R.string.label_temperature_to_drink) +
                        cursor.getString(BeerColumns.Index.BEER_TEMPERATURE_TO_DRINK)
        );
    }
}


