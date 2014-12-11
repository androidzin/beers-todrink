package br.com.androidzin.brunomateus.beerstodrink.provider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.*;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerProvider extends ProviGenProvider {

    private static Class[] contracts = new Class[]{BeerColumns.class};
    private static int DB_VERSION = 1;

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new ProviGenOpenHelper(context, "beers_to_drink", null, DB_VERSION, contractClasses());
    }

    @Override
    public Class[] contractClasses() {
        return contracts;
    }
}
