package br.com.androidzin.brunomateus.beerstodrink;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import br.com.androidzin.brunomateus.beerstodrink.dummy.DummyContent;

/**
 * A fragment representing a single Beer detail screen.
 * This fragment is either contained in a {@link BeerListActivity}
 * in two-pane mode (on tablets) or a {@link BeerDetailActivity}
 * on handsets.
 */
@EFragment(R.layout.fragment_beer_detail)
public class BeerDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

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

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }


    @AfterViews
    void calledAfterInjection() {
        Resources resources = getResources();
        beerName.setText(mItem.content);
        beerAbout.setText(resources.getText(R.string.beer_about));
        beerImage.setImageDrawable(resources.getDrawable(R.drawable.duvel));
        beerCountry.setImageDrawable(resources.getDrawable(R.drawable.belgium_flag));

    }
}