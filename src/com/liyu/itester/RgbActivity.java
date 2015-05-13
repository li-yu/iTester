package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

public class RgbActivity extends Activity implements OnClickListener,OnLongClickListener{
	final String TAG = "RgbActivity";
	View rgb;
	int id = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rgb);
		
		initUI();
		
	}
	
	public void initUI()
	{
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		getWindow().getDecorView().setSystemUiVisibility(8);
		findViewById(R.id.button_pass).setVisibility(View.INVISIBLE);
		findViewById(R.id.button_fail).setVisibility(View.INVISIBLE);
		rgb =this.findViewById(R.id.textView_rgb);
		rgb.setOnClickListener(this);
		rgb.setOnLongClickListener(this);
		rgb.setBackgroundColor(Color.MAGENTA);
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
			this.setResult(R.id.button_rgb,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_rgb), RgbActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_rgb,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_rgb), RgbActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		case R.id.textView_rgb:
			changeRGB(id);
			id++;
			break;
		}
	}
	

	public void changeRGB(int id)
	{
		switch(id)
		{
			case 0:
				rgb.setBackgroundColor(Color.RED);
				break;
			case 1:
				rgb.setBackgroundColor(Color.GREEN);
				break;
			case 2:
				rgb.setBackgroundColor(Color.BLUE);
				break;
			case 3:
				rgb.setBackgroundColor(Color.DKGRAY);
				break;
			case 4:
				rgb.setBackgroundColor(Color.GRAY);
				break;
			case 5:
				rgb.setBackgroundColor(Color.LTGRAY);
				break;
			case 6:
				rgb.setBackgroundColor(Color.WHITE);
				break;
			case 7:
				rgb.setBackgroundResource(R.drawable.rgb01);
				break;
			case 8:
				rgb.setBackgroundResource(R.drawable.rgb02);
				break;
			case 9:
				rgb.setBackgroundResource(R.drawable.rgb03);
				break;
			case 10:
				rgb.setBackgroundResource(R.drawable.rgb04);
				break;
			case 11:
				rgb.setVisibility(View.GONE);
				getWindow().getDecorView().setSystemUiVisibility(0);
				findViewById(R.id.button_pass).setVisibility(View.VISIBLE);
				findViewById(R.id.button_fail).setVisibility(View.VISIBLE);
				break;
		}
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		getWindow().getDecorView().setSystemUiVisibility(2);
		return true;
	}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(RgbActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    RgbActivity.this.setResult(R.id.button_rgb,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_rgb),RgbActivity.this,XmlUtils.activityClasses.get(0), false);
	    				RgbActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
