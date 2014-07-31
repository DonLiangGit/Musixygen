package com.donliang.musixygen;

import java.util.concurrent.TimeUnit;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	MediaPlayer mediaPlayer;
	private SeekBar songBar;
	private double startTime = 0;
	private double lastTime = 0;
	private int temp = 0;
	private int forwardTime = 10000;
	private int rewindTime = 10000;
	private TextView playTimeField;
	private boolean ButtonBoolean = false;
	
	private Handler barHandler = new Handler();
	
	private ListView albumList;
//	private ListView SlidingMenu_List;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_layout);
        
//		setBehindContentView(R.layout.activity_menu);
//		getSlidingMenu().setBehindOffset(100);
		
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.activity_menu);
		
//		SlidingMenu_List = (ListView) findViewById(R.id.left_drawer);
//		LayoutInflater SMinflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View SMview = SMinflater.inflate(R.layout.headerview, null);
//		SlidingMenu_List.addHeaderView(SMview);
		
//        String[] SMdrawer_menu = this.getResources().getStringArray(R.array.menu_item);
//        ArrayAdapter<String> SMadapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, SMdrawer_menu);
//        SlidingMenu_List.setAdapter(SMadapter);
        
        // Main Activity
        albumList = (ListView)findViewById(R.id.album_list);
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.control_button, null);
		albumList.addHeaderView(view);
		
        String[] drawer_menu = this.getResources().getStringArray(R.array.album_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.song_list_item, drawer_menu);
        albumList.setAdapter(adapter);
        
        playTimeField = (TextView)findViewById(R.id.playTime);
//        initSlidngMenuLV();
        
        songBar = (SeekBar)findViewById(R.id.songBar);
        
        
        Button play = (Button)findViewById(R.id.play_button);
        play.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_games);
        		mediaPlayer = MediaPlayer.create(MainActivity.this, path);
        		Toast.makeText(getBaseContext(), "play", Toast.LENGTH_SHORT ).show();
        		mediaPlayer.start();
        		
        		// initialize seekbar progress
        		startTime = mediaPlayer.getCurrentPosition();
        		lastTime = mediaPlayer.getDuration();
        		songBar.setMax((int)lastTime);
        		songBar.setProgress((int)startTime);
        		barHandler.postDelayed(updatedSongTime, 100);
        	}
        });
//        
        Button stop = (Button)findViewById(R.id.stop_button);
        stop.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		if (mediaPlayer.isPlaying()) {
        			Toast.makeText(getBaseContext(), "stop", Toast.LENGTH_SHORT ).show();
            		mediaPlayer.pause();
            		mediaPlayer.seekTo(0);
        		}
        	}
        });
//        
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
//        
        Button loop = (Button)findViewById(R.id.loop_button);
        loop.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		if (!ButtonBoolean) {
	        		Toast.makeText(getBaseContext(), "looping is true", Toast.LENGTH_SHORT ).show();
	        		mediaPlayer.setLooping(true); 
	        		ButtonBoolean = true; 
        		} else {
            		Toast.makeText(getBaseContext(), "looping is false", Toast.LENGTH_SHORT ).show();
            		mediaPlayer.setLooping(false); 
            		ButtonBoolean = false; 
        		}     		
        	}
        });
        
        Button forward = (Button)findViewById(R.id.forward_button);
        forward.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		temp = (int)startTime;
        		if ( (temp+forwardTime) <= lastTime ) {
        			startTime = startTime + forwardTime;
        			mediaPlayer.seekTo((int)startTime);
        		} else {
        			Toast.makeText(getBaseContext(), "Cannot forward.", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
        
        Button rewind = (Button)findViewById(R.id.rewind_button);
        rewind.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		temp = (int)startTime;
        		if ( (temp - rewindTime) > 0) {
        			startTime = startTime - rewindTime;
        			mediaPlayer.seekTo((int)startTime);
        		} else {
        			Toast.makeText(getBaseContext(), "Cannot rewind", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }

//	private void initSlidngMenuLV() {
//		// TODO Auto-generated method stub
//		setBehindContentView(R.layout.activity_menu);
//		getSlidingMenu().setBehindOffset(100);
//	}
	

	private Runnable updatedSongTime = new Runnable() {

    	@Override
		public void run() {
			// TODO Auto-generated method stub
			startTime = mediaPlayer.getCurrentPosition();
			// Dynamic Time
			playTimeField.setText(String.format("%d:%02d",
					TimeUnit.MILLISECONDS.toMinutes((long)startTime),
					TimeUnit.MILLISECONDS.toSeconds((long)startTime) - 
					TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)startTime)))
					);
			
			songBar.setProgress((int)startTime);
			barHandler.postDelayed(this, 100);
		}
    	
    };

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
