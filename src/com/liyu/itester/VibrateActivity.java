package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class VibrateActivity extends Activity implements OnClickListener{
	TextView tv_vibrate;
	final String TAG = "VibrateActivity";
	ToggleButton tb;
	Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vibrate);
		initUI();
	}
	private void initUI()
	{
		tv_vibrate = (TextView)findViewById(R.id.textView_vibrate);
		tv_vibrate.setTextColor(Color.BLUE);
		tv_vibrate.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		tb = (ToggleButton)findViewById(R.id.toggleButton_vibrate);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if(!vibrator.hasVibrator())
		{
			findViewById(R.id.button_pass).setEnabled(false);
			tv_vibrate.setText(R.string.label_vibrator_none);
			tb.setEnabled(false);
			return;
		}
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					long[] pattern = { 100,100,100,100};    
					vibrator.vibrate(pattern, 0);
					tv_vibrate.setText(R.string.label_vibrator_start);
				}
				else
				{
					vibrator.cancel();
					tv_vibrate.setText(R.string.label_vibrator_stop);
				}
			}
		});
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
			this.setResult(R.id.button_vibrate,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_vibrate), VibrateActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_vibrate,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_vibrate), VibrateActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(vibrator!=null)
			vibrator.cancel();
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(VibrateActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    VibrateActivity.this.setResult(R.id.button_vibrate,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_vibrate),VibrateActivity.this,XmlUtils.activityClasses.get(0), false);
	    				VibrateActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
