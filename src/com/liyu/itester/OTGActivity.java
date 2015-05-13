package com.liyu.itester;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;
import com.liyu.itester.utils.MountListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.widget.TextView;

public class OTGActivity extends Activity implements OnClickListener,MountListener.OnStateChangedListener{
	private TextView tv_otg;
	private boolean isPassed = false;
	private MountListener ml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otg);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		tv_otg = (TextView)findViewById(R.id.textView_otg);
		tv_otg.setOnHoverListener(new OnHoverListener() {
			
			@Override
			public boolean onHover(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int what = event.getAction();
				switch(what){
				 case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
					 
                     break;
                 case MotionEvent.ACTION_HOVER_MOVE:  //鼠标在view上
                	 if(!isPassed)
                	 {
                	 isPassed =true;
                	 tv_otg.setText("Pass");
                	 tv_otg.setTextColor(Color.GREEN);
                	 findViewById(R.id.button_pass).setEnabled(true);
                	 if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_otg), OTGActivity.this, XmlUtils.activityClasses.get(0), true);
                	 if(FunctionApplication.isAutoMode)
                	 OTGActivity.this.finish();
                	 }
                	 
                     break;
                 case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
                	 
                     break;
				}
				return false;
			}
		});
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
			this.setResult(R.id.button_otg,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_otg), OTGActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_otg,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_otg), OTGActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(OTGActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
	    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				Intent intent = new Intent();
	    				intent.putExtra("pass", false);
	    			    OTGActivity.this.setResult(R.id.button_otg,intent);
	    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_otg),OTGActivity.this,XmlUtils.activityClasses.get(0), false);
	    				OTGActivity.this.finish();
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }
	@Override
	public void onMounted() {
		// TODO Auto-generated method stub
		 if(!isPassed)
    	 {
    	 isPassed =true;
    	 tv_otg.setText("Pass");
    	 tv_otg.setTextColor(Color.GREEN);
    	 findViewById(R.id.button_pass).setEnabled(true);
    	 if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_otg), OTGActivity.this, XmlUtils.activityClasses.get(0), true);
    	 if(FunctionApplication.isAutoMode)
    	 OTGActivity.this.finish();
    	 }
	}
	@Override
	public void onUnMounted() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ml = new MountListener(OTGActivity.this);
		ml.setOnStateChangedListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(ml!=null)
		{
			ml.removeListener();
		}
	}
}
