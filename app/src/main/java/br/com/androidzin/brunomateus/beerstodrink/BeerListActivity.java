package br.com.androidzin.brunomateus.beerstodrink;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

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

    static final String BEER_NAME = "beer_name";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private BeerListFragment mBeerListFragment;

    public enum BeerFilterCriteria  {
        NAME, COUNTRY
    }

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
        mBeerListFragment = (BeerListFragment)
                getSupportFragmentManager().findFragmentById(R.id.beer_list);
        handleIntent(getIntent());

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(BeerContract.BeerColumns.BEER_NAME, query);
            updateBeerList(queryBundle, BeerFilterCriteria.NAME);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.beer_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString(BeerContract.BeerColumns.BEER_NAME, query);
                updateBeerList(queryBundle, BeerFilterCriteria.NAME);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.filter_country:
                showFilterByCountryDialog();
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterByCountryDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        final String[] c = {"brazil", "usa"};
        final boolean[] checked = {false, false};
        b.setTitle(R.string.filter_country)
                .setMultiChoiceItems(c, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checked[which] = isChecked;
                    }
                }).setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> coutries = new ArrayList<String>();
                for (int i = 0; i < checked.length; i++) {
                    if (checked[i]) {
                        coutries.add(c[i]);
                    }
                }
                Bundle queryBundle = new Bundle();
                queryBundle.putStringArrayList(BeerContract.BeerColumns.BEER_COUNTRY,
                        coutries);
                updateBeerList(queryBundle, BeerFilterCriteria.COUNTRY);
            }
        }).show();

    }

    public void updateBeerList(Bundle query, BeerFilterCriteria criteria){
        mBeerListFragment.updateBeerList(query,criteria);
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
        updateDatabase(beer);
        showAnimation(beer);
    }

    private void showAnimation(Beer beer) {
        Intent i = new Intent(getApplicationContext(), BeerDrinkingActivity_.class);
        i.putExtra(BEER_NAME, beer.getName());
        startActivity(i);
    }

    private void updateDatabase(Beer beer) {
        ContentValues values = beer.getContentValues();
        getContentResolver().update(
                Uri.withAppendedPath(
                        BeerContract.BeerColumns.CONTENT_URI,
                        String.valueOf(beer.getId())),
                values,
                null,
                null
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
        updateDatabase(beer);
    }
}
