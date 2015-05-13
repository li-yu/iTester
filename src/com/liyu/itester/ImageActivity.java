package com.liyu.itester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ImageActivity extends Activity implements OnClickListener{
	TextView tv_image;
	final String TAG = "ImageActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		
		initUI();
		tv_image.setTextColor(Color.BLUE);
		tv_image.setTextSize(30);
		tv_image.setText("Model: " + Build.MODEL
    			+ "\nAndroid: " + Build.VERSION.RELEASE
    			+ "\nKernel: " + getFormattedKernelVersion()
    			+ "\nImage: " + android.os.Build.DISPLAY);
	}
	
	private void initUI()
	{
		tv_image = (TextView)findViewById(R.id.textView_image);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
	}

	public static String formatKernelVersion(String paramString)
	  {
	    Matcher localMatcher = Pattern.compile("Linux version (\\S+) \\((\\S+?)\\) (?:\\(gcc.+? \\)) (#\\d+) (?:.*?)?((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)").matcher(paramString);
	    if (!localMatcher.matches())
	    {
	      Log.e("DeviceInfoSettings", "Regex did not match on /proc/version: " + paramString);
	      return "Unavailable";
	    }
	    if (localMatcher.groupCount() < 4)
	    {
	      Log.e("DeviceInfoSettings", "Regex match on /proc/version only returned " + localMatcher.groupCount() + " groups");
	      return "Unavailable";
	    }
	    return localMatcher.group(1) + " "  + localMatcher.group(4);
	  }

	  public static String getFormattedKernelVersion()
	  {
	    try
	    {
	      String str = formatKernelVersion(readLine("/proc/version"));
	      return str;
	    }
	    catch (IOException localIOException)
	    {
	      Log.e("DeviceInfoSettings", "IO Exception when getting kernel version for Device Info screen", localIOException);
	    }
	    return "Unavailable";
	  }

	  private static String readLine(String paramString)
			    throws IOException
			  {
			    BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramString), 256);
			    try
			    {
			      String str = localBufferedReader.readLine();
			      return str;
			    }
			    finally
			    {
			      localBufferedReader.close();
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
			this.setResult(R.id.button_image,intent);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_image,intent);
			this.finish();
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(ImageActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    ImageActivity.this.setResult(R.id.button_image,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_image),ImageActivity.this,XmlUtils.activityClasses.get(0), false);
	    				ImageActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
