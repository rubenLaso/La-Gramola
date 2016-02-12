package fernandoruben.mp3player;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ruben on 3/02/16.
 */
public class ListHandler {

    public static ArrayList<AudioFile> importLocalAudioFiles () {
        ArrayList<AudioFile> audioFiles = new ArrayList<>();

        MediaStore mediaStore = new MediaStore();
        mediaStore.

        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                // ...process entry...
            } while (cursor.moveToNext());
        }
    }
}
