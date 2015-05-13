package com.liyu.itester;

import java.io.IOException;

import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FlashlightActivity extends Activity implements OnClickListener,Callback{
	TextView tv_flashlight;
	final String TAG = "FlashlightActivity";
	ToggleButton tb;
	Camera camera;
	SurfaceHolder mHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flashlight);
		initUI();
		}
	
	
		@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		SurfaceView preview = (SurfaceView)findViewById(R.id.PREVIEW);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);
	}


		private void initUI()
		{
			tv_flashlight = (TextView)findViewById(R.id.textView_flashlight);
			tv_flashlight.setTextColor(Color.BLUE);
			tv_flashlight.setTextSize(30);
			tv_flashlight.setText(R.string.label_flashlight_off);
			findViewById(R.id.button_pass).setOnClickListener(this);
			findViewById(R.id.button_fail).setOnClickListener(this);
			tb = (ToggleButton)findViewById(R.id.toggleButton_flashlight);
			try {
				camera = Camera.open();
				camera.startPreview();
				if(!FlashlightManager.hasFlashlight(camera))
				{
					tv_flashlight.setText(R.string.label_flashlight_none);
					tb.setEnabled(false);
					findViewById(R.id.button_pass).setEnabled(false);
					return;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tb.setEnabled(false);
				findViewById(R.id.button_pass).setEnabled(false);
				tv_flashlight.setText(R.string.label_flashlight_exception);
				return;
			}
			
			tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked)
					{
						FlashlightManager.turnLightOn(camera);
						tv_flashlight.setText(R.string.label_flashlight_on);
					}
					else
					{
						FlashlightManager.turnLightOff(camera);
						tv_flashlight.setText(R.string.label_flashlight_off);
					}
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
				this.setResult(R.id.button_flashlight,intent);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_flashlight), FlashlightActivity.this, XmlUtils.activityClasses.get(0), true);
				this.finish();
				break;
			case R.id.button_fail:
				intent.putExtra("pass", false);
				this.setResult(R.id.button_flashlight,intent);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_flashlight), FlashlightActivity.this, XmlUtils.activityClasses.get(0), false);
				this.finish();
				break;
			}
		}
		
		 @Override
		protected void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			if(camera!=null)
				camera.release();
		}


		@Override
		    public boolean onKeyDown(int keyCode, KeyEvent event) { 
		    	if (keyCode == KeyEvent.KEYCODE_BACK)
		    	{
		    		new AlertDialog.Builder(FlashlightActivity.this)
		    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
		    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
		    			public void onClick(DialogInterface dialog, int whichButton)
		    			{
		    				Intent intent = new Intent();
		    				intent.putExtra("pass", false);
		    			    FlashlightActivity.this.setResult(R.id.button_flashlight,intent);
		    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_flashlight),FlashlightActivity.this,XmlUtils.activityClasses.get(0), false);
		    				FlashlightActivity.this.finish();
		    			}
		    		})
		    		.show();
		    		return true;
		    	}
				return false;
		    }
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mHolder = holder;
	        try {
	            camera.setPreviewDisplay(mHolder);  
	        } catch (IOException e){
	            e.printStackTrace();
	        }
		}
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			camera.stopPreview();
	        mHolder = null;
		}
}
