package br.com.androidzin.brunomateus.beerstodrink.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.List;

import br.com.androidzin.brunomateus.beerstodrink.model.Beer;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerAdapter extends SimpleCursorAdapter {

    public BeerAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BeerAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
