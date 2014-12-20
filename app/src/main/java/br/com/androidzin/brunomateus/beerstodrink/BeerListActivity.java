package br.com.androidzin.brunomateus.beerstodrink;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import br.com.androidzin.brunomateus.beerstodrink.adapter.BeerAdapter;
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;


/**
 * An activity representing a list of Beers. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BeerDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link BeerListFragment} and the item details
 * (if present) is a {@link BeerDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link BeerAdapter.OnBeerCardClickListener} interface
 * to listen for item selections.
 */
public class BeerListActivity extends ActionBarActivity
        implements BeerAdapter.OnBeerCardClickListener, Beer.Drinkable,
        BeerDialogConfirmation.BeerDialogConfirmartionListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        if (findViewById(R.id.beer_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    public void onBeerSeleteced(String beerId) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(BeerDetailFragment.BEER_ID, beerId);
            BeerDetailFragment fragment = new BeerDetailFragment_();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.beer_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, BeerDetailActivity.class);
            detailIntent.putExtra(BeerDetailFragment.BEER_ID, beerId);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onDrink(Beer beer) {
        beer.setDrinked(true);
        Log.d(getClass().getSimpleName(), beer.getName() + "was drinked");
        ContentValues values = beer.getContentValues();
        updateBeer(beer);
    }

    private void updateBeer(Beer beer) {
        ContentValues values = beer.getContentValues();
        getContentResolver().update(
                BeerContract.BeerColumns.CONTENT_URI,
                values,
                BeerContract.QUERY_BY_ID,
                new String[]{String.valueOf(beer.getId())}
        );
    }

    @Override
    public void onNotDrank(Beer beer) {
        BeerDialogConfirmation dialog = new BeerDialogConfirmation();
        dialog.setBeer(beer);
        dialog.show(getSupportFragmentManager(), "BeerConfirmation");
    }

    @Override
    public void onDialogPositiveClick(Beer beer) {
        beer.setDrinked(false);
        Log.d(getClass().getSimpleName(), beer.getName() + "was not drinked");
        updateBeer(beer);
    }
}
