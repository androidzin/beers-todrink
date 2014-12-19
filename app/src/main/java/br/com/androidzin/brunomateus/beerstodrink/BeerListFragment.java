package br.com.androidzin.brunomateus.beerstodrink;

import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.androidzin.brunomateus.beerstodrink.adapter.BeerAdapter;
import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
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
public class BeerListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final int URL_LOADER = 1;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private RecyclerView mRecyclerView;

    private BeerAdapter mAdapter;

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
            loader = new CursorLoader(getActivity(),
                    BeerColumns.CONTENT_URI,
                    BeerColumns.ALL_PROJECTION,
                    null,
                    null,
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beer_list, container, true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.beer_recycler_list);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BeerAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof BeerAdapter.OnBeerCardClickListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        beerClickCallbacks = (BeerAdapter.OnBeerCardClickListener) activity;
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

}
