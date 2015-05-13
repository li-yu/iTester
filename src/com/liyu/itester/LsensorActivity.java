package com.liyu.itester;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LsensorActivity extends Activity implements OnClickListener{
	final String TAG = "LsensorActivity";
	TextView tv_lsensor;
	private List<Sensor> sensors;
	private MSensorEvent mSebsorEvent;
	private SensorManager sm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsensor);
		 initUI();
		 timer.schedule(task, 5000);
    }
    
	private void initUI()
	{
		tv_lsensor = (TextView)findViewById(R.id.textView_lsensor);
		tv_lsensor.setTextColor(Color.BLUE);
		tv_lsensor.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sm == null)
		{
			tv_lsensor.setText("这台机器毛都没有!");
		}
		else
		{
			sensors = sm.getSensorList(Sensor.TYPE_LIGHT);
			Log.d(TAG, String.valueOf(sensors.size()));
			if (sensors.size()!=0){
				mSebsorEvent = new MSensorEvent();
				sm.registerListener(mSebsorEvent, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
				
				tv_lsensor.setText("Vendor: " + sensors.get(0).getVendor());
				tv_lsensor.append("\nName: " + sensors.get(0).getName());
				tv_lsensor.append("\nVersion: " + sensors.get(0).getVersion());
				tv_lsensor.append("\nPower: " + sensors.get(0).getPower() + "mA");
				tv_lsensor.append(getResources().getString(R.string.label_lightsensor_operation));
			}else
			{
				tv_lsensor.setText(R.string.label_lightsensor_none);
				uHandler.sendEmptyMessage(1);
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
			this.setResult(R.id.button_lsensor,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_lsensor), LsensorActivity.this,XmlUtils.activityClasses.get(0),  true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_lsensor,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_lsensor), LsensorActivity.this,XmlUtils.activityClasses.get(0),  false);
			this.finish();
			break;
		}
	}
	public class MSensorEvent implements SensorEventListener{
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		public void onSensorChanged(SensorEvent event) {
			tv_lsensor.setText("Vendor: " + event.sensor.getVendor());
			tv_lsensor.append("\nName: " + event.sensor.getName());
			tv_lsensor.append("\nVersion: " + event.sensor.getVersion());
			tv_lsensor.append("\nPower: " + event.sensor.getPower() + "mA");
			tv_lsensor.append("\n"+event.values[0]+" lux");
			findViewById(R.id.button_pass).setEnabled(true);
			tv_lsensor.setTextColor(Color.GREEN);
			uHandler.sendEmptyMessage(1);
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (sensors.size()!=0)	sm.unregisterListener(mSebsorEvent);
	}

	@Override
	protected void onResume()
	{
		if (sensors.size()!=0)	sm.registerListener(mSebsorEvent, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
		super.onResume();
	}
	
	private Handler uHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
				case 0: 
					tv_lsensor.setText(R.string.label_lightsensor_noneorexception);
					tv_lsensor.setTextColor(Color.RED);
				break;

				case 1:
					if (LsensorActivity.this.timer != null)
			        {
						LsensorActivity.this.timer.cancel();
						LsensorActivity.this.timer = null;
			        }
				break;	
			}
		}
	};
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			uHandler.sendEmptyMessage(0);
		}
	};
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(LsensorActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    LsensorActivity.this.setResult(R.id.button_lsensor,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_lsensor),LsensorActivity.this,XmlUtils.activityClasses.get(0), false);
	    				LsensorActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
