package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Demonstrates wrapping a layout in a ScrollView.
 *
 */
public class PointerLocation extends Activity implements OnClickListener{
	  private BroadcastReceiver mReceiver;
	  final String TAG = "MTActivity";
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
       // getWindow().getDecorView().setSystemUiVisibility(8);//rk platform solution is 8
       // getWindow().getDecorView().setSystemUiVisibility(2); //other standard platform is 2
        setContentView(R.layout.activity_pointloaction);
        initUI();
    }
    
	private void initUI()
	{
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		findViewById(R.id.button_pass).setVisibility(View.GONE);
		findViewById(R.id.button_fail).setVisibility(View.GONE);
	}

    private void regBroadReceiver()
    {
     mReceiver = new BroadcastReceiver()
      {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
          String str = paramAnonymousIntent.getAction();
          if (str.equals("com.liyu.itester.show"))
          {  PointerLocation.this.getWindow().getDecorView().setSystemUiVisibility(0);
             findViewById(R.id.button_pass).setVisibility(View.VISIBLE);
  		     findViewById(R.id.button_fail).setVisibility(View.VISIBLE);
          }
          else{
        	  getWindow().getDecorView().setSystemUiVisibility(2);
        	  findViewById(R.id.button_pass).setVisibility(View.GONE);
      		  findViewById(R.id.button_fail).setVisibility(View.GONE);
          }
        }
      };
      IntentFilter localIntentFilter = new IntentFilter("com.liyu.itester.show");
      localIntentFilter.addAction("com.liyu.itester.hide");
      registerReceiver(mReceiver, localIntentFilter);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		PointerLocation.this.getWindow().getDecorView().setSystemUiVisibility(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		regBroadReceiver();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mReceiver!=null)
		{
			unregisterReceiver(mReceiver);
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
			this.setResult(R.id.button_drawing,intent);
			if(FunctionApplication.isAutoMode)
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_drawing),PointerLocation.this,XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_drawing,intent);
			if(FunctionApplication.isAutoMode)
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_drawing),PointerLocation.this,XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(PointerLocation.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    PointerLocation.this.setResult(R.id.button_drawing,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_drawing),PointerLocation.this,XmlUtils.activityClasses.get(0), false);
	    				PointerLocation.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}
