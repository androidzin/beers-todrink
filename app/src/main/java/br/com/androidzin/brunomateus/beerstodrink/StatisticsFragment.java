package br.com.androidzin.brunomateus.beerstodrink;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import br.com.androidzin.brunomateus.beerstodrink.model.Statistics;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;
import br.com.androidzin.brunomateus.beerstodrink.util.InformationFromResource;

@EFragment(R.layout.fragment_beer_statistic)
public class StatisticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemSelectedListener{


    private static final int URL_LOADER = 2;

    @ViewById(R.id.piechart)
    PieChart mPieChart;

    @ViewById(R.id.char_label)
    TextView mCharLabel;

    @ViewById(R.id.chart_options)
    Spinner chartOptions;

    private HashMap<String, Statistics> countries;
    private ArrayList<String> countriesList;

    private String ALL, DRANK, NOT_DRANK;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
        countries = new HashMap<>();
        chartOptions.setOnItemSelectedListener(this);

        ALL = getString(R.string.all_contries);
        DRANK = getString(R.string.drank);
        NOT_DRANK = getString(R.string.not_drank);
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
            } else {
                String countryName = InformationFromResource.getBeerCountry(getActivity(), lastCountry);
                createStatistic(countryName, totalDrankByCountry, totalByCountry);
                totalByCountry = 1;
                totalDrankByCountry = 0;
                lastCountry = country;
            }

            Integer isDrank = data.getInt(BeerContract.BeerColumns.Index.BEER_DRANK);
            if(isDrank == 1){
                totalDrankByCountry++;
                totalDrank++;
            }

        }

        countriesList = new ArrayList<String>(countries.keySet());
        Collections.sort(countriesList);

        createStatistic(ALL, totalDrank, 1001);
        countriesList.add(0, ALL);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                countriesList);

        chartOptions.setAdapter(adapter);

    }

    private void createStatistic(String country, int totalDrankByCountry, int totalByCountry) {
        Statistics statistics = new Statistics(country, totalDrankByCountry, totalByCountry);
        countries.put(statistics.getCountry(), statistics);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!countriesList.isEmpty()){
            Statistics statistics = countries.get(countriesList.get(position));
            mPieChart.clearChart();

            mPieChart.addPieSlice(new PieModel(DRANK, statistics.getNumberOfDrankBeers(),
                    getResources().getColor(R.color.amber_light)));
            mPieChart.addPieSlice(new PieModel(NOT_DRANK,
                    statistics.getNumberOfBeers() - statistics.getNumberOfDrankBeers(),
                    getResources().getColor(R.color.amber)));

            mPieChart.startAnimation();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
