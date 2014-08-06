package com.donliang.musixygen;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	private MediaPlayer mediaPlayer = new MediaPlayer();
	private SeekBar songBar;
	private double startTime = 0;
	private double lastTime = 0;
	private int temp = 0;
	private int forwardTime = 10000;
	private int rewindTime = 10000;
	private TextView playTimeField;
	private boolean ButtonBoolean = false;
	
	private int positionTag = 0;
	private int resumeTag = 0;
	
	private MediaMetadataRetriever songMainMeta = new MediaMetadataRetriever();
	private ImageView album_artFront = null;
	private ImageView album_artBack = null;
	
	private Handler barHandler = new Handler();
	
//	private ListView SlidingMenu_List;

	private File SDCard_Path = Environment.getExternalStorageDirectory();;
	private File musicPathFile;
	
	private List<String> songs = new ArrayList<String>();
	private final String Path = new String(SDCard_Path.toString() + "/Musixygen/");
	
	private ListView lv;
	
	ArrayList<Song> songsTest;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        SlidingMenu menu = new SlidingMenu(this);
//        menu.setMode(SlidingMenu.RIGHT);
//        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.activity_menu);
		
//		SlidingMenu_List = (ListView) findViewById(R.id.left_drawer);
//		LayoutInflater SMinflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View SMview = SMinflater.inflate(R.layout.headerview, null);
//		SlidingMenu_List.addHeaderView(SMview);
		
//        String[] SMdrawer_menu = this.getResources().getStringArray(R.array.menu_item);
//        ArrayAdapter<String> SMadapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, SMdrawer_menu);
//        SlidingMenu_List.setAdapter(SMadapter);
        
        // Main Activity
//        albumList = (ListView)findViewById(R.id.album_list);
        lv = (ListView)findViewById(R.id.album_list);
        checkAvail();
        
        album_artFront = (ImageView)findViewById(R.id.album_front);
        album_artBack = (ImageView)findViewById(R.id.album_back);
        
