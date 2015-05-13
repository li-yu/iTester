package com.liyu.itester;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProximityActivity extends Activity implements OnClickListener,SensorEventListener{
	TextView tv_prox;
	final String TAG = "ProximityActivity";
	private SensorManager mSensor;
	private Sensor ProxSensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proximity);
		initUI();
		timer.schedule(task, 5000);
		}
		
		private void initUI()
		{
			tv_prox = (TextView)findViewById(R.id.textView_prox);
			tv_prox.setTextColor(Color.BLUE);
			tv_prox.setTextSize(30);
			findViewById(R.id.button_pass).setOnClickListener(this);
			findViewById(R.id.button_fail).setOnClickListener(this);
			findViewById(R.id.button_pass).setEnabled(false);
			mSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
	        ProxSensor = mSensor.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	        if(ProxSensor==null)
	        {
	        	tv_prox.setText(R.string.label_prox_none);
	        	findViewById(R.id.button_pass).setEnabled(false);
	        	uHandler.sendEmptyMessage(1);
	        }
	        else
	        {
	        	tv_prox.setText("Vendor: " + ProxSensor.getVendor());
				tv_prox.append("\nName: " + ProxSensor.getName());
				tv_prox.append("\nVersion: " + ProxSensor.getVersion());
				tv_prox.append("\nPower: " + ProxSensor.getPower() + "mA");
				tv_prox.append(getResources().getString(R.string.label_prox_operation));
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
				this.setResult(R.id.button_prox,intent);
				this.finish();
				break;
			case R.id.button_fail:
				intent.putExtra("pass", false);
				this.setResult(R.id.button_prox,intent);
				this.finish();
				break;
			}
		}
		
		@Override
		public void onResume() {
	        super.onResume();
	        if(ProxSensor!=null)
	        mSensor.registerListener(this, ProxSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }
		
		@Override
		public void onPause() {
	        super.onPause();
	        if(ProxSensor!=null)
	        mSensor.unregisterListener(this);
	    }

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			tv_prox.setText("Vendor: " + event.sensor.getVendor());
			tv_prox.append("\nName: " + event.sensor.getName());
			tv_prox.append("\nVersion: " + event.sensor.getVersion());
			tv_prox.append("\nPower: " + event.sensor.getPower() + "mA");
			tv_prox.append("\nValue: " + event.values[0]+" cm");
			findViewById(R.id.button_pass).setEnabled(true);
			uHandler.sendEmptyMessage(1);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		
		private Handler uHandler = new Handler(){
			@Override
			public void handleMessage(android.os.Message msg) {
				switch(msg.what)
				{
					case 0: 
						tv_prox.setText(R.string.label_prox_noneorexception);
					break;

					case 1:
						if (ProximityActivity.this.timer != null)
				        {
							ProximityActivity.this.timer.cancel();
							ProximityActivity.this.timer = null;
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
		    		new AlertDialog.Builder(ProximityActivity.this)
		    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
		    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
		    			public void onClick(DialogInterface dialog, int whichButton)
		    			{
		    				Intent intent = new Intent();
		    				intent.putExtra("pass", false);
		    			    ProximityActivity.this.setResult(R.id.button_prox,intent);
		    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_proximity),ProximityActivity.this,XmlUtils.activityClasses.get(0), false);
		    				ProximityActivity.this.finish();
		    			}
		    		})
		    		.show();
		    		return true;
		    	}
				return false;
		    }
}
