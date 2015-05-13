package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BatActivity extends Activity implements OnClickListener{
	
	TextView tv_bat;
	final String TAG = "BatActivity";
	int standardLevel = 60;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bat);
		
		initUI();
		
	}
	
	private void initUI()
	{
		tv_bat = (TextView)findViewById(R.id.textView_bat);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		try
		{
			standardLevel = Integer.parseInt(XmlUtils.getEntity(this.getClass().getSimpleName()).getParameter().toString());
		}
		catch(Exception e)
		{
			
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
        registerReceiver(mBatInfoReceiver, 
        		new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	  }

	  @Override
	  protected void onPause() {
	    super.onPause();
	    unregisterReceiver(mBatInfoReceiver);
	  }
	  
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)){
			
				int status = intent.getIntExtra("status", 0);
				int health = intent.getIntExtra("health", 1);
				int level = intent.getIntExtra("level", 0);
				int plugged = intent.getIntExtra("plugged", 0);
				int voltage = intent.getIntExtra("voltage", 0);
				int temperature = intent.getIntExtra("temperature", 0);
				String technology = intent.getStringExtra("technology");
		      
				String statusString = "unknown";
			          
			switch (status) {
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					statusString = "unknown";
					break;
				case BatteryManager.BATTERY_STATUS_CHARGING:
					statusString = "Charging";
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
					statusString = "Discharging";
					break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					statusString = "Discharging";
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					statusString = "Full";
					break;
			}
			        
			String healthString = "unknown";
			        
			switch (health) {
				case BatteryManager.BATTERY_HEALTH_UNKNOWN:
					healthString = "unknown";
					break;
				case BatteryManager.BATTERY_HEALTH_GOOD:
					healthString = "Good";
					break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT:
					healthString = "Over heat";
					break;
				case BatteryManager.BATTERY_HEALTH_DEAD:
					healthString = "Dead";
					break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
					healthString = "Over voltage";
					break;
				case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
					healthString = "Unspecified failure";
					break;
			}
			        
			String acString = "Unknown";
			        
			switch (plugged) {
				case BatteryManager.BATTERY_PLUGGED_AC:
					acString = "AC";
					break;
				case BatteryManager.BATTERY_PLUGGED_USB:
					acString = "USB";
					break;
			}
			tv_bat.setTextColor(Color.BLUE);
			tv_bat.setTextSize(30);
			tv_bat.setText(	  "Status: " + statusString +
							  "\nLevel: " + String.valueOf(level) + "%" +
						      "\nHealth: " + healthString +
			        		  "\nPlugged: " + acString +
			        		  "\nVoltage: " + String.valueOf(voltage) + "mV" +
			        		  "\nTemperature: " + String.valueOf(temperature) + "¢J" +
			        		  "\nTechnology: " + technology);
			if(FunctionApplication.isAutoMode)
			{
			  if(level<standardLevel)
			  {
				tv_bat.setTextColor(Color.RED);
				findViewById(R.id.button_pass).setEnabled(false);
			  }
			  else
			  {
				tv_bat.setTextColor(Color.GREEN);
				findViewById(R.id.button_pass).setEnabled(true);
			  }
			}
			
			}
	}
};

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
			this.setResult(R.id.button_bat,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bat), BatActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_bat,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bat), BatActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(BatActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    BatActivity.this.setResult(R.id.button_bat,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bat),BatActivity.this,XmlUtils.activityClasses.get(0), false);
	    				BatActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
