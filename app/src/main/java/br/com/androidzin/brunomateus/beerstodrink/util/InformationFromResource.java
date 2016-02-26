package br.com.androidzin.brunomateus.beerstodrink.util;

import android.content.Context;

/**
 * Created by bruno on 19/02/16.
 */
public class InformationFromResource {

    public static String getBeerCountry(Context context, String identifier){
        return context.getString(context.getResources().getIdentifier(identifier, "string",
                context.getPackageName()));
    }
}
