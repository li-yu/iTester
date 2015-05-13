package com.liyu.itester;

import com.liyu.itester.touch.BorderTouchView;
import com.liyu.itester.touch.CenterTouchView;
import com.liyu.itester.touch.OnTouchChangedListener;
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

public class TouchActivity extends Activity implements OnTouchChangedListener{
    /** Called when the activity is first created. */
	private BorderTouchView mBorderView;
	private CenterTouchView mCenterView;
	private BroadcastReceiver mReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //getWindow().getDecorView().setSystemUiVisibility(2); //other standard platform is 2
       
        setContentView(R.layout.activity_touch);
        getWindow().getDecorView().setSystemUiVisibility(8);//rk platform solution is 8
        mBorderView = (BorderTouchView)findViewById(R.id.touch_border);
        mCenterView = (CenterTouchView)findViewById(R.id.touch_center);
        mBorderView.setOnTouchChangedListener(this);
    }
    
    private void regBroadReceiver()
    {
     mReceiver = new BroadcastReceiver()
      {
        public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
        {
          String str = paramAnonymousIntent.getAction();
          if (str.equals("com.liyu.itester.show"))
          {  
             getWindow().getDecorView().setSystemUiVisibility(0);
          }
          else{
        	  getWindow().getDecorView().setSystemUiVisibility(2);
          }
        }
      };
      IntentFilter localIntentFilter = new IntentFilter("com.liyu.itester.show");
      localIntentFilter.addAction("com.liyu.itester.hide");
      registerReceiver(mReceiver, localIntentFilter);
    }
	@Override
	public void onTouchFinish(View v) {
		// TODO Auto-generated method stub
		if(v==mBorderView)
		{
			mBorderView.setVisibility(View.GONE);
            mCenterView.setOnTouchChangedListener(this);
            mCenterView.setVisibility(View.VISIBLE);
		}
		else if(v==mCenterView)
		{
			Intent intent = new Intent();
			intent.putExtra("pass", true);
			this.setResult(R.id.button_drawing,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_drawing),TouchActivity.this,XmlUtils.activityClasses.get(0), true);
			TouchActivity.this.finish();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getWindow().getDecorView().setSystemUiVisibility(0);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//regBroadReceiver();
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
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(TouchActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    TouchActivity.this.setResult(R.id.button_drawing,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_drawing),TouchActivity.this,XmlUtils.activityClasses.get(0), false);
	    				TouchActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
}