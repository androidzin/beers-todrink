package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.androidzin.brunomateus.beerstodrink.R;
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerAdapter extends CursorRecyclerViewAdapter<BeerAdapter.ViewHolder>{

    private LayoutInflater mLayoutInflater;
    private OnBeerCardClickListener mBeerListener;

    public BeerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        setLayoutInflater(context);
    }

    public void setOnBeerCardListener(OnBeerCardClickListener listener){
        this.mBeerListener = listener;
    }

    public interface OnBeerCardClickListener {

        public void onBeerSeleteced(String beerId);
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView beerName;
        private TextView beerABV;
        private String beerId;
        public ViewHolder(View itemView, final OnBeerCardClickListener listener) {
            super(itemView);
            beerName = (TextView) itemView.findViewById(R.id.beer_name);
            beerABV = (TextView) itemView.findViewById(R.id.beer_abv);
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

        public void setBeerABV(String beerABV) {
            this.beerABV.setText(beerABV);
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
        viewHolder.setBeerABV(cursor.getString(BeerColumns.Index.BEER_ABV));
    }
}


