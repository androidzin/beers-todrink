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
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerAdapter extends CursorRecyclerViewAdapter<BeerViewHolder>{

    private LayoutInflater mLayoutInflater;
    private OnBeerCardClickListener mBeerListener;
    private Beer.Drinkable mDrinkableListener;
    private Context mContext;

    public BeerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        setLayoutInflater(context);
        mContext = context;
    }

    public void setOnBeerCardListener(OnBeerCardClickListener listener){
        this.mBeerListener = listener;
    }

    public void setDrinkableListener(Beer.Drinkable drinkableCallbacks) {
        this.mDrinkableListener = drinkableCallbacks;
    }

    public interface OnBeerCardClickListener {

        public void onBeerSeleteced(String beerId);
    }

    private void setLayoutInflater(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public BeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = mLayoutInflater.inflate(R.layout.beer_list_item, parent, false);
        BeerViewHolder holder = new BeerViewHolder(mContext, cardView, mBeerListener, mDrinkableListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BeerViewHolder viewHolder, Cursor cursor) {
        viewHolder.bindBeer(cursor);
    }

}


