package com.liyu.itester;

import java.io.File;
import java.text.DecimalFormat;
import com.liyu.itester.utils.SdPath;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SDActivity extends Activity implements OnClickListener,MountListener.OnStateChangedListener{
	private final String TAG = "SDActivity";
	private TextView tv_sd;
	private MountListener ml;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sd);
	     initUI();
	    }
	    
		private void initUI()
		{
			tv_sd = (TextView)findViewById(R.id.textView_sd);
			tv_sd.setTextColor(Color.BLUE);
			tv_sd.setTextSize(30);
			findViewById(R.id.button_pass).setOnClickListener(this);
			findViewById(R.id.button_fail).setOnClickListener(this);
			if(!FunctionApplication.isAutoMode)
			tv_sd.setText(getStorageInfo());
			else
			{
				if(isExternalSDexit())
				{
					if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_sd), SDActivity.this,XmlUtils.activityClasses.get(0), true);
					SDActivity.this.finish();
				}
				else
				{
					tv_sd.setTextColor(Color.RED);
					tv_sd.setText("NO SD Card!");
					findViewById(R.id.button_pass).setEnabled(false);
				}
			}
			
		}

		private String getStorageInfo()
		{
			
				return getResources().getString(R.string.label_sd_internalsd)+"\nPath: "+SdPath.getRealInternalSDpath()+"\n"+getSDSize(SdPath.getRealInternalSDpath())+"\n\n"+
						getResources().getString(R.string.label_sd_externalsd)+"\nPath: "+SdPath.getRealExternalSDpath()+"\n"+getSDSize(SdPath.getRealExternalSDpath())+"\n\n"+
						getResources().getString(R.string.label_sd_usb)+"\nPath: "+SdPath.getRealUSBpath()+"\n"+getSDSize(SdPath.getRealUSBpath());
			
		}
		
		private String getSDSize(String path)
		{
			 // 取得sdcard文件路径   
		    File pathFile = new File(path);   
		     
		    android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());   
		     
		    // 获取SDCard上BLOCK总数   
		    long nTotalBlocks = statfs.getBlockCount();   
		     
		    // 获取SDCard上每个block的SIZE   
		    long nBlocSize = statfs.getBlockSize();   
		     
		    // 获取可供程序使用的Block的数量   
		    long nAvailaBlock = statfs.getAvailableBlocks();   
		     
		    // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)   
		   // long nFreeBlock = statfs.getFreeBlocks();   
		     
		    // 计算SDCard 总容量大小MB   
		    long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;   
		    double a = (double)nSDTotalSize/1024;
		    String b = new DecimalFormat("#.##").format(a);
		    // 计算 SDCard 剩余大小MB   
		    long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;  
		    double c = (double)nSDFreeSize/1024;
		    String d = new DecimalFormat("#.##").format(c);
		    return "Total: "+b+" GB"+" Available: "+d+" GB";
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
				this.setResult(R.id.button_sd,intent);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_sd), SDActivity.this,XmlUtils.activityClasses.get(0), true);
				this.finish();
				break;
			case R.id.button_fail:
				intent.putExtra("pass", false);
				this.setResult(R.id.button_sd,intent);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_sd), SDActivity.this,XmlUtils.activityClasses.get(0), false);
				this.finish();
				break;
			}
		}
		
		public boolean isExternalSDexit()
		{
			try {
				File pathFile = new File(SdPath.getRealExternalSDpath());   
				 
				android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());   
				 
				// 获取SDCard上BLOCK总数   
				long nTotalBlocks = statfs.getBlockCount();   
				 
				// 获取SDCard上每个block的SIZE   
				long nBlocSize = statfs.getBlockSize();   
				 
				// 获取可供程序使用的Block的数量   
				long nAvailaBlock = statfs.getAvailableBlocks();   
				 
				// 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)   
   // long nFreeBlock = statfs.getFreeBlocks();   
				 
				// 计算SDCard 总容量大小MB   
				long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;   
				double a = (double)nSDTotalSize/1024;
				String b = new DecimalFormat("#.##").format(a);
				if(b.equals("0"))
					return false;
				else 
					return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		
		 @Override
		    public boolean onKeyDown(int keyCode, KeyEvent event) { 
		    	if (keyCode == KeyEvent.KEYCODE_BACK)
		    	{
		    		new AlertDialog.Builder(SDActivity.this)
		    		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
		    		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
		    			public void onClick(DialogInterface dialog, int whichButton)
		    			{
		    				Intent intent = new Intent();
		    				intent.putExtra("pass", false);
		    			    SDActivity.this.setResult(R.id.button_sd,intent);
		    				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_sd),SDActivity.this,XmlUtils.activityClasses.get(0), false);
		    				SDActivity.this.finish();
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
			initUI();
			Toast.makeText(getApplicationContext(), "mount", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onUnMounted() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			ml = new MountListener(SDActivity.this);
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
