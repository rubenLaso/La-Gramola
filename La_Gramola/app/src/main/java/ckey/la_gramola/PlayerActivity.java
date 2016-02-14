package ckey.la_gramola;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private Uri song_uri;
    private ArrayList<String> global_uris;
    private int position;
    private ImageButton play;
    private ImageButton next;
    private ImageButton prev;
    private ImageButton shuffle;
    private ImageButton lyrics;
    private TextView lyrics_view;
    private TextView title_view;
    private TextView artist_view;
    private SeekBar seekBar;
    private Bundle bundle;
    static MediaPlayer mediaPlayer;
    private boolean mutex;
    private GetLyrics lyrics_getter;
    static CharSequence old_lyrics = "";
    private boolean valid_lyrics = false;
    static boolean was_playing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //GetPreferences.setColorScheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        old_lyrics = "";
        mediaPlayer = null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        shuffle = (ImageButton) findViewById(R.id.shuffle_button);
        lyrics = (ImageButton) findViewById(R.id.lyrics_button);
        lyrics_view = (TextView) findViewById(R.id.lyrics);
        title_view = (TextView) findViewById(R.id.song_title);
        artist_view = (TextView) findViewById(R.id.song_artist);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        bundle = getIntent().getBundleExtra("bundle_uris");
        song_uri = Uri.parse(bundle.getString("song_uri"));
        global_uris = bundle.getStringArrayList("global_uris");

        position = global_uris.indexOf(song_uri.toString());

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), song_uri);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    nextSong();
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
                }
            });
        }

        if (GetPreferences.getLyricsAuto(getApplicationContext())) {
            if (old_lyrics != null && !old_lyrics.equals(""))
                lyrics_view.setText(old_lyrics);
            else
                obtenerLyrics(song_uri);
        } else {
            lyrics_view.setBackgroundResource(0);
        }

        setTitle(getFileName(song_uri));

        if (artist_view != null && title_view != null) {
            artist_view.setText(getArtist(song_uri));
            title_view.setText(getTitle(song_uri));
        }

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mutex)
                    mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mutex = true;
                return false;
            }
        });

        Thread update_seekbar = new Thread() {
            @Override
            public void run () {
                try {
                    while (mediaPlayer.getCurrentPosition() != mediaPlayer.getDuration()) {
                        mutex = false;
                        sleep(500);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        update_seekbar.start();

        changeImage(song_uri);

        MainActivity.mediaPlayer=mediaPlayer;
        if (mediaPlayer.isPlaying() || was_playing) {
            mediaPlayer.start();
        }

        play.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle_click_listener();
            }
        });

        lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyrics_listener();
            }
        });

        check_buttons();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.player_layout);

        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);
        shuffle = (ImageButton) findViewById(R.id.shuffle_button);
        lyrics = (ImageButton) findViewById(R.id.lyrics_button);
        lyrics_view = (TextView) findViewById(R.id.lyrics);
        title_view = (TextView) findViewById(R.id.song_title);
        artist_view = (TextView) findViewById(R.id.song_artist);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        if (GetPreferences.getLyricsAuto(getApplicationContext())) {
            if (old_lyrics != null && !old_lyrics.equals(""))
                lyrics_view.setText(old_lyrics);
            else
                obtenerLyrics(song_uri);
        } else {
            lyrics_view.setBackgroundResource(0);
        }

        setTitle(getFileName(song_uri));

        if (artist_view != null && title_view != null) {
            artist_view.setText(getArtist(song_uri));
            title_view.setText(getTitle(song_uri));
        }

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mutex)
                    mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mutex = true;
                return false;
            }
        });

        Thread update_seekbar = new Thread() {
            @Override
            public void run () {
                try {
                    while (mediaPlayer.getCurrentPosition() != mediaPlayer.getDuration()) {
                        mutex = false;
                        sleep(500);
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        update_seekbar.start();

        changeImage(song_uri);

        play.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffle_click_listener();
            }
        });

        lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyrics_listener();
            }
        });

        check_buttons();
    }

    public void check_buttons () {
        if (GetPreferences.getShuffle(getApplicationContext())) {
            shuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
        } else {
            shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
        }
        if (GetPreferences.getLyricsAuto(getApplicationContext())) {
            lyrics.setImageResource(R.drawable.ic_music_note_white_24dp);
        } else {
            lyrics.setImageResource(R.drawable.ic_music_note_black_24dp);
        }
    }

    private void shuffle_click_listener () {
        if (GetPreferences.getShuffle(getApplicationContext())) {
            GetPreferences.setShuffle(getApplicationContext(), false);
            shuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
        } else {
            GetPreferences.setShuffle(getApplicationContext(), true);
            shuffle.setImageResource(R.drawable.ic_shuffle_white_24dp);
        }
    }

    private void lyrics_listener () {
        if (lyrics_view.getText().equals("")) {
            if (valid_lyrics) {
                lyrics_view.setText(old_lyrics);
            } else {
                obtenerLyrics(song_uri);
                old_lyrics = lyrics_view.getText();
                valid_lyrics = true;
            }
            lyrics.setImageResource(R.drawable.ic_music_note_white_24dp);
            GetPreferences.setLyricsAuto(getApplicationContext(), true);
            lyrics_view.setBackgroundResource(R.color.lyrics_background);
        }
        else{
            lyrics_view.setText("");
            lyrics_view.setBackgroundResource(0);
            lyrics.setImageResource(R.drawable.ic_music_note_black_24dp);
            GetPreferences.setLyricsAuto(getApplicationContext(), false);
        }
    }

    private String getTitle (Uri audioFileUri) {
        MediaMetadataRetriever metaRetriever= new MediaMetadataRetriever();
        metaRetriever.setDataSource(getApplicationContext(), audioFileUri);
        return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }

    private String getArtist (Uri audioFileUri) {
        MediaMetadataRetriever metaRetriever= new MediaMetadataRetriever();
        metaRetriever.setDataSource(getApplicationContext(), audioFileUri);
        return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    private String getFileName(Uri audioFileUri) {
        String result = null;
        if (audioFileUri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(audioFileUri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = audioFileUri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void changeImage (Uri audioFileUri) {
        MediaMetadataRetriever metaRetriever= new MediaMetadataRetriever();
        metaRetriever.setDataSource(getApplicationContext(), audioFileUri);
        byte [] image = metaRetriever.getEmbeddedPicture();

        if (image != null && image.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            int dim = (int) (1.75 * getScreenSizePixels());
            bitmap = getResizedBitmap(bitmap, dim, dim);
            BitmapDrawable bm = new BitmapDrawable(bitmap);
            bm.setGravity(Gravity.CENTER);
            findViewById(R.id.scroll_lyrics).setBackgroundDrawable(bm);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.caratula);
            int dim = (int) (1.75 * getScreenSizePixels());
            bitmap = getResizedBitmap(bitmap, dim, dim);
            BitmapDrawable bm = new BitmapDrawable(bitmap);
            bm.setGravity(Gravity.CENTER);
            findViewById(R.id.scroll_lyrics).setBackgroundDrawable(bm);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null)
            return null;
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public double getScreenSizePixels()
    {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // Note, screenHeightDp isn't reliable
        // (it seems to be too small by the height of the status bar),
        // but we assume screenWidthDp is reliable.
        // Note also, dm.widthPixels,dm.heightPixels aren't reliably pixels
        // (they get confused when in screen compatibility mode, it seems),
        // but we assume their ratio is correct.
        return  (double)config.screenWidthDp * dm.density;
    }

    private void obtenerLyrics(Uri song_uri) {
        lyrics_getter = new GetLyrics(getApplicationContext(), findViewById(android.R.id.content));
        try {
            String title = getTitle(song_uri);
            String artist = getArtist(song_uri);
            if (title == null) {
                title = "";
            }
            if (artist == null) {
                artist = "";
            }
            lyrics_getter.execute(title, artist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play () {
        if (mediaPlayer.isPlaying()) {
            was_playing = false;
            mediaPlayer.pause();
            play.setImageResource(R.drawable.play_button);
            return;
        }
        was_playing = true;
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);
        play.setImageResource(R.drawable.pause_button);
    }

    private void nextSong () {
        was_playing = mediaPlayer.isPlaying();
        if (GetPreferences.getShuffle(getApplicationContext())) {
            position = (int) (Math.random() * global_uris.size());
        } else {
            ++position;
            if (position >= global_uris.size()) {
                position = 0;
            }
        }
        song_uri = Uri.parse(global_uris.get(position));

        setTitle(getFileName(song_uri));

        if (artist_view != null && title_view != null) {
            artist_view.setText(getArtist(song_uri));
            title_view.setText(getTitle(song_uri));
        }

        if (GetPreferences.getLyricsAuto(getApplicationContext())) {
            obtenerLyrics(song_uri);
            lyrics_view.setBackgroundResource(R.color.lyrics_background);
        } else {
            valid_lyrics = false;
            lyrics_view.setText("");
            lyrics_view.setBackgroundResource(0);
        }

        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), song_uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);

        changeImage(song_uri);

        MainActivity.mediaPlayer = mediaPlayer;

        if (was_playing)
            mediaPlayer.start();
    }

    private void prevSong () {
        was_playing = mediaPlayer.isPlaying();
        valid_lyrics = false;
        lyrics_view.setText("");
        if (GetPreferences.getShuffle(getApplicationContext())) {
            position = (int) (Math.random() * global_uris.size());
        } else {
            --position;
            if (position < 0) {
                position = global_uris.size() - 1;
            }
        }
        song_uri = Uri.parse(global_uris.get(position));

        setTitle(getFileName(song_uri));

        if (artist_view != null && title_view != null) {
            artist_view.setText(getArtist(song_uri));
            title_view.setText(getTitle(song_uri));
        }

        if (GetPreferences.getLyricsAuto(getApplicationContext())) {
            obtenerLyrics(song_uri);
            lyrics_view.setBackgroundResource(R.color.lyrics_background);
        } else {
            valid_lyrics = false;
            lyrics_view.setText("");
            lyrics_view.setBackgroundResource(0);
        }

        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), song_uri);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);

        changeImage(song_uri);

        MainActivity.mediaPlayer = mediaPlayer;

        if (was_playing)
            mediaPlayer.start();
    }
}
