package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class KeyActivity extends Activity implements OnClickListener{
	final String TAG = "KeyActivity";
	CheckBox POWER,VUP,VDOWN;
	boolean isSuspend = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key);
		
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		POWER = (CheckBox)findViewById(R.id.checkBox_power);
		VUP = (CheckBox)findViewById(R.id.checkBox_v1);
		VDOWN = (CheckBox)findViewById(R.id.checkBox_v2);
		findViewById(R.id.button_pass).setEnabled(false);
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
			this.setResult(R.id.button_keyboard,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_key), KeyActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_keyboard,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_key), KeyActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
	      return true;
		}
		if(keyCode == KeyEvent.KEYCODE_VOLUME_UP)
		{
			 VUP.setChecked(true);
	      if(isAllChecked())
	      {
	    	  findViewById(R.id.button_pass).setEnabled(true);
	    	  findViewById(R.id.button_fail).setEnabled(false);
	    	  if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_key), KeyActivity.this, XmlUtils.activityClasses.get(0), true);
	    	  if(FunctionApplication.isAutoMode)
	    		  KeyActivity.this.finish();
	      }
	      
	      return true;
		}
		if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
		{
			 VDOWN.setChecked(true);
	      if(isAllChecked())
	      {
	    	  findViewById(R.id.button_pass).setEnabled(true);
	    	  findViewById(R.id.button_fail).setEnabled(false);
	    	  if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_key), KeyActivity.this, XmlUtils.activityClasses.get(0), true);
	    	  if(FunctionApplication.isAutoMode)
	    		  KeyActivity.this.finish();
	      }
	      
	      return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isSuspend)
		{
			POWER.setChecked(true);
			 if(isAllChecked())
		      {
		    	  findViewById(R.id.button_pass).setEnabled(true);
		    	  findViewById(R.id.button_fail).setEnabled(false);
		    	  if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_key), KeyActivity.this, XmlUtils.activityClasses.get(0), true);
		    	  if(FunctionApplication.isAutoMode)
		    		  KeyActivity.this.finish();
		      }
			 isSuspend = false;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isSuspend = true;
	}

	private boolean isAllChecked()
	{
		return POWER.isChecked()&VUP.isChecked()&VDOWN.isChecked();
	}
}
