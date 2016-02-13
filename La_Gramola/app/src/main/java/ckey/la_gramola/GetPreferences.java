package ckey.la_gramola;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ruben on 13/02/16.
 */
public class GetPreferences {
    public static Boolean getShuffle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_orden_aleatorio_key),
                new Boolean(context.getString(R.string.pref_orden_aleatorio_default)));
    }
}
