package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class FMActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fm);
		
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_openFM).setOnClickListener(this);
		
	    openFM();
	}

	public void openFM()
	{
		try {
			Intent fmIntent = new Intent();
			ComponentName componetName = new ComponentName("com.mediatek.FMRadio","com.mediatek.FMRadio.FMRadioActivity");
			fmIntent.setComponent(componetName);
			startActivity(fmIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			findViewById(R.id.button_pass).setEnabled(false);
			findViewById(R.id.button_openFM).setEnabled(false);
			Toast.makeText(FMActivity.this, "No FM Radio!", Toast.LENGTH_SHORT).show();
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
			this.setResult(R.id.button_fm,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_fm), FMActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_fm,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_fm), FMActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		case R.id.button_openFM:
			openFM();
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(FMActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    FMActivity.this.setResult(R.id.button_fm,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_fm),FMActivity.this,XmlUtils.activityClasses.get(0), false);
	    				FMActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
