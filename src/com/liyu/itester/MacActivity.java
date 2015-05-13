package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MacActivity extends Activity implements OnClickListener{

	final String TAG = "HwActivity";
	private TextView tv_mac;
	private WifiManager mWifi;
	private WifiInfo wifiInfo ;
	private BluetoothAdapter bAdapt;
	private String btMac, WifiMac,IMEI;
	private String para_wifi,para_bt,para_imei;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mac);
		
		try {
			initUI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 tv_mac.setTextColor(Color.BLUE);
		 tv_mac.setTextSize(30);
		 tv_mac.setText("WiFi mac: "+WifiMac+"\n"+"Bluetooth mac: "+btMac+"\n"+"IMEI: "+IMEI+"\n"+"Serial Number: "+Build.SERIAL);
		 checkStatus();
	}

	public void initUI()
	{
		tv_mac = (TextView)findViewById(R.id.textView_mac);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setOnClickListener(this);
		
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE); 
        mWifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        IMEI=tm.getDeviceId();

        wifiInfo = mWifi.getConnectionInfo();
        
        bAdapt= BluetoothAdapter.getDefaultAdapter();
       
        if (bAdapt != null)
        {
            if (!bAdapt.isEnabled())
            {
            	bAdapt.enable();
            }
            
            btMac = bAdapt.getAddress();
        }else{
        	btMac = "No Bluetooth Device!";
        }
       
        if((WifiMac = wifiInfo.getMacAddress())== null)
        {
        	WifiMac = "Turning on...";
        }
        
        
	}
	public void checkStatus()
	{
		 if(FunctionApplication.isAutoMode)
		 {
			 para_bt = FunctionApplication.tce.getBTAddressStart().toUpperCase();
			 para_wifi = FunctionApplication.tce.getWiFiAddressStart().toUpperCase();
			 para_imei = FunctionApplication.tce.getIMEIStart();
			 if(btMac.toUpperCase().startsWith(para_bt)&&WifiMac.toUpperCase().startsWith(para_wifi)&&IMEI.startsWith(para_imei))
			 {
				 tv_mac.setTextColor(Color.GREEN);
				 if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mac), MacActivity.this, XmlUtils.activityClasses.get(0), true);
				 MacActivity.this.finish();
			 }
			 else
			 {
				 tv_mac.setTextColor(Color.RED);
				 findViewById(R.id.button_pass).setEnabled(false);
			 }
		 }
	}
	private BroadcastReceiver scanResultsReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();

			if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
			{
				Log.d(TAG, "wifi state changed......");
			if(mWifi.isWifiEnabled())
			{
				wifiInfo = mWifi.getConnectionInfo();
				if((WifiMac = wifiInfo.getMacAddress())== null)
		        {
					WifiMac = wifiInfo.getMacAddress();
		        }
				tv_mac.setText("WiFi mac: "+WifiMac+"\n"+"Bluetooth mac: "+btMac+"\n"+"IMEI: "+IMEI+"\n"+"Serial Number: "+Build.SERIAL);
				checkStatus();
			}
			}
			if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
			{
				Log.d(TAG, "bt state changed......");
			if(bAdapt.isEnabled())
			{
				btMac = bAdapt.getAddress();
				tv_mac.setText("WiFi mac: "+WifiMac+"\n"+"Bluetooth mac: "+btMac+"\n"+"IMEI: "+IMEI+"\n"+"Serial Number: "+Build.SERIAL);
				checkStatus();
			}
			}
			if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
			{
				if(mWifi.isWifiEnabled())
				{
					wifiInfo = mWifi.getConnectionInfo();
					if((WifiMac = wifiInfo.getMacAddress())== null)
			        {
						WifiMac = wifiInfo.getMacAddress();
			        }
					tv_mac.setText("WiFi mac: "+WifiMac+"\n"+"Bluetooth mac: "+btMac+"\n"+"IMEI: "+IMEI+"\n"+"Serial Number: "+Build.SERIAL);
					checkStatus();
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
			this.setResult(R.id.button_mac,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mac), MacActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_mac,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mac), MacActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(scanResultsReceiver, intentFilter);
		if(mWifi!=null&&!mWifi.isWifiEnabled())
		{
			mWifi.setWifiEnabled(true);
		}
		if(bAdapt!=null&&!bAdapt.isEnabled())
		{
			bAdapt.enable();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(scanResultsReceiver);
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(MacActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    MacActivity.this.setResult(R.id.button_mac,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_mac),MacActivity.this,XmlUtils.activityClasses.get(0), false);
	    				MacActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
