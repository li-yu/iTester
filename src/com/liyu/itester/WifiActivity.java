package com.liyu.itester;


import java.util.List;

import com.liyu.itester.utils.WifiAdmin;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WifiActivity extends Activity implements OnClickListener{

	final String TAG = "MTActivity";
	TextView tv_wifi;
	private WifiManager mWifi;
	private Boolean isSet = false;
	private List<ScanResult> sRet;
	private int testMode = 0;//manual test mode is 0
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);
		 initUI();
    }
    
	private void initUI()
	{
		mWifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		tv_wifi = (TextView)findViewById(R.id.textView_wifi);
		tv_wifi.setTextColor(Color.BLUE);
		tv_wifi.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_wifi_connect).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		if(FunctionApplication.isAutoMode)
			try {
				testMode = Integer.parseInt(XmlUtils.getEntity(this.getClass().getSimpleName()).getParameter().toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				testMode = 0;
			}
	}
    private void gotoWifiPicker()
    {
	Intent intent = new Intent();
	//intent.setClassName("com.android.settings","com.android.settings.wifi.WifiPickerActivity");
	intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
	intent.putExtra("extra_prefs_show_button_bar", true);
	//intent.putExtra("extra_prefs_set_next_text", "完成");
	//intent.putExtra("extra_prefs_set_back_text", "返回");
	intent.putExtra("wifi_enable_next_on_connect", true);
	startActivity(intent);
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
			this.setResult(R.id.button_multitouch,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_wifi), WifiActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_multitouch,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_wifi), WifiActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		case R.id.button_wifi_connect:
			if(testMode==0)
			gotoWifiPicker();
			else
			{
				if(mWifi!=null&&!mWifi.isWifiEnabled())
				{
					mWifi.setWifiEnabled(true);
				}
				else if (mWifi.isWifiEnabled())
				{
					mWifi.startScan();
				}
			}
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(scanResultsReceiver, intentFilter);
		if(mWifi!=null&&!mWifi.isWifiEnabled())
		{
			mWifi.setWifiEnabled(true);
		}
		else if (mWifi.isWifiEnabled())
		{
			mWifi.startScan();
		}
	}

	
	private Handler uHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
				case 0: /* get scanResults */
					WifiInfo info = (null == mWifi ? null : mWifi.getConnectionInfo());
		        	if (null != info) {
		        	    String SSID= info.getSSID();
		        	    String Status=SSID==null?"Disconnected":"Connected";
		            	int  IPint= info.getIpAddress();
		        	    String IP = "" + (IPint & 0xFF) + "." + ((IPint >> 8) & 0xFF) + "." + ((IPint >> 16) & 0xFF) + "." + ((IPint >> 24) & 0xFF); 
		        	    String Speed=String.valueOf(info.getLinkSpeed())+"Mbps";
		        	    String Mac=info.getMacAddress();
		        	    int Rssi=info.getRssi();
		        	    tv_wifi.setTextColor(Color.GREEN);
		        	    tv_wifi.setText(R.string.label_wifi_connected_info);
		        	    tv_wifi.append("\nSSID: "+SSID);
		        	    tv_wifi.append("\nStatus: "+Status);
		        	    tv_wifi.append("\nSpeed: "+Speed);
		        	    tv_wifi.append("\nSignal: "+String.valueOf(Rssi)+" dBm");
		        	    tv_wifi.append("\nIP address: "+IP);
		        	    tv_wifi.append("\nMac address: "+Mac);
		        	    findViewById(R.id.button_pass).setEnabled(true);
//		        	    if(testMode==1)
//		        	    {
//							if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_wifi), WifiActivity.this, XmlUtils.activityClasses.get(0), true);
//							WifiActivity.this.finish();
//		        	    }
		        	}
				break;

				case 1: /* timeout or wifi not found */
					if(!FunctionApplication.isAutoMode)
						return;
					tv_wifi.setText(R.string.label_wifi_ap_none);
					tv_wifi.setTextColor(Color.RED);
				break;

				case 2: /*  */
					if (!mWifi.isWifiEnabled())
					{
						mWifi.setWifiEnabled(true);
					}
				break;
				
				case 3:
		        break;
		        
				case 4:
					tv_wifi.setText(R.string.label_wifi_ap_outofrange);
					tv_wifi.setTextColor(Color.RED);
					
			}
		}
	};
	private BroadcastReceiver scanResultsReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();

			if (action.equals("android.net.wifi.SCAN_RESULTS") )
			{
				sRet = mWifi.getScanResults();
				int num = sRet.size();
				String apNames= "";
				if (num > 0)
				{
					//手动测试模式
					if(testMode == 0)
					{
						findViewById(R.id.button_pass).setEnabled(true);
						return;
					}
					else if(testMode>1)//自动测试模式：搜索测试
					{
						tv_wifi.setText("");
						for (int i=0; i< num; i++)
						{
							tv_wifi.append(sRet.get(i).SSID+"\n");
							apNames = apNames+sRet.get(i).SSID;
						}
						
						if(num>=testMode)
						{
						   findViewById(R.id.button_pass).setEnabled(true);
						   if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_wifi), WifiActivity.this, XmlUtils.activityClasses.get(0), true);
						   WifiActivity.this.finish();
						}
						else
						{
							findViewById(R.id.button_pass).setEnabled(false);
							
						}return;
					}
					else if(testMode==1)//自动测试：连接测试
					{
						tv_wifi.setText("");
						for (int i=0; i< num; i++)
						{
							tv_wifi.append(sRet.get(i).SSID+"\n");
							apNames = apNames+sRet.get(i).SSID;
						}
						
						if((!apNames.contains(FunctionApplication.tce.getWiFiSSID())))
						uHandler.sendEmptyMessage(4);
						else if(!isSet)
						{
							isSet = true;
							connectWiFi();
						}
					}
					
				}
				else
				{
					uHandler.sendEmptyMessage(1);
				}
			}
			if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
			{
				Log.d(TAG, "wifi state changed......");
			}
			if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
				 ConnectivityManager cm = (ConnectivityManager)WifiActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
				 NetworkInfo wifiNetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		            if(wifiNetInfo.isConnected()){
		                //连接状态
		                uHandler.sendEmptyMessage(0);
		            } else if (isSet){  
	                	uHandler.sendEmptyMessage(1);
	                } 
	        }
		}

	};
	public void connectWiFi()
	{
		  new Thread()
		    {
		      public void run()
		      {
		    	  WifiAdmin wifiAdmin = new WifiAdmin(WifiActivity.this);  
		          wifiAdmin.openWifi(); 
		          WifiConfiguration wcg=wifiAdmin.CreateWifiInfo(FunctionApplication.tce.getWiFiSSID(), FunctionApplication.tce.getWiFiPassWord(),3);
	              wifiAdmin.addNetwork(wcg);
		      }
		    }
		    .start();
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
	    		new AlertDialog.Builder(WifiActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    				WifiActivity.this.setResult(R.id.button_bt,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_wifi),WifiActivity.this,XmlUtils.activityClasses.get(0), false);
	    				WifiActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}



