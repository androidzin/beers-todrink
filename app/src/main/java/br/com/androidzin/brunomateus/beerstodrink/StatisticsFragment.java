package br.com.androidzin.brunomateus.beerstodrink;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;


public class StatisticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final int URL_LOADER = 2;

    LinearLayout layout;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);


        return layout;
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
        int totalByCountry = 0;
        int totalDrankByCountry = 0;
        data.moveToFirst();
        String lastCountry = data.getString(BeerContract.BeerColumns.Index.BEER_COUNTRY);
        while(data.moveToNext()){
            String country = data.getString(BeerContract.BeerColumns.Index.BEER_COUNTRY);
            if(lastCountry.equalsIgnoreCase(country)){
                totalByCountry++;
                Integer isDrank = data.getInt(BeerContract.BeerColumns.Index.BEER_DRANK);
                if(isDrank == 1){
                    totalDrankByCountry++;
                }
            } else {
                addProgressBar(lastCountry, totalDrankByCountry, totalByCountry);
                totalByCountry = 0;
                totalDrankByCountry = 0;
                lastCountry = country;
            }

        }

    }

    private void addProgressBar(String country, int totalDrankByCountry, int totalByCountry) {
        Log.d(StatisticsFragment.class.getSimpleName(),
                country + " " +  totalByCountry + " " + totalDrankByCountry);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView countryName = new TextView(getActivity());
        countryName.setText(country);
        countryName.setLayoutParams(params);

        ProgressBar countryStats = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        countryStats.setMax(totalByCountry);
        countryStats.setProgress(totalDrankByCountry);
        countryStats.setLayoutParams(params);

        layout.addView(countryName);
        layout.addView(countryStats);



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
