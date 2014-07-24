package com.donliang.musixygen;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = (Button)findViewById(R.id.play_button);
        play.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_games);
        		mediaPlayer = MediaPlayer.create(MainActivity.this, path);
        		Toast.makeText(getBaseContext(), "play", Toast.LENGTH_SHORT ).show();
        		mediaPlayer.start();
        	}
        });
        
        Button stop = (Button)findViewById(R.id.stop_button);
        stop.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		if (mediaPlayer.isPlaying()) {
        			Toast.makeText(getBaseContext(), "stop", Toast.LENGTH_SHORT ).show();
            		mediaPlayer.stop();
            		mediaPlayer.release(); 
            		
        		}
        	}
        });
        
        Button pause = (Button)findViewById(R.id.pause_button);
        pause.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		if (mediaPlayer.isPlaying()) {
        			Toast.makeText(getBaseContext(), "pause", Toast.LENGTH_SHORT ).show();
            		mediaPlayer.pause();      			
        		}
        	}
        });
        
        Button loop = (Button)findViewById(R.id.loop_button);
        loop.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		mediaPlayer.setLooping(true);
        	}
        });
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		super.onDestroy();
	}
    
    

}
