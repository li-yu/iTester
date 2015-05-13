package com.liyu.itester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.liyu.itester.utils.MountListener;
import com.liyu.itester.utils.SdPath;
import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MP4Activity extends Activity implements OnClickListener,MountListener.OnStateChangedListener{
	TextView tv_mp4;
	private VideoView videoview;
	final String TAG = "Mp4Activity";
	private List<String> playList = new ArrayList<String>();
	boolean isPause = false;
	private Handler mHandle;
	private String videoType =".mp4";
	private MountListener ml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp4);
		initUI();
	}

	private void initUI()
	{
		if(FunctionApplication.isAutoMode)
		videoType = "."+XmlUtils.getEntity(this.getClass().getSimpleName().toString()).getParameter().toString().trim();
		tv_mp4 = (TextView)findViewById(R.id.textView_mp4);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		videoview=(VideoView)this.findViewById(R.id.videoView_mp4);
		videoview.setMediaController(new MediaController(this));
		mHandle = new Handler(){   
 	       public void handleMessage(Message msg){  
         	switch(msg.what){
         	case 1:
         		  Random  rand = new Random(System.currentTimeMillis());
        		  int id = rand.nextInt(playList.size());
        		  videoview.setVideoPath(playList.get(id));
        		  videoview.start();
        		  String[] name = playList.get(id).split("/");
        		  tv_mp4.setText("Playing: "+name[name.length-1]);
        		  tv_mp4.setTextColor(Color.BLUE);
        		  findViewById(R.id.button_pass).setEnabled(true);
         		break;
         	case 2:
         		  tv_mp4.setText("No Videos!");
        		  findViewById(R.id.button_pass).setEnabled(false);
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
						if(hasVideo())
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
			this.setResult(R.id.button_mp4,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mp4), MP4Activity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("fail", false);
			this.setResult(R.id.button_mp4,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mp4), MP4Activity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	
	public boolean findVideo(String mnt,String type)
    {
    	File f = new File(mnt);
    	if (!f.exists()) return false;
    	if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    			return false;
    	try{
    		File[] fl = f.listFiles();
    		if (fl.length == 0 )
    		{
    			return false;
    		}
    	
    		for (int i = 0; i< fl.length; i++)
    		{
    			if (fl[i].isDirectory())   findVideo(fl[i].getAbsolutePath(),type);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ml = new MountListener(MP4Activity.this);
		ml.setOnStateChangedListener(this);
        videoview.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(videoview!=null&videoview.isPlaying())
		{
			videoview.pause();
		}
		if(ml!=null)
		{
			ml.removeListener();
		}
	}
	private boolean hasVideo()
	{
		if(findVideo(SdPath.getRealExternalSDpath(), videoType))
		{
			return true;
		}
		else if(findVideo(SdPath.getRealInternalSDpath(), videoType))
		{
			return true;
		}
		else if(findVideo(SdPath.getRealUSBpath(), videoType))
		{
			return true;
		}
		return false;
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(MP4Activity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    MP4Activity.this.setResult(R.id.button_mp4,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mp4),MP4Activity.this,XmlUtils.activityClasses.get(0), false);
	    				MP4Activity.this.finish();
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
}
