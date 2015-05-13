package com.liyu.itester;

import com.liyu.itester.utils.InterAddressUtil;
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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class EthernetActivity extends Activity implements OnClickListener{
    TextView tv ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ethernet);
		tv = (TextView)findViewById(R.id.textView_ethernet);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
				 ConnectivityManager cm = (ConnectivityManager)EthernetActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
				 NetworkInfo ethernetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET); 
		            if(ethernetInfo!=null&&ethernetInfo.isConnected()){
		                //连接状态
		                tv.setText(R.string.label_ethernet_connectted);
		                tv.setTextColor(Color.GREEN);
		                //tv.append("\nIP: "+InterAddressUtil.getLocalIpAddress());
		                tv.append("\nMac address: "+InterAddressUtil.getMacAddress());
		                findViewById(R.id.button_pass).setEnabled(true);
		            } else {  
		            	tv.setText(R.string.label_ethernet_disconnectted);
		            	tv.setTextColor(Color.RED);
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
			this.setResult(R.id.button_ethernet,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_ethernet), EthernetActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_ethernet,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_ethernet), EthernetActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		new AlertDialog.Builder(EthernetActivity.this)
    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
    			public void onClick(DialogInterface dialog, int whichButton)
    			{
    				Intent intent = new Intent();
    				intent.putExtra("pass", false);
    			    EthernetActivity.this.setResult(R.id.button_ethernet,intent);
    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_ethernet),EthernetActivity.this,XmlUtils.activityClasses.get(0), false);
    				EthernetActivity.this.finish();
    			}
    		})
    		.show();
    		return true;
    	}
		return false;
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ConnectivityManager cm = (ConnectivityManager)EthernetActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo ethernetInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET); 
           if(ethernetInfo!=null&&ethernetInfo.isConnected()){
               //连接状态
               tv.setText(R.string.label_ethernet_connectted);
               tv.setTextColor(Color.GREEN);
               //tv.append("\nIP: "+InterAddressUtil.getLocalIpAddress());
               tv.append("\nMac address: "+InterAddressUtil.getMacAddress());
               findViewById(R.id.button_pass).setEnabled(true);
           } else {  
           	tv.setText(R.string.label_ethernet_disconnectted);
           	tv.setTextColor(Color.RED);
           } 
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, filter);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(mReceiver);
	}

}