//		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.control_button, null);
//		lv.addHeaderView(view);
        
		lv.setOnItemClickListener( new OnItemClickListener() {
			View view2;
			int select_item = -1;
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				v.setSelected(true);
				if((select_item == -1) || (select_item == position)){
					v.setBackgroundColor(Color.parseColor("#A69cede4"));
				} else {
					view2.setBackground(null);
					v.setBackgroundColor(Color.parseColor("#A69cede4"));
				}
				view2 = v;
				select_item=position;
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				Log.d("position", Path+songsTest.get(position).getFilenmae());
				try {
					v.setSelected(true);
					
					if (positionTag == position && mediaPlayer.isPlaying() == true) {
						mediaPlayer.pause();
						resumeTag = mediaPlayer.getCurrentPosition();
						Log.d("pause","pause");
					} else if (positionTag == position && mediaPlayer.isPlaying() == false) {
						mediaPlayer.seekTo(resumeTag);
						mediaPlayer.start();
						Log.d("resume","resume");
					} else {
						songMainMeta.setDataSource(Path+songsTest.get(position).getFilenmae());
	        			// Retrieve the album art
	        			byte[] art = null;
	        			if (songMainMeta.getEmbeddedPicture() != null) {
	        				art = songMainMeta.getEmbeddedPicture();       				
	        				final Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
	        				album_artFront.setImageBitmap(songImage);
	        				
	        				final Animation albumAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
	        				album_artFront.startAnimation(albumAnimation);
	        			} else {
	        				album_artFront.setImageResource(R.drawable.album_cover);
	        			}
						
						positionTag = position;
						
						mediaPlayer.reset();
						mediaPlayer.setDataSource(Path+songsTest.get(position).getFilenmae());
						mediaPlayer.prepare();
						mediaPlayer.start();
						
		        		startTime = mediaPlayer.getCurrentPosition();
		        		lastTime = mediaPlayer.getDuration();
		        		songBar.setMax((int)lastTime);
		        		songBar.setProgress((int)startTime);
		        		barHandler.postDelayed(updatedSongTime, 100);
					}
	        		
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
//        String[] drawer_menu = this.getResources().getStringArray(R.array.album_item);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.song_list_item, drawer_menu);
//        albumList.setAdapter(adapter);
        
        playTimeField = (TextView)findViewById(R.id.playTime);
//        initSlidngMenuLV();
        
        songBar = (SeekBar)findViewById(R.id.songBar);
        
        
//        Button play = (Button)findViewById(R.id.play_button);
//        play.setOnClickListener(new View.OnClickListener() {       	
//        	@Override
//        	public void onClick(View v) {
//        		Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_games);
//        		mediaPlayer = MediaPlayer.create(MainActivity.this, path);
//        		Toast.makeText(getBaseContext(), "play", Toast.LENGTH_SHORT ).show();
//        		mediaPlayer.start();
//        		
        		// initialize seekbar progress

//        	}
//        });
//        
//        Button stop = (Button)findViewById(R.id.stop_button);
//        stop.setOnClickListener(new View.OnClickListener() {       	
//        	@Override
//        	public void onClick(View v) {
//        		if (mediaPlayer.isPlaying()) {
//        			Toast.makeText(getBaseContext(), "stop", Toast.LENGTH_SHORT ).show();
//            		mediaPlayer.pause();
//            		mediaPlayer.seekTo(0);
//        		}
//        	}
//        });
//        
//        Button pause = (Button)findViewById(R.id.pause_button);
//        pause.setOnClickListener(new View.OnClickListener() {       	
//        	@Override
//        	public void onClick(View v) {
//        		if (mediaPlayer.isPlaying()) {
//        			Toast.makeText(getBaseContext(), "pause", Toast.LENGTH_SHORT ).show();
//            		mediaPlayer.pause();      			
//        		}
//        	}
//        });
//        
        Button loop = (Button)findViewById(R.id.loop_button);
        loop.setOnClickListener(new View.OnClickListener() {       	
        	@Override
        	public void onClick(View v) {
        		if (!ButtonBoolean && mediaPlayer != null ) {
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
        
//        Button forward = (Button)findViewById(R.id.forward_button);
//        forward.setOnClickListener(new View.OnClickListener() {       	
//        	@Override
//        	public void onClick(View v) {
//        		temp = (int)startTime;
//        		if ( (temp+forwardTime) <= lastTime ) {
//        			startTime = startTime + forwardTime;
//        			mediaPlayer.seekTo((int)startTime);
//        		} else {
//        			Toast.makeText(getBaseContext(), "Cannot forward.", Toast.LENGTH_SHORT).show();
//        		}
//        	}
//        });
        
//        Button rewind = (Button)findViewById(R.id.rewind_button);
//        rewind.setOnClickListener(new View.OnClickListener() {       	
//        	@Override
//        	public void onClick(View v) {
//        		temp = (int)startTime;
//        		if ( (temp - rewindTime) > 0) {
//        			startTime = startTime - rewindTime;
//        			mediaPlayer.seekTo((int)startTime);
//        		} else {
//        			Toast.makeText(getBaseContext(), "Cannot rewind", Toast.LENGTH_SHORT).show();
//        		}
//        	}
//        });
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
    }

    private void checkAvail() {
		
		// testing
		songsTest = new ArrayList<Song>();
		ArrayList<Map<String,String>> songsMap = new ArrayList<Map<String,String>>();
		
        // Check SD Card mounted or not
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
        	return;
        } else { 
//        	textview.setText("SD Card is here.");
        	SDCard_Path = Environment.getExternalStorageDirectory();
        }
        // Check folder in SD Card exists or not
        // Environment.getExternalStorageDirectory.getParent() returns '/storage/emulated'
        // Environment.getExternalStorageDirectory.getName() returns' 0
        // Full path return exact directory name in SDCard.
        musicPathFile = new File(SDCard_Path.getParent() + "/" + SDCard_Path.getName() + "/Musixygen/");
        if (musicPathFile.exists()) {
        	final String path = musicPathFile.getPath();
//        	Log.d("get", path);
        }
        if (musicPathFile.listFiles() == null) {
//        	textview.setText("Cannot find files!");
        	Log.d("get song","null");
        } else {
//        	textview.setText("Yo!");
        	if (musicPathFile.listFiles(new mp3FileFilter()).length > 0) {
        		for (File file : musicPathFile.listFiles(new mp3FileFilter())) {

        			// get MediaMetaData for each song
        			MediaMetadataRetriever songMetaData = new MediaMetadataRetriever();
        			songMetaData.setDataSource(Path + file.getName());
        			String artistName = songMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
	    			if (artistName == null) {
	    				artistName = "Unknown";
	    			}
        			
	    			// Retrieve the duration
        			int secs = Integer.parseInt(songMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
        			int mins = secs / 60;
        			secs = secs % 60;
        			String duration = String.format("%02d:%02d", mins, secs);
        			
        			// Retrieve the artist name
        			String singerName = songMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        			if (singerName == null || singerName.equals("")) {
        				singerName = "Unknown";
        			}

        			
        			// Retrieve the song title
        			String songTitle = songMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        			if (songTitle == null || songTitle.equals("")) {
        				songTitle = "Unknown";
        			}
        			
        			// customized listview 7.31 testing
        			Song s = new Song();
        			s.setFilename(file.getName());
        			s.setSinger(singerName);
        			s.setDuration(duration);
           			s.setTitle(songTitle);
        			songsTest.add(s);
        			
        			Map<String, String> mapSongInfo = convertSongToMap(s);
        			songsMap.add(mapSongInfo);
        			
//        			songs.add(file.getName());
        			
        		}
        		
        		SimpleAdapter adapter = new SimpleAdapter(this,songsMap,R.layout.song_list_item, new String[]{"songTitle","duration"},new int[]{R.id.text1, R.id.text2});
        		lv.setAdapter(adapter);
        		
//        		ArrayAdapter<String> songList = new ArrayAdapter<String>(this, R.layout.list_item, R.id.text1, songs);
//        		lv.setAdapter(songList);
        	}       	
        }	
	}

	private Runnable updatedSongTime = new Runnable() {
    	@Override
		public void run() {
			// TODO Auto-generated method stub
    		// Here we got an exception.
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

	private Map<String, String> convertSongToMap(Song s) {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("songPath", s.getFilenmae());
		map.put("duration", s.getDuration());
		map.put("singerName", s.getSinger());
		map.put("songTitle", s.getTitle());
		return map;
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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
    
	class mp3FileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}

}
