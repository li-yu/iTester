package com.liyu.itester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.liyu.itester.utils.SdPath;
import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;
import com.liyu.itester.utils.MountListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MP3Activity extends Activity implements OnClickListener,OnSeekBarChangeListener,MountListener.OnStateChangedListener{
	TextView tv_mp3;
	final String TAG = "MP3Activity";
	private SeekBar seekbar;
	private MediaPlayer audioplayer;
	private Handler mHandle;
	private List<String> playList = new ArrayList<String>();
	private String audioType= ".mp3";
	private MountListener ml;
	private boolean playSuccessed= false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3);
	
		initUI();
		}
		
		private void initUI()
		{
			if(FunctionApplication.isAutoMode)
			audioType = "."+XmlUtils.getEntity(this.getClass().getSimpleName()).getParameter().toString().trim();
			audioplayer=new MediaPlayer();
			audioplayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					  Random  rand = new Random(System.currentTimeMillis());
		    		  int id = rand.nextInt(playList.size());
		    		  playMusic(playList.get(id));
		    		  String[] name = playList.get(id).split("/");
		    		  //tv_mp3.setText("正在播放: "+name[name.length-1]);
		    		  tv_mp3.setText(R.string.label_mp3_pluginheadset);
				}
			});
			tv_mp3 = (TextView)findViewById(R.id.textView_mp3);
			tv_mp3.setTextColor(Color.BLUE);
			tv_mp3.setTextSize(30);
			findViewById(R.id.button_pass).setOnClickListener(this);
			findViewById(R.id.button_fail).setOnClickListener(this);
			seekbar = (SeekBar)findViewById(R.id.seekBar_mp3);
			seekbar.setOnSeekBarChangeListener(this);
			mHandle = new Handler(){   
	    	       public void handleMessage(Message msg){  
	            	switch(msg.what){
	            	case 0:
	            		if(audioplayer!=null){
	    	           int position = audioplayer.getCurrentPosition();   
	    	           int mMax = audioplayer.getDuration();  
	    	           int sMax = seekbar.getMax();    
	    	           seekbar.setProgress(position*sMax/mMax);
	            		}
	    	           break;
	            	case 1:
	            	   Random  rand = new Random(System.currentTimeMillis());
	  	    		   int id = rand.nextInt(playList.size());
	  	    		   playMusic(playList.get(id));
	  	    		   String[] name = playList.get(id).split("/");
	  	    		   //tv_mp3.setText("正在播放: "+name[name.length-1]);
	  	    		   tv_mp3.setText(R.string.label_mp3_pluginheadset);
	  	    		   seekbar.setEnabled(true);
	  	    		   //findViewById(R.id.button_pass).setEnabled(true);
	  	    		   playSuccessed= true;
	            		break;
	            	case 2:
	            		tv_mp3.setText("No MP3!");
	  	    		    findViewById(R.id.button_pass).setEnabled(false);
	  	    		    playSuccessed = false;
	  	    		    seekbar.setEnabled(false);
	            		break;
	            	}
	              }  
	    	    }; 
	    	    
	    		new Thread()
				{
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							if(hasMusic())
							mHandle.sendEmptyMessage(1);
							else
							mHandle.sendEmptyMessage(2);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							
							mHandle.sendEmptyMessage(2); 
							e.printStackTrace();
						}
					}
					
				}.start();
		}
		private void playMusic(String path)  
	    {   
	        try   
	        {   
	            /* 重置MediaPlayer */   
	            audioplayer.reset();   
	            /* 设置要播放的文件的路径 */   
	            audioplayer.setDataSource(path);   
	            /* 准备播放 */   
	            audioplayer.prepare();   
	            /* 开始播放 */   
	            audioplayer.start();
	            startProgressUpdate();     
	        }catch (IOException e)
	        {
	        	
	        }   
	    }
		 public void startProgressUpdate()
		    {  
		       DelayThread dThread = new DelayThread(100);  
		       dThread.start();  
		     } 
			
		public class DelayThread extends Thread {  
			          int milliseconds;     
			      public DelayThread(int i){  
			            milliseconds = i;  
			       }  
			       public void run() {  
			           while(true){  
			               try {  
			                   sleep(milliseconds);  
			               } catch (InterruptedException e) {  
			                   // TODO Auto-generated catch block  
			                   e.printStackTrace();  
			               }  
			                 
			               mHandle.sendEmptyMessage(0);  
			          }  
			        } 
			 
			    }
			   
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(jumpUtils.isFastDoubleClick())
				return;
			Intent intent = new Intent();
			switch(v.getId())
			{
			case R.id.button_pass:
				intent.putExtra("pass", true);
				this.setResult(R.id.button_mp3,intent);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mp3), MP3Activity.this, XmlUtils.activityClasses.get(0), true);
				MP3Activity.this.finish();
				break;
			case R.id.button_fail:
				intent.putExtra("pass", false);
				this.setResult(R.id.button_mp3,intent);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mp3), MP3Activity.this, XmlUtils.activityClasses.get(0), false);
				MP3Activity.this.finish();
				break;
			}
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			 
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			 
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			  int dest = seekBar.getProgress();  
			  int mMax = audioplayer.getDuration();  
		      int sMax = seekbar.getMax();  
			  audioplayer.seekTo(mMax*dest/sMax);
		}

		
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			if(audioplayer!=null&&audioplayer.isPlaying())
			{
				audioplayer.stop();
				audioplayer.release();
				audioplayer = null;
			}
		}

		public boolean findMusic(String mnt,String type)
	    {
	    	File f = new File(mnt);
	    	try{
	    		File[] fl = f.listFiles();
	    		if (fl.length == 0 )
	    		{
	    			return false;
	    		}
	    	
	    		for (int i = 0; i< fl.length; i++)
	    		{
	    			if (fl[i].isDirectory())   findMusic(fl[i].getAbsolutePath(),type);
	    			if (fl[i].toString().toLowerCase().endsWith(type))
	    			{
	    				playList.add(fl[i].getAbsoluteFile().toString());
	    			}
	    		}
	    	}
	    	catch (Exception e)
	    	{
	    		return false;
	    	}
	    	
	    	if (!playList.isEmpty())
	    	{
	    		return true;
	    	}
	    	else
	    	{
	    		return false;
	    	}
	    }
		private boolean hasMusic()
		{
			if(findMusic(SdPath.getRealExternalSDpath(), audioType))
			{
				return true;
			}
			else if(findMusic(SdPath.getRealInternalSDpath(), audioType))
			{
				return true;
			}
			else if(findMusic(SdPath.getRealUSBpath(), audioType))
			{
				return true;
			}
			return false;
		}
		 @Override
		    public boolean onKeyDown(int keyCode, KeyEvent event) { 
		    	if (keyCode == KeyEvent.KEYCODE_BACK)
		    	{
		    		new AlertDialog.Builder(MP3Activity.this)
		    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
		    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
		    			public void onClick(DialogInterface dialog, int whichButton)
		    			{
		    				Intent intent = new Intent();
		    				intent.putExtra("pass", false);
		    			    MP3Activity.this.setResult(R.id.button_mp3,intent);
		    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mp4),MP3Activity.this,XmlUtils.activityClasses.get(0), false);
		    				MP3Activity.this.finish();
		    			}
		    		})
		    		.show();
		    		return true;
		    	}
				return false;
		    }

		@Override
		public void onMounted() {
			// TODO Auto-generated method stub
			initUI();
		}

		@Override
		public void onUnMounted() {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			if(ml!=null)
			{
				ml.removeListener();
			}
			unregisterReceiver(headSetReceiver);
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			ml = new MountListener(MP3Activity.this);
			ml.setOnStateChangedListener(this);
			
			IntentFilter headsetFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
			registerReceiver(headSetReceiver, headsetFilter);
		}
		
		private BroadcastReceiver headSetReceiver= new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				if (intent.getIntExtra("state", 2) == 0)
				{
					//拔出
					tv_mp3.setText(R.string.label_mp3_headset_removed);
                }
				else if (intent.getIntExtra("state", 2) == 1) 
                { 
                   //插入
					if(playSuccessed)
					{
						findViewById(R.id.button_pass).setEnabled(true);
					}
					tv_mp3.setText(getResources().getString(R.string.label_mp3_detected)+getHeadsetType(intent.getIntExtra("microphone", 2)));
					
                }
			}
		};
		
		private String getHeadsetType(int type)
		{
			if(type==1)
				return getResources().getString(R.string.label_mp3_headset_type_4);
			else return getResources().getString(R.string.label_mp3_headset_type_3);
		}
		
}
