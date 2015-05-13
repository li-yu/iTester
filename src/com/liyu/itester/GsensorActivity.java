package com.liyu.itester;

import java.util.Locale;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GsensorActivity extends Activity implements OnClickListener,SensorEventListener{
	TextView tv_gsensor;
	final String TAG = "GsensorActivity";
	private SensorManager mSensor;
	private Sensor gSensor;
	private int isChanged = 0;
	private int rorateTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gsensor);
		initUI();
	}
	
	private void initUI()
	{
		tv_gsensor = (TextView)findViewById(R.id.textView_gsensor);
		tv_gsensor.setTextColor(Color.BLUE);
		tv_gsensor.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		try
		{
			rorateTime = Integer.parseInt(XmlUtils.getEntity(this.getClass().getSimpleName()).getParameter().toString());
		}
		catch(Exception e)
		{
			
		}
		mSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        gSensor = mSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
			this.setResult(R.id.button_gsensor,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gsensor),GsensorActivity.this, XmlUtils.activityClasses.get(0),true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_gsensor,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gsensor),GsensorActivity.this, XmlUtils.activityClasses.get(0),false);
			this.finish();
			break;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if(!FunctionApplication.isAutoMode||rorateTime==0)
		{
		findViewById(R.id.button_pass).setEnabled(true);
		}
		tv_gsensor.setText("Vendor: " + event.sensor.getVendor());
		tv_gsensor.append("\nName: " + event.sensor.getName());
		tv_gsensor.append("\nVersion: " + event.sensor.getVersion());
		tv_gsensor.append("\nPower: " + event.sensor.getPower() + "mA");
		tv_gsensor.append("\nX: " + event.values[0]);
		tv_gsensor.append("\nY: " + event.values[1]);
		tv_gsensor.append("\nZ: " + event.values[2]);
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		loadMyConfig();
		if ( ++isChanged == rorateTime)
		  {
			Intent intent = new Intent();
			intent.putExtra("pass", true);
			this.setResult(R.id.button_gsensor,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo("Gsensor",GsensorActivity.this, XmlUtils.activityClasses.get(0),true);
			this.finish();
		  }
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResume() {
        super.onResume();
        mSensor.registerListener(this, gSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
	
	@Override
	public void onPause() {
        super.onPause();
        mSensor.unregisterListener(this);
    }
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(GsensorActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    GsensorActivity.this.setResult(R.id.button_gsensor,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gsensor),GsensorActivity.this,XmlUtils.activityClasses.get(0), false);
	    				GsensorActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
	 
	 public void loadMyConfig()
		{
			if(FunctionApplication.tce!=null)
			{
				if(FunctionApplication.tce.getLanguage().toString().equalsIgnoreCase("chinese"))
				{
					toChinese();
				}
				else
				{
					toEnglish();
				}
			}
			else
			{
			       toChinese();
			}
		}

		public void toEnglish()
		{
			String languageToLoad  = "en_US"; 
	        Locale locale = new Locale(languageToLoad);  
	        Locale.setDefault(locale);  
	        Configuration config = getResources().getConfiguration();  
	        DisplayMetrics metrics = getResources().getDisplayMetrics();  
	        config.locale = Locale.ENGLISH;  
	        getResources().updateConfiguration(config, metrics); 
		}
		
		public void toChinese()
		{
			String languageToLoad  = "zh"; 
	        Locale locale = new Locale(languageToLoad);  
	        Locale.setDefault(locale);  
	        Configuration config = getResources().getConfiguration();  
	        DisplayMetrics metrics = getResources().getDisplayMetrics();  
	        config.locale = Locale.SIMPLIFIED_CHINESE;  
	        getResources().updateConfiguration(config, metrics); 
		}
}
