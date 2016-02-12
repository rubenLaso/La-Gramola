package ckey.la_gramola;

import android.net.Uri;

/**
 * Created by ruben on 11/02/16.
 */
public class AudioFile {
    private long ID;
    private Uri uri;
    private String title;
    private String artist;

    public AudioFile(long ID, Uri uri, String title, String artist) {
        this.ID = ID;
        this.uri = uri;
        this.title = title;
        this.artist = artist;
    }

    public long getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Uri getUri() {
        return uri;
    }
}
