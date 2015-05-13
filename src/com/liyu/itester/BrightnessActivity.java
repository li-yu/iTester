package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BrightnessActivity extends Activity implements OnClickListener,OnSeekBarChangeListener{
	TextView tv_brightness;
	final String TAG = "BrightnessActivity";
	private SeekBar seekbar;
	int brightness = 10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brightness);
		initUI();
	}
	
	private void initUI()
	{
		tv_brightness = (TextView)findViewById(R.id.textView_brightness);
		tv_brightness.setTextColor(Color.BLUE);
		tv_brightness.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		seekbar = (SeekBar)findViewById(R.id.seekBar_brightness);
		seekbar.setOnSeekBarChangeListener(this);
		tv_brightness.setText("屏幕亮度: "+getScreenBrightness());
		seekbar.setProgress(getScreenBrightness());
		brightness = getScreenBrightness();
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
			this.setResult(R.id.button_brightness,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_brightness), BrightnessActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_brightness,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_brightness), BrightnessActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		 if(progress<10)
		 {
			 progress=10;
		 }
		 tv_brightness.setText(getResources().getString(R.string.label_brightness)+progress);
		 setScreenBrightness(progress);
		 brightness = progress;
		 
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		findViewById(R.id.button_pass).setEnabled(true);
	}

	/** 
     * 获得当前屏幕亮度值  0--255 
     */  
      private int getScreenBrightness(){  
        int screenBrightness=255;  
        try{  
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);  
        }  
        catch (Exception localException){  
            
        }  
        return screenBrightness;  
      }  
    
     
      private void saveScreenBrightness(int paramInt){  
        try{  
          Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);  
        }  
        catch (Exception localException){  
          localException.printStackTrace();  
        }  
      }  
      
      /** 
       * 保存当前的屏幕亮度值，并使之生效 
       */  
      private void setScreenBrightness(int paramInt){  
        Window localWindow = getWindow();  
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();  
        float f = paramInt / 255.0F;  
        localLayoutParams.screenBrightness = f;  
        localWindow.setAttributes(localLayoutParams);  
      }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		saveScreenBrightness(brightness);
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(BrightnessActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    BrightnessActivity.this.setResult(R.id.button_brightness,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_brightness),BrightnessActivity.this,XmlUtils.activityClasses.get(0), false);
	    				BrightnessActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
