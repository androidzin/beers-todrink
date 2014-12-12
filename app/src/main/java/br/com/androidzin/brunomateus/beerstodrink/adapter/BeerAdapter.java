package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
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
public class BeerAdapter extends CursorRecyclerViewAdapter<BeerAdapter.ViewHolder> {

    private LayoutInflater mLayoutInflater;

    public BeerAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        setLayoutInflater(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView beerName;
        private TextView beerABV;
        public ViewHolder(View itemView) {
            super(itemView);
            beerName = (TextView) itemView.findViewById(R.id.beer_name);
            beerABV = (TextView) itemView.findViewById(R.id.beer_abv);
        }
    }

    private void setLayoutInflater(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(
                mLayoutInflater.inflate(R.layout.beer_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        viewHolder.beerName.setText(cursor.getString(BeerColumns.Index.BEER_NAME));
        viewHolder.beerABV.setText(cursor.getString(BeerColumns.Index.BEER_ABV));
    }
}


