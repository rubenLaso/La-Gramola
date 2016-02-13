package ckey.la_gramola;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    private Uri song_uri;
    private ArrayList<String> global_uris;
    private int position;
    private ImageButton play;
    private ImageButton next;
    private ImageButton prev;
    private Bundle bundle;
    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();

        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        prev = (ImageButton) findViewById(R.id.prev);

        bundle = getIntent().getBundleExtra("bundle_uris");
        song_uri = Uri.parse(bundle.getString("song_uri"));
        global_uris = bundle.getStringArrayList("global_uris");

        position = global_uris.indexOf(song_uri.toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), song_uri);

        MainActivity.mediaPlayer=mediaPlayer;

        mediaPlayer.start();

        play.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play_button);
                    return;
                }
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause_button);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GetPreferences.getShuffle(getApplicationContext())) {
                    position = (int) (Math.random() * global_uris.size());
                } else {
                    ++position;
                    if (position >= global_uris.size()) {
                        position = 0;
                    }
                }
                song_uri = Uri.parse(global_uris.get(position));

                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), song_uri);

                MainActivity.mediaPlayer=mediaPlayer;

                mediaPlayer.start();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GetPreferences.getShuffle(getApplicationContext())) {
                    position = (int) (Math.random() * global_uris.size());
                } else {
                    --position;
                    if (position < 0) {
                        position = global_uris.size() - 1;
                    }
                }
                song_uri = Uri.parse(global_uris.get(position));

                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), song_uri);

                MainActivity.mediaPlayer = mediaPlayer;

                mediaPlayer.start();
            }
        });
    }
}
