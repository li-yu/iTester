package com.liyu.itester;

import com.liyu.itester.compass.CompassActivity;
import com.liyu.itester.utils.jumpUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivityWiperReceiver extends DeviceAdminReceiver {
	static final int RESULT_ENABLE = 1;
public static class MainActivity extends Activity implements OnClickListener{
	final String TAG = "MainActivity" ;
	DevicePolicyManager mDPM;
	ComponentName mDeviceAdmin;
	int mode = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "oncreate");
		this.setTitle(R.string.hello_world);
		initUI();
		
	}

	private void initUI()
	{
		int[] ids = {R.id.button_hw,R.id.button_mac,R.id.button_bat,R.id.button_image,R.id.button_gsensor,R.id.button_brightness,
				R.id.button_rgb,R.id.button_multitouch,R.id.button_drawing,R.id.button_wifi,R.id.button_bt,R.id.button_sd,
				R.id.button_mp3,R.id.button_mp4,R.id.button_record,R.id.button_camera,R.id.button_lsensor,R.id.button_mobile,
				R.id.button_vibrate,R.id.button_flashlight,R.id.button_keyboard,R.id.button_compass,R.id.button_gyro,R.id.button_prox,
				R.id.button_gps,R.id.button_otg,R.id.button_ethernet,R.id.button_reset,R.id.button_fm};
		for(int i =0;i < ids.length;i++)
		{
			findViewById(ids[i]).setOnClickListener(this);
		}
		
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		mDeviceAdmin = new ComponentName(MainActivity.this,
				MainActivityWiperReceiver.class);
		//得到当前设备管理器有没有激活
		boolean active = mDPM.isAdminActive(mDeviceAdmin);
		if (!active) { 
			//如果没有激活的话，就去提示用户激活（第一次运行程序时）
			getAdmin();
		}
	}
	  @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(MainActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.label_confirmexit)
	    		.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				finish();
	    				System.exit(0);
	    			}
	    		})
	    		.setNegativeButton("No", new DialogInterface.OnClickListener(){

	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				dialog.dismiss();
	    			}
	    		}).show();
	    		return true;
	    	}
			return false;
	    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(jumpUtils.isFastDoubleClick())
			return;
		switch(v.getId())
		{
		case R.id.button_hw:
			startActivityForResult(new Intent(MainActivity.this, HwActivity.class), R.id.button_hw);
			break;
		case R.id.button_mac:
			startActivityForResult(new Intent(MainActivity.this, MacActivity.class), R.id.button_mac);
			break;
		case  R.id.button_bat:
			startActivityForResult(new Intent(MainActivity.this, BatActivity.class), R.id.button_bat);
			break;
		case R.id.button_image:
			startActivityForResult(new Intent(MainActivity.this, ImageActivity.class), R.id.button_image);
		    break;
		case R.id.button_gsensor:
			startActivityForResult(new Intent(MainActivity.this, GsensorActivity.class), R.id.button_gsensor);
			break;
		case R.id.button_brightness:
			startActivityForResult(new Intent(MainActivity.this, BrightnessActivity.class), R.id.button_brightness);
			break;
		case R.id.button_rgb:
			startActivityForResult(new Intent(MainActivity.this, RgbActivity.class), R.id.button_rgb);
			break;
		case R.id.button_multitouch:
			startActivityForResult(new Intent(MainActivity.this, MTActivity.class), R.id.button_multitouch);
			break;
		case R.id.button_drawing:
			startActivityForResult(new Intent(MainActivity.this, TouchActivity.class), R.id.button_drawing);
			break;
		case R.id.button_wifi:
			startActivityForResult(new Intent(MainActivity.this, WifiActivity.class), R.id.button_wifi);
			break;
		case R.id.button_bt:
			startActivityForResult(new Intent(MainActivity.this, BtActivity.class), R.id.button_bt);
			break;
		case R.id.button_sd:
			startActivityForResult(new Intent(MainActivity.this, SDActivity.class), R.id.button_sd);
			break;
		case R.id.button_mp3:
			startActivityForResult(new Intent(MainActivity.this, MP3Activity.class), R.id.button_mp3);
			break;
		case R.id.button_mp4:
			startActivityForResult(new Intent(MainActivity.this, MP4Activity.class), R.id.button_mp4);
			break;
		case R.id.button_record:
			startActivityForResult(new Intent(MainActivity.this, RecorderActivity.class), R.id.button_record);
			break;
		case R.id.button_camera:
			startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), R.id.button_camera);
			break;
		case R.id.button_lsensor:
			startActivityForResult(new Intent(MainActivity.this, LsensorActivity.class), R.id.button_lsensor);
			break;
		case R.id.button_mobile:
			startActivityForResult(new Intent(MainActivity.this, MobileNetActivity.class), R.id.button_mobile);
			break;
		case R.id.button_vibrate:
			startActivityForResult(new Intent(MainActivity.this, VibrateActivity.class), R.id.button_vibrate);
			break;
		case R.id.button_flashlight:
			startActivityForResult(new Intent(MainActivity.this, FlashlightActivity.class), R.id.button_flashlight);
			break;
		case R.id.button_keyboard:
			startActivityForResult(new Intent(MainActivity.this, KeyActivity.class), R.id.button_keyboard);
			break;
		case R.id.button_compass:
			startActivityForResult(new Intent(MainActivity.this, CompassActivity.class), R.id.button_compass);
			break;
		case R.id.button_gyro:
			startActivityForResult(new Intent(MainActivity.this, GyroscopeActivity.class), R.id.button_gyro);
			break;
		case R.id.button_prox:
			startActivityForResult(new Intent(MainActivity.this, ProximityActivity.class), R.id.button_prox);
			break;
		case R.id.button_gps:
			startActivityForResult(new Intent(MainActivity.this, GPSActivity.class), R.id.button_gps);
			break;
		case R.id.button_otg:
			startActivityForResult(new Intent(MainActivity.this, OTGActivity.class), R.id.button_otg);
			break;
		case R.id.button_ethernet:
			startActivityForResult(new Intent(MainActivity.this, EthernetActivity.class), R.id.button_ethernet);
			break;
		case R.id.button_fm:
			startActivityForResult(new Intent(MainActivity.this, FMActivity.class), R.id.button_fm);
			break;
		case R.id.button_reset:
			boolean active = mDPM.isAdminActive(mDeviceAdmin);
			if (!active) { 
				//如果没有激活的话，就去提示用户激活（第一次运行程序时）
				getAdmin();
			}
			else
			{
				chooseResetMode();
			}
			break;
		
		}
	}
	 public void getAdmin() {
         // Launch the activity to have the user enable our admin.
         Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
         intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                 mDeviceAdmin);
         intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                 "毓哥出品，必属精品!");
         startActivityForResult(intent, RESULT_ENABLE);
     }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null)
		{
			return;
		}
		
			boolean pass = data.getBooleanExtra("pass", false);
			Utils.setResult(findViewById(requestCode), pass);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.about:
			new AlertDialog.Builder(this)
			.setTitle(getText(R.string.menu_about))
			.setCancelable(false)
			.setMessage(getText(R.string.hello_world)+"\n\n"+getResources().getString(R.string.about_content))
			.setPositiveButton("OK", null)
			.create().show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void chooseResetMode()
	{
		mode = 1;
		new AlertDialog.Builder(this)
		.setTitle(R.string.title_warning)
		.setMultiChoiceItems(new String[]{getResources().getString(R.string.label_format)},new boolean[]{true}, new DialogInterface.OnMultiChoiceClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					mode = 1;
				}
				else
				{
					mode = 0;
				}
			}
		})
		.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				mDPM.wipeData(mode);
	    			}
	    		})
	    		.setNegativeButton("No", new DialogInterface.OnClickListener(){

	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				dialog.dismiss();
	    			}
	    		})
		.create().show();
	
	}
}
}
