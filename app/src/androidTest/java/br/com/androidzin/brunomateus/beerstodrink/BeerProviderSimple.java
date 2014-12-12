package br.com.androidzin.brunomateus.beerstodrink;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenOpenHelper;

import br.com.androidzin.brunomateus.beerstodrink.provider.BeerProvider;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerProviderSimple extends BeerProvider {

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new ProviGenOpenHelper(getContext(), "beers_to_drink", null, 1, contractClasses());
    }
}
