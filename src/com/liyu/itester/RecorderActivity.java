package com.liyu.itester;


import com.liyu.itester.soundrecorder.RecordHelper;
import com.liyu.itester.soundrecorder.VUMeter;
import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class RecorderActivity extends Activity implements OnClickListener,RecordHelper.OnStateChangedListener{
private static final String TAG = "SoundHandActivity";
	
	private VUMeter mVUMeter;
	private RecordHelper mRecorder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recorder);
		
		setupViews();
		
		}

		public void setupViews() {
			findViewById(R.id.imageButton_play).setOnClickListener(this);
			findViewById(R.id.imageButton_start).setOnClickListener(this);
			findViewById(R.id.imageButton_stop).setOnClickListener(this);
			findViewById(R.id.button_pass).setOnClickListener(this);
			findViewById(R.id.button_fail).setOnClickListener(this);
			findViewById(R.id.imageButton_play).setEnabled(false);
			findViewById(R.id.imageButton_stop).setEnabled(false);
			findViewById(R.id.button_pass).setEnabled(false);
			mRecorder = new RecordHelper();
			mRecorder.setOnStateChangedListener(this);
			mVUMeter = (VUMeter) findViewById(R.id.uvMeter);
			mVUMeter.setRecorder(mRecorder);
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
			this.setResult(R.id.button_record,intent);
			mRecorder.delete();
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_recorder), RecorderActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_record,intent);
			mRecorder.delete();
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_recorder), RecorderActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		case R.id.imageButton_play:
			findViewById(R.id.imageButton_play).setEnabled(false);
			findViewById(R.id.imageButton_stop).setEnabled(true);
			mRecorder.startPlayback();
			break;
		case R.id.imageButton_start:
			findViewById(R.id.imageButton_stop).setEnabled(true);
			findViewById(R.id.imageButton_play).setEnabled(false);
			findViewById(R.id.imageButton_start).setEnabled(false);
			mRecorder.startRecording(MediaRecorder.OutputFormat.AMR_NB, ".amr",this);
			break;
		case R.id.imageButton_stop:
			findViewById(R.id.imageButton_stop).setEnabled(false);
			findViewById(R.id.imageButton_play).setEnabled(true);
			findViewById(R.id.imageButton_start).setEnabled(true);
			mRecorder.stop();
			break;
		}
	}

	@Override
	public void onStateChanged(int state) {
		// TODO Auto-generated method stub
		if (state == RecordHelper.PLAYING_END)
		{
			findViewById(R.id.imageButton_play).setEnabled(true);
			findViewById(R.id.imageButton_stop).setEnabled(false);
			findViewById(R.id.button_pass).setEnabled(true);
		}
	}

	@Override
	public void onError(int error) {
		// TODO Auto-generated method stub
		Log.d(TAG, String.valueOf(error));
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(RecorderActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    RecorderActivity.this.setResult(R.id.button_record,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_recorder),RecorderActivity.this,XmlUtils.activityClasses.get(0), false);
	    				RecorderActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
