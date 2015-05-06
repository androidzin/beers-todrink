package br.com.androidzin.brunomateus.beerstodrink;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.LinearLayout;


import java.util.ArrayList;

import br.com.androidzin.brunomateus.beerstodrink.adapter.StatisticsAdapter;
import br.com.androidzin.brunomateus.beerstodrink.model.Statistics;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;


public class StatisticsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int URL_LOADER = 2;

    private StatisticsAdapter mAdapter;

    LinearLayout layout;

    private ArrayList<Statistics> bars;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        bars = new ArrayList<Statistics>();
        mAdapter = new StatisticsAdapter(getActivity());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if(id == URL_LOADER){
            loader = new CursorLoader(getActivity(),
                    BeerContract.BeerColumns.CONTENT_URI,
                    BeerContract.BeerColumns.ALL_PROJECTION,
                    null,
                    null,
                    BeerContract.BeerColumns.BEER_COUNTRY);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        processData(data);
        showStatistics();
    }

    private void processData(Cursor data) {
        int totalByCountry = 1;
        int totalDrankByCountry = 0;
        int totalDrank = 0;
        data.moveToFirst();
        String lastCountry = data.getString(BeerContract.BeerColumns.Index.BEER_COUNTRY);
        while(data.moveToNext()){
            String country = data.getString(BeerContract.BeerColumns.Index.BEER_COUNTRY);
            if(lastCountry.equalsIgnoreCase(country)){
                totalByCountry++;
                Integer isDrank = data.getInt(BeerContract.BeerColumns.Index.BEER_DRANK);
                if(isDrank == 1){
                    totalDrankByCountry++;
                    totalDrank++;
                }
            } else {
                createStatistic(lastCountry, totalDrankByCountry, totalByCountry);
                totalByCountry = 1;
                totalDrankByCountry = 0;
                lastCountry = country;
            }

        }
        createStatistic(0, "Total", totalDrank, 1001);

    }

    private void createStatistic(String country, int totalDrankByCountry, int totalByCountry) {
        createStatistic(bars.size(), country, totalDrankByCountry, totalByCountry);
    }

    private void createStatistic(int index, String country, int totalDrankByCountry, int totalByCountry) {
        Log.d(StatisticsFragment.class.getSimpleName(),
                country + " " + totalByCountry + " " + totalDrankByCountry);

        Statistics statistics = new Statistics(country, totalDrankByCountry, totalByCountry);

        bars.add(index, statistics);


    }

    private void showStatistics() {
        mAdapter.setStatistics(bars);
        setListAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
