package ckey.la_gramola;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ruben on 11/02/16.
 */
public class Song_view_holder {
    public final TextView title_view;
    public final TextView artist_view;
    public final TextView album_view;

    public Song_view_holder(View view) {
        title_view = (TextView) view.findViewById(R.id.song_list_title);
        artist_view = (TextView) view.findViewById(R.id.song_list_artist);
        album_view = (TextView) view.findViewById(R.id.song_list_disc);
    }
}
