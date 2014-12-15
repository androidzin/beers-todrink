package br.com.androidzin.brunomateus.beerstodrink;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.BeerColumns;


/**
 * A fragment representing a single Beer detail screen.
 * This fragment is either contained in a {@link BeerListActivity}
 * in two-pane mode (on tablets) or a {@link BeerDetailActivity}
 * on handsets.
 */
@EFragment(R.layout.fragment_beer_detail)
public class BeerDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String BEER_ID = "beer_id";
    private static final int URL_LOADER = 2;

    private Beer mItem;

    @ViewById(R.id.beer_name)
    TextView beerName;

    @ViewById(R.id.beer_image)
    ImageView beerImage;

    @ViewById(R.id.beer_country_image)
    ImageView beerCountry;

    @ViewById(R.id.beer_about)
    TextView beerAbout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(BEER_ID)) {
            getLoaderManager().initLoader(URL_LOADER, getArguments(), this);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if(id == URL_LOADER){
            loader = new CursorLoader(getActivity(),
                    Uri.withAppendedPath(BeerColumns.CONTENT_URI,
                            args.getString(BEER_ID)),
                    BeerColumns.ALL_PROJECTION,
                    null,
                    null,
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mItem = Beer.beerFromCursor(data);

        Resources resources = getResources();
        beerName.setText(mItem.getName());
        beerAbout.setText(mItem.getCountry().concat(" ") + mItem.getAbv());
        beerImage.setImageDrawable(resources.getDrawable(R.drawable.duvel));
        beerCountry.setImageDrawable(resources.getDrawable(R.drawable.belgium_flag));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}