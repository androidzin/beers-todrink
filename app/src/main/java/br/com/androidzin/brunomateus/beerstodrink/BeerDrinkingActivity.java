package br.com.androidzin.brunomateus.beerstodrink;

import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;

@EActivity(R.layout.activity_beer_drinking)
public class BeerDrinkingActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int URL_LOADER = 3;

    @ViewById(R.id.drinking_animation)
    ImageView drinkingAnimation;

    @ViewById(R.id.drinking_message)
    TextView drinkingMessage;

    @ViewById(R.id.beer_name)
    TextView beerName;

    private String beer;
    private String beerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beer = getIntent().getStringExtra(BeerListActivity.BEER_NAME);
        beerColor = getIntent().getStringExtra(BeerListActivity.BEER_COLOR);
        getSupportLoaderManager().initLoader(URL_LOADER, null, this);
    }

    @AfterViews
    public void setBeerName(){

        beerName.setText(beer);

    }

    @AfterViews
    public void startAnimation(){
        final int animationId = getResources().getIdentifier("drinking_"+beerColor,
                "anim", getPackageName());
        drinkingAnimation.setBackgroundResource(animationId);
        AnimationDrawable animation = (AnimationDrawable) drinkingAnimation.getBackground();
        animation.setOneShot(true);
        animation.start();
        showStatus();
    }

    @UiThread(delay=2000)
    public void showStatus(){
        drinkingMessage.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if(id == URL_LOADER){
            loader = new CursorLoader(this,
                    BeerContract.BeerColumns.CONTENT_URI,
                    null,
                    BeerContract.NOT_DRANK_BEERS,
                    null,
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int remaingBeers = data.getCount();
        drinkingMessage.setText(getResources().getQuantityString(
                R.plurals.one_more_congratulations,
                remaingBeers, remaingBeers));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
