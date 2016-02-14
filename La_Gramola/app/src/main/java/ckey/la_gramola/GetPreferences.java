package ckey.la_gramola;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by ruben on 13/02/16.
 */
public class GetPreferences {
    public static Boolean getShuffle(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_orden_aleatorio_key),
                new Boolean(context.getString(R.string.pref_orden_aleatorio_default)));
    }

    public static void setShuffle(Context context, Boolean new_value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.pref_orden_aleatorio_key), new_value).apply();
    }

    public static boolean getLyricsAuto(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_descarga_letra_key),
                new Boolean(context.getString(R.string.pref_descarga_letra_default_value)));
    }

    public static void setLyricsAuto(Context context, Boolean new_value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(context.getString(R.string.pref_descarga_letra_key), new_value).apply();
    }

    public static int getColorScheme(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String color =  prefs.getString(context.getString(R.string.pref_color_key),
                context.getString(R.string.pref_color_key));

        if (color.equals("azul")) {
            return R.style.BlueTheme;
        }
        if (color.equals("rojo")) {
            return R.style.RedTheme;
        }
        return R.style.AppTheme;
    }

    public static void setColorScheme (Activity activity) {
        activity.setTheme(getColorScheme(activity.getApplicationContext()));
    }

    public static int getBackgroundColor(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String color =  prefs.getString(context.getString(R.string.pref_color_key),
                context.getString(R.string.pref_color_key));

        if (color.equals("azul")) {
            return R.color.blue_scheme_colorPrimary;
        }
        if (color.equals("rojo")) {
            return R.color.red_scheme_colorPrimary;
        }
        return R.color.green_scheme_colorPrimary;
    }
}
