package ckey.la_gramola;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Intent player_intent;
    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //GetPreferences.setColorScheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView songListView = (ListView) findViewById(R.id.songListView);
        this.player_intent = new Intent(getApplicationContext(), PlayerActivity.class);

        ContentResolver musicResolver = getContentResolver();
        final Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        songListView.setAdapter(new SongListAdapter(this, musicCursor, 0));

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                Uri playableUri = null;
                ArrayList<String> global_uris = new ArrayList<>();

                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                if (cursor != null) {
                    playableUri = Uri.withAppendedPath(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));

                    cursor.moveToFirst();
                    do {
                        global_uris.add(Uri.withAppendedPath(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))).toString());
                    } while (cursor.moveToNext());
                }

                if (playableUri != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("song_uri", playableUri.toString());
                    bundle.putStringArrayList("global_uris", global_uris);
                    player_intent.putExtra("bundle_uris", bundle);
                    startActivity(player_intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
