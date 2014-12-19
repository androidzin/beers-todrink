package br.com.androidzin.brunomateus.beerstodrink.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.tjeannin.provigen.ProviGenProvider;
import com.tjeannin.provigen.helper.TableBuilder;
import com.tjeannin.provigen.helper.TableUpdater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static br.com.androidzin.brunomateus.beerstodrink.provider.BeerContract.*;

/**
 * Created by bruno on 11/12/14.
 */
public class BeerProvider extends ProviGenProvider {

    public static final String INSERT_STATEMENT = "INSERT INTO beer ( "
            + BeerColumns.BEER_NAME + ", " + BeerColumns.BEER_COUNTRY + ", "
            + BeerColumns.BEER_DRINKED + ", " + BeerColumns.BEER_TEMPERATURE_TO_DRINK + ", "
            + BeerColumns.BEER_ABV + ", " + BeerColumns.BEER_RELEASE_DATE + ", "
            + BeerColumns.BEER_COLOR +  " ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static Class[] contracts = new Class[]{BeerColumns.class};
    private static int DB_VERSION = 1;

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new SQLiteOpenHelper(context, "beers_to_drink", null, DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                new TableBuilder(BeerColumns.class).createTable(db);

                BufferedReader reader = getBufferedReaderFromCsvFile();
                if(reader != null) {
                    parseCsvAndInsert(db, reader);
                }
            }


            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                TableUpdater.addMissingColumns(db, BeerColumns.class);
            }
        };
    }

    private BufferedReader getBufferedReaderFromCsvFile() {
        InputStream fileStream = null;
        try {
            fileStream = getContext().getAssets().open("beers_to_drink.csv");

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(BeerProvider.class.getSimpleName(), "Error: couldn't  open file");
        }

        if(fileStream != null) {
            return new BufferedReader(new InputStreamReader(fileStream, Charset.forName("UTF-8")));
        }
        return null;
    }

    private void bindField(SQLiteStatement statement, String[] columns, int fieldIndex) {
        if(fieldIndex <= columns.length){
            statement.bindString(fieldIndex, columns[fieldIndex - 1]);
        }
    }

    private void parseCsvAndInsert(SQLiteDatabase db, BufferedReader reader) {
        String currentLine = null;
        try {
            SQLiteStatement statement = db.compileStatement(INSERT_STATEMENT);
            db.beginTransaction();
            while((currentLine = reader.readLine()) != null){
                String[] columns = currentLine.split(",");

                bindField(statement, columns, BeerColumns.Index.BEER_NAME);
                bindField(statement, columns, BeerColumns.Index.BEER_COUNTRY);
                bindField(statement, columns, BeerColumns.Index.BEER_DRINKED);
                bindField(statement, columns, BeerColumns.Index.BEER_TEMPERATURE_TO_DRINK);
                bindField(statement, columns, BeerColumns.Index.BEER_ABV);
                bindField(statement, columns, BeerColumns.Index.BEER_RELEASE_DATE);
                bindField(statement, columns, BeerColumns.Index.BEER_COLOR);

                statement.executeInsert();
                statement.clearBindings();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class[] contractClasses() {
        return contracts;
    }
}
