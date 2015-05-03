package br.com.androidzin.brunomateus.beerstodrink;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.androidzin.brunomateus.beerstodrink.adapter.BeerAdapter;
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;
import br.com.androidzin.brunomateus.beerstodrink.util.FilterBuilder;
import br.com.androidzin.brunomateus.beerstodrink.util.TemperatureConversor;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;

/**
 * A list fragment representing a list of Beers. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link BeerDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the
 * {@link BeerAdapter.OnBeerCardClickListener}
 * interface.
 */
@EFragment(R.layout.fragment_beer_list)
public class BeerListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        BeerFilterCountryDialog.FilterCountryListener{

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final int URL_LOADER = 1;
    static final String BEER_NAME = "beer_name";
    public static final String DIALOG_FILTER_COUNTRY = "BeerFilterCountry";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    @ViewById(R.id.beer_recycler_list)
    RecyclerView mRecyclerView;

    private BeerAdapter mAdapter;

    private TemperatureConversor.MetricSystems current;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private BeerAdapter.OnBeerCardClickListener beerClickCallbacks = sBeerClickCallback;

    private Beer.Drinkable drinkableCallbacks = sDrinkableCallbacks;

    /**
     * A dummy implementation of the {@link BeerAdapter.OnBeerCardClickListener}
     * interface that does nothing. Used only when this fragment is not attached to an activity.
     */
    private static BeerAdapter.OnBeerCardClickListener sBeerClickCallback =
            new BeerAdapter.OnBeerCardClickListener() {

                @Override
                public void onBeerSeleteced(String beerId) {

                }
            };

    private static Beer.Drinkable sDrinkableCallbacks = new Beer.Drinkable() {
        @Override
        public void onDrink(Beer beer) {

        }

        @Override
        public void onNotDrank(Beer beer) {

        }
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if(id == URL_LOADER){
            String selection = FilterBuilder.getQuery(args);
            loader = new CursorLoader(getActivity(),
                    BeerColumns.CONTENT_URI,
                    BeerColumns.ALL_PROJECTION,
                    selection,
                    null,
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            mAdapter.changeCursor(data);
        } else {
            mAdapter.swapCursor(data);
        }
        mAdapter.setOnBeerCardListener(beerClickCallbacks);
        mAdapter.setDrinkableListener(drinkableCallbacks);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeerListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(savedInstanceState != null){
            BeerFilterCountryDialog countryFilterDialog =
                    (BeerFilterCountryDialog) getFragmentManager().findFragmentByTag(DIALOG_FILTER_COUNTRY);
            if(countryFilterDialog != null){
                countryFilterDialog.setListener(this);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TemperatureConversor.MetricSystems newOne = TemperatureConversor.verifyCurrentTemperatureSystem(getActivity());
        if(current != newOne){
            current = newOne;
            mRecyclerView.setAdapter(null);
            mRecyclerView.setAdapter(mAdapter);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the options menu from XML
        inflater.inflate(R.menu.filter_menu, menu);
        updateDrinkFilterMenu(menu.findItem(R.id.filter_drink));

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.beer_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Bundle queryBundle = new Bundle();
                if(!query.isEmpty()) {
                    queryBundle.putString(BeerContract.BeerColumns.BEER_NAME, query);
                }
                updateBeerList(queryBundle, BeerListActivity.BeerFilterCriteria.NAME);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.filter_country:
                showFilterByCountryDialog();
                break;
            case R.id.filter_drink:
                updateFilterDrink();
                updateDrinkFilterMenu(item);
                break;
            case R.id.settings_menu:
                Intent i = new Intent(getActivity(), SettingsActivity_.class);
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
                    i.setClass(getActivity(), SettingsOldActivity.class);
                }
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateDrinkFilterMenu(MenuItem item) {
        if(FilterBuilder.showAll()){
            item.setTitle(getString(R.string.show_all));
        } else {
            item.setTitle(getString(R.string.not_drank));
        }
    }


    private void updateFilterDrink() {
        updateBeerList(new Bundle(), BeerListActivity.BeerFilterCriteria.DRINK);

    }

    private void showFilterByCountryDialog() {
        BeerFilterCountryDialog dialog = new BeerFilterCountryDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(), DIALOG_FILTER_COUNTRY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(URL_LOADER, null, this);
        current = TemperatureConversor.verifyCurrentTemperatureSystem(getActivity());
    }

    @AfterViews
    public void configureReclycleList(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BeerAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void updateBeerList(Bundle queryBundle, BeerListActivity.BeerFilterCriteria criteria){
        queryBundle.putInt("criteria", criteria.ordinal());
        getLoaderManager().restartLoader(URL_LOADER, queryBundle, this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof BeerAdapter.OnBeerCardClickListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        beerClickCallbacks = (BeerAdapter.OnBeerCardClickListener) activity;

        if (!(activity instanceof Beer.Drinkable)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        drinkableCallbacks = (Beer.Drinkable) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        beerClickCallbacks = sBeerClickCallback;
        drinkableCallbacks = sDrinkableCallbacks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onFilter(Bundle queryBundler) {
        updateBeerList(queryBundler, BeerListActivity.BeerFilterCriteria.COUNTRY);
    }
}
