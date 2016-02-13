package ckey.la_gramola;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by Fernando on 11/02/2016.
 */
public class ObtenerLyrics extends AsyncTask<String, String, String> {

    private final String LOG_TAG = ObtenerLyrics.class.getSimpleName();
    private final Context mContext;
    private final View mView;
    private String lyrics;

    public ObtenerLyrics(Context context, View view) {
        mContext = context;
        mView = view;
    }

    public String getLyrics (){
        return lyrics;
    }

    private String getIDFromJson(String JsonStr1)
            throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        String id = null;

        try {
            JSONObject trackJson = new JSONObject(JsonStr1);
            JSONObject mensaje = trackJson.getJSONObject("message");
            JSONObject cuerpo = mensaje.getJSONObject("body");
            JSONObject cancion = cuerpo.getJSONObject("track");
            id = cancion.getString("track_id");


            Log.d(LOG_TAG, "Buscar la ID Completada. " + id + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return id;
    }

    private String getLyricsFromJson(String JsonStr2)
            throws JSONException {

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        String lyrics = null;
        try {
            JSONObject trackJson = new JSONObject(JsonStr2);
            JSONObject mensaje = trackJson.getJSONObject("message");
            JSONObject cuerpo = mensaje.getJSONObject("body");
            JSONObject cancion = cuerpo.getJSONObject("lyrics");
            lyrics = cancion.getString("lyrics_body");


            Log.d(LOG_TAG, "Buscar la LETRA Completada. " + lyrics + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return lyrics;
    }

    @Override
    protected String doInBackground(String... params) {

        //----------------------------------------------------
        //Recuperamos el id de la cancion
        //----------------------------------------------------


        //Comprobamos que se pasan argumentos
        if (params.length == 0) {
            return null;
        }

        String titulo = params[0];
        String autor = params[1];



        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String JsonStr1 = null;
        String format = "json";
        String id = null;


        try {
            final String FORECAST_BASE_URL1 =
                    "http://api.musixmatch.com/ws/1.1/matcher.track.get?";
            final String APPID_PARAM = "apikey";
            final String ARTIST_PARAM = "q_artist";
            final String TRACK_PARAM = "q_track";
            final String FORMAT_PARAM = "format";


            Uri builtUri = Uri.parse(FORECAST_BASE_URL1).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "391b1dad56f735d843709f7be30a0749")
                    .appendQueryParameter(ARTIST_PARAM, autor)
                    .appendQueryParameter(TRACK_PARAM, titulo)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            JsonStr1 = buffer.toString();
            id = getIDFromJson(JsonStr1);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        //----------------------------------------------------
        //Recuperamos la letra
        //----------------------------------------------------

        urlConnection = null;
        reader = null;


        String JsonStr2 = null;


        try {

            final String FORECAST_BASE_URL1 =
                    "http://api.musixmatch.com/ws/1.1/track.lyrics.get?";
            final String APPID_PARAM = "apikey";
            final String TRACK_ID = "track_id";
            final String FORMAT_PARAM = "format";


            Uri builtUri = Uri.parse(FORECAST_BASE_URL1).buildUpon()
                    .appendQueryParameter(APPID_PARAM, "391b1dad56f735d843709f7be30a0749")
                    .appendQueryParameter(TRACK_ID, id)
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .build();

            URL url = new URL(builtUri.toString());


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }
            JsonStr2 = buffer.toString();
            lyrics = getLyricsFromJson(JsonStr2);


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        if (lyrics == "" || lyrics == null) {
            lyrics = "Lo sentimos :( \n" +
                    "No hemos encontrado letra para tu canción. Intenta: \n" +
                    "   - Revisar que el título sea el original.\n" +
                    "   - Proporcionar datos como el autor de la misma.\n" +
                    "   - Comprobar que realmente la canción tiene letra.\n";
        }
        return lyrics;
    }
    @Override
    protected void onPostExecute(String lyrics) {
        TextView t = (TextView) mView.findViewById(R.id.lyrics);
        t.setText(lyrics.split("\\*\\*\\*\\*\\*\\*\\*")[0]);
    }

}
