package com.liyu.itester;

import java.util.Timer;
import java.util.TimerTask;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BtActivity extends Activity implements OnClickListener{

	final String TAG = "BtActivity";
	TextView tv_bt;
	private BluetoothAdapter ba;
	Timer mTimer;
	TimerTask mTask;
	Handler uHandler;
	int btNum = 0;
	int count = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bt);
		initUI();
		timer.schedule(task, 3000);
	}
	private void initUI()
	{
		
		ba = BluetoothAdapter.getDefaultAdapter();
		tv_bt = (TextView)findViewById(R.id.textView_bt);
		tv_bt.setTextColor(Color.BLUE);
		tv_bt.setTextSize(30);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		uHandler = new Handler(){
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0:
				tv_bt.setText(getResources().getString(R.string.label_nobtdevice)+count-- +getResources().getString(R.string.label_btresearch));
				break;
				case 1:
					if (BtActivity.this.timer != null)
			        {
						BtActivity.this.timer.cancel();
						BtActivity.this.timer = null;
			        }
					break;
				case 2:
					tv_bt.setText(getResources().getString(R.string.label_nobt_or_cannotopen));
					break;
				}
				}
			};
		initTimer();
		
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
			this.setResult(R.id.button_bt,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bt), BtActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_bt,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bt), BtActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	
	private BroadcastReceiver scanResultsReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();

			if (action.equals(BluetoothDevice.ACTION_FOUND) )
			{
				uHandler.sendEmptyMessage(1);
				Log.d(TAG, "Discovery result...");
				tv_bt.setText(getResources().getString(R.string.label_bt_list));
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      		    String str=device.getName();
				tv_bt.append("\n"+str);
				btNum ++;
				findViewById(R.id.button_pass).setEnabled(true);
				if(FunctionApplication.isAutoMode){
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bt), BtActivity.this, XmlUtils.activityClasses.get(0), true);
				BtActivity.this.finish();}
			}
			if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
			{
				uHandler.sendEmptyMessage(1);
				Log.d(TAG, "bt state changed......");
			if(ba.isEnabled())
			{
				ba.startDiscovery();
			}
			}
			if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED))
			{
				uHandler.sendEmptyMessage(1);
				Log.d(TAG, "start search。。。");
				tv_bt.setText(getResources().getString(R.string.label_bt_seraching));
				if (BtActivity.this.mTimer != null)
		        {
					BtActivity.this.mTimer.cancel();
					BtActivity.this.mTimer = null;
		        }
				count = 5 ;
				btNum = 0;
			}
			if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
			{
				uHandler.sendEmptyMessage(1);
				Log.d(TAG, "end search。。。");
				tv_bt.append(getResources().getString(R.string.label_bt_searchedone));
				if(btNum==0)
				{
					findViewById(R.id.button_pass).setEnabled(false);
					mTimer = null;
					mTask = null;
					initTimer();
				    mTimer.schedule(mTask, 1000, 1000);
					 
				}
			}
		}

	};
	
	private void initTimer()
	{
		mTimer = new Timer();
		mTask = new TimerTask() {
			public void run() {
				if(count < 0)
				{
					ba.startDiscovery();
				}
				else
				uHandler.sendEmptyMessage(0);
			}
		};
	}
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
       	filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
       	filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
       	filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(scanResultsReceiver, filter);
		if(ba==null)
		{
			tv_bt.setText(getResources().getString(R.string.label_bt_none));
			findViewById(R.id.button_pass).setEnabled(false);
		}
		if(ba!=null&&!ba.isEnabled())
		{
			ba.enable();
		}
		if (ba!= null&&ba.isEnabled())
		{
			if(!ba.isDiscovering())
			{
				ba.startDiscovery();
			}
			else 
			{
				ba.cancelDiscovery();
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(scanResultsReceiver);
		if (BtActivity.this.mTimer != null)
        {
			BtActivity.this.mTimer.cancel();
			BtActivity.this.mTimer = null;
        }
	}
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			uHandler.sendEmptyMessage(2);
		}
	};
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(BtActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    BtActivity.this.setResult(R.id.button_bt,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_bt),BtActivity.this,XmlUtils.activityClasses.get(0), false);
	    				BtActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
