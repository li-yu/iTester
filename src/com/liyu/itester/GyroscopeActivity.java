package com.liyu.itester;

import java.util.Timer;
import java.util.TimerTask;
import com.liyu.itester.gyro.TouchSurfaceView;
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

public class GyroscopeActivity extends Activity implements OnClickListener,SensorEventListener{
	TextView tv_gyroscope;
	final String TAG = "GyroscopeActivity";
	private SensorManager mSensor;
	private Sensor GyroSensor;
	private TouchSurfaceView mGLSurfaceView;
	private float x, y, z = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gyroscope);
		initUI();
		timer.schedule(task, 2000);
	}
	
	private void initUI()
	{
		mGLSurfaceView = (TouchSurfaceView)findViewById(R.id.tsv);

		tv_gyroscope = (TextView)findViewById(R.id.textView_gyroscope);
		tv_gyroscope.setTextColor(Color.BLUE);
		tv_gyroscope.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		mSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        GyroSensor = mSensor.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(GyroSensor==null)
        {
        	tv_gyroscope.setText(R.string.label_gyro_none);
        	findViewById(R.id.button_pass).setEnabled(false);
        	uHandler.sendEmptyMessage(1);
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
			this.setResult(R.id.button_gyro,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gyroscope), GyroscopeActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_gyro,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gyroscope), GyroscopeActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		tv_gyroscope.setText("Vendor: " + event.sensor.getVendor());
		tv_gyroscope.append("\nName: " + event.sensor.getName());
//		tv_gyroscope.append("\nVersion: " + event.sensor.getVersion());
//		tv_gyroscope.append("\nPower: " + event.sensor.getPower() + "mA");
//		tv_gyroscope.append("\nX: " + event.values[0]);
//		tv_gyroscope.append("\nY: " + event.values[1]);
//		tv_gyroscope.append("\nZ: " + event.values[2]);
    	
		y += event.values[0];
		x += event.values[1];
		z += event.values[2];

		mGLSurfaceView.updateGyro(x, y, z);
		
		findViewById(R.id.button_pass).setEnabled(true);
		tv_gyroscope.setTextColor(Color.GREEN);
		uHandler.sendEmptyMessage(1);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onResume() {
        super.onResume();
        if(GyroSensor!=null)
        mSensor.registerListener(this, GyroSensor, SensorManager.SENSOR_DELAY_GAME);
        mGLSurfaceView.onResume();
    }
	
	@Override
	public void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
        if(GyroSensor!=null)
        mSensor.unregisterListener(this);
        
    }
	
	private  Handler uHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
				case 0: 
					tv_gyroscope.setText(R.string.label_gyro_noneorexception);
					tv_gyroscope.setTextColor(Color.RED);
				break;

				case 1:
					if (GyroscopeActivity.this.timer != null)
			        {
						GyroscopeActivity.this.timer.cancel();
						GyroscopeActivity.this.timer = null;
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
	    		new AlertDialog.Builder(GyroscopeActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    GyroscopeActivity.this.setResult(R.id.button_gyro,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gyroscope),GyroscopeActivity.this,XmlUtils.activityClasses.get(0), false);
	    				GyroscopeActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
