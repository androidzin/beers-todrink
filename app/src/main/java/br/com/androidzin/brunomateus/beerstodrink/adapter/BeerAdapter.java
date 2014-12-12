package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
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
public class BeerAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;

    public BeerAdapter(Context context, Cursor c) {
        super(context, c);
        getLayoutInflater(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BeerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        getLayoutInflater(context);
    }

    private void getLayoutInflater(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.beer_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView beerName = (TextView) view.findViewById(R.id.beer_name);
        TextView beerABV = (TextView) view.findViewById(R.id.beer_abv);

        beerName.setText(cursor.getString(BeerColumns.Index.BEER_NAME));
        beerABV.setText(cursor.getString(BeerColumns.Index.BEER_ABV));
    }
}


