package com.liyu.itester;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class GPSActivity extends Activity implements OnClickListener{
	final String TAG = "GPSActivity";
	TextView tv_gps,tv_satNUM;
	private StringBuilder sb; 
	LocationManager locationManager;
	LocationListener ll;
	String provider;
	private Timer timer;
	private TimerTask task;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		initUI();
		
    }
    
	private void initUI()
	{
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setEnabled(false);
		tv_gps = (TextView)findViewById(R.id.textView_gps);
		tv_satNUM = (TextView)findViewById(R.id.textView_satNUM);
		tv_gps.setTextColor(Color.BLUE);
		tv_gps.setTextSize(30);
		
		if(!hasGPSDevice(GPSActivity.this))
		{
			tv_gps.setText(R.string.label_gps_none);
			tv_satNUM.setVisibility(View.GONE);
			return;
		}
		    openGPSSettings(); 
	        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
	        provider = LocationManager.GPS_PROVIDER;  
	        Location location = locationManager.getLastKnownLocation(provider);  
	        updateMsg(location);  
	        ll = new LocationListener()
	        {

				public void onLocationChanged(Location location) {
					// TODO Auto-generated method stub
					uHandler.sendEmptyMessage(1);
					String locInfo = updateMsg(location);  
	                tv_gps.setText(null);  
	                tv_gps.setText(locInfo);  
	                findViewById(R.id.button_pass).setEnabled(true);
				}

				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}
	        	
	        };
	        
	        
	}
	private String updateMsg(Location loc) {  
        sb = null;  
        sb = new StringBuilder("Location:\n");  
        if (loc != null) {  
            double lat = loc.getLatitude();  
            double lng = loc.getLongitude();  
  
            sb.append("Latitude: " + lat + "\nLongitude: " + lng);  
  
            if (loc.hasAccuracy()) {  
                sb.append("\nAccuracy: " + loc.getAccuracy());  
            }  
  
            if (loc.hasAltitude()) { 
                sb.append("\nHeight: " + new DecimalFormat("#.#").format(loc.getAltitude()) + "m");  
            }  
  
            if (loc.hasBearing()) {// 偏离正北方向的角度  
                sb.append("\nDirection: " + loc.getBearing());  
            }  
  
            if (loc.hasSpeed()) {  
                if (loc.getSpeed() * 1.852 < 5) {  
                    sb.append("\nSpeed: 0.0km/h");  
                } else {  
                    sb.append("\nSpeed: " + loc.getSpeed() * 1.852 + "km/h");  
                }  
  
            }  
        } else {  
            sb.append("No location info");  
        }  
  
        return sb.toString();  
    }  
  
    private void openGPSSettings() {  
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {  
            return;  
        }  
        Toast.makeText(this, R.string.label_gps_turnon, Toast.LENGTH_SHORT).show(); 
        
         Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
         startActivityForResult(intent, 0); 
        
    }  
    
    /** 
     * 卫星状态监听器 
     */  
    private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // 卫星信号  
      
    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {  
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数  
            LocationManager locationManager = (LocationManager) GPSActivity.this.getSystemService(Context.LOCATION_SERVICE);  
            GpsStatus status = locationManager.getGpsStatus(null); //取当前状态  
            String satelliteInfo = updateGpsStatus(event, status);  
            tv_satNUM.setText(null);  
            tv_satNUM.setText(satelliteInfo);  
        }  
    };  
  
    private String updateGpsStatus(int event, GpsStatus status) {  
        StringBuilder sb2 = new StringBuilder("");  
        if (status == null) {  
        	uHandler.sendEmptyMessage(1);
            sb2.append(getResources().getString(R.string.label_gps_satellitenumber) +0);  
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {  
            int maxSatellites = status.getMaxSatellites();  
            Iterator<GpsSatellite> it = status.getSatellites().iterator();  
            numSatelliteList.clear();  
            int count = 0;  
            while (it.hasNext() && count <= maxSatellites) {  
                GpsSatellite s = it.next();  
                numSatelliteList.add(s);  
                count++;  
            }  
            sb2.append(getResources().getString(R.string.label_gps_satellitenumber) + numSatelliteList.size());  
            uHandler.sendEmptyMessage(1);
        }  
          
        return sb2.toString();  
    }  
    private Boolean toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try 
          {
            PendingIntent.getBroadcast(GPSActivity.this, 0, gpsIntent, 0).send();
            return true;
          } catch (CanceledException e) 
          {
            e.printStackTrace();
            return false;
          }
}
    private void openGPS() {       
        //获取GPS现在的状态（打开或是关闭状态）
      boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
   
      if(gpsEnabled)
      {

      //关闭GPS
       Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, false );
      }
      else
      {
       //打开GPS  www.2cto.com
       Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, true);

      }
      
    }
    public boolean hasGPSDevice(Context context)
    {
    	try{
     final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
     if ( mgr == null ) return false;
     final List<String> providers = mgr.getAllProviders();
     if ( providers == null ) return false;
     return providers.contains(LocationManager.GPS_PROVIDER);
    	}
    	catch(Exception e)
    	{
    		return false;
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
			this.setResult(R.id.button_gps,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gps), GPSActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_gps,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gps), GPSActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
			
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timer = new Timer();
		 task = new TimerTask() {
				@Override
				public void run() {
					uHandler.sendEmptyMessage(0);
				}
			};
		timer.schedule(task, 10000);
		if(locationManager!=null)
		{
		locationManager.requestLocationUpdates(provider, 1000, 1, ll); 
		locationManager.addGpsStatusListener(statusListener);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uHandler.sendEmptyMessage(1);
		if(locationManager!=null)
		{
		locationManager.removeUpdates(ll);
		}
	}
	private Handler uHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
				case 0: 
					tv_gps.setText(R.string.label_gps_none);
					tv_satNUM.setVisibility(View.GONE);
				break;

				case 1:
					if (GPSActivity.this.timer != null)
			        {
						GPSActivity.this.timer.cancel();
						timer = null;
						task = null;
			        }
				break;	
			}
		}
	};
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(GPSActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    GPSActivity.this.setResult(R.id.button_gps,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_gps),GPSActivity.this,XmlUtils.activityClasses.get(0), false);
	    				GPSActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
