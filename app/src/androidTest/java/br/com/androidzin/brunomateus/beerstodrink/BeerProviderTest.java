package br.com.androidzin.brunomateus.beerstodrink;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import br.com.androidzin.brunomateus.beerstodrink.model.Beer;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract;
import br.com.androidzin.brunomateus.beerstodrink.provider.BeerProvider;
import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.*;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerProviderTest extends ProviderTestCase2<BeerProviderSimple> {

    private MockContentResolver mMockContentResolver;

    private SQLiteDatabase mDb;

    private Uri contentUri = BeerColumns.CONTENT_URI;

    private final Beer[] TEST_SET = {
            new Beer("Victory HpoDevil", "usa", "7", 6.7f),
            new Beer("Eclipse Imperial Stout", "usa", "13", 9.5f),
            new Beer("Motor Oil", "italy", "8 - 10", 6.8f),
            new Beer("Westvleteren Abt 12", "belgium", "12", 10.02f),
            new Beer("Brugse Zot", "belgium", "8", 6f)
    };

    public BeerProviderTest(){
        super(BeerProviderSimple.class, BeerContract.AUTHORITY);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockContentResolver = getMockContentResolver();

        mDb = getProvider().openHelper(getMockContext()).getWritableDatabase();
    }

    public void testInsert(){

        ContentValues values = TEST_SET[0].getContentValues();

        Uri resultUri = mMockContentResolver.insert(contentUri, values);

        assertNotNull(resultUri);

        long id = ContentUris.parseId(resultUri);

        assertTrue(id > 0);
    }

    private void insertTestSet(){
        for(Beer beer : TEST_SET){
            mMockContentResolver.insert(contentUri, beer.getContentValues());
        }
    }

    public void testQueryAll() {

        insertTestSet();

        Cursor c = mMockContentResolver.query(contentUri, BeerColumns.ALL_PROJECTION, null, null, null);

        assertEquals(TEST_SET.length, c.getCount());
    }

    public void testQueryById(){

        insertTestSet();

        Cursor c = mMockContentResolver.query(contentUri, BeerColumns.ALL_PROJECTION, "_id = ?",
                new String[]{"3"}, null);

        assertEquals(1, c.getCount());

        c.moveToFirst();

        String beerName = c.getString(BeerColumns.Index.BEER_NAME);

        assertEquals(TEST_SET[2].getName(), beerName);
    }
}
