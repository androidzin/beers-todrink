package br.com.androidzin.brunomateus.beerstodrink.provider;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by brunomateus on 11/12/14.
 */
public final class BeerContract  {

    public static final String AUTHORITY = "br.com.androidzin.brunomateus.beerstodrink";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String QUERY_BY_ID = "_id = ?";

    public static final String DRANK_BEERS = "drank = 1";

    public static final String NOT_DRANK_BEERS = "drank = 0";

    public static final String BEER_COUNTRIES = "1 GROUP by country";

    public interface BeerColumns extends ProviGenBaseContract {
        @Column(Column.Type.TEXT)
        public static final String BEER_NAME = "name";

        @Column(Column.Type.TEXT)
        public static final String BEER_COUNTRY = "country";

        @Column(Column.Type.INTEGER)
        public static final String BEER_DRANK = "drank";

        @Column(Column.Type.TEXT)
        public static final String BEER_TEMPERATURE_TO_DRINK = "temperature";

        @Column(Column.Type.REAL)
        public static final String BEER_ABV = "abv";

        @Column(Column.Type.TEXT)
        public static final String BEER_RELEASE_DATE = "release_date";

        @Column(Column.Type.TEXT)
        public static final String BEER_COLOR = "color";

        @ContentUri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BeerContract.CONTENT_URI, "beer");

        public static final String ALL_PROJECTION[] =  {_ID, BEER_NAME, BEER_COUNTRY, BEER_DRANK,
                BEER_TEMPERATURE_TO_DRINK, BEER_ABV, BEER_RELEASE_DATE, BEER_COLOR};

        public interface Index {
            int BEER_ID = 0;
            int BEER_NAME = 1;
            int BEER_COUNTRY = 2;
            int BEER_DRANK = 3;
            int BEER_TEMPERATURE_TO_DRINK = 4;
            int BEER_ABV = 5;
            int BEER_RELEASE_DATE = 6;
            int BEER_COLOR = 7;
        }


    }


}
