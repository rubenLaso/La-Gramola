package ckey.la_gramola;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by ruben on 11/02/16.
 */
public class SongListAdapter extends CursorAdapter {

    public SongListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_layout, parent, false);
        Song_view_holder viewHolder = new Song_view_holder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Song_view_holder viewHolder = (Song_view_holder) view.getTag();
        int titleColumn = cursor.getColumnIndex
                (MediaStore.Audio.Media.TITLE);
        int artistColumn = cursor.getColumnIndex
                (MediaStore.Audio.Media.ARTIST);
        int albumColumn = cursor.getColumnIndex
                (MediaStore.Audio.Media.ALBUM);

        viewHolder.title_view.setText(cursor.getString(titleColumn));
        viewHolder.artist_view.setText(cursor.getString(artistColumn));
        viewHolder.album_view.setText(cursor.getString(albumColumn));
    }
}
