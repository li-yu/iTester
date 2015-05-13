package com.liyu.itester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.liyu.itester.utils.CameraUtils;
import com.liyu.itester.utils.GetPlatform;
import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HwActivity extends Activity implements OnClickListener{
	TextView tv_hw;
	final String TAG = "HwActivity";
	private String GLRENDERER,GLVENDOR,GLVERSION;
	private String piexls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hw);
		
		initUI();
		
		if(FunctionApplication.isAutoMode)
			return;
		try
		{
			Timer timer=new Timer();
			timer.schedule(new TimerTask(){
			public void run(){
				uHandler.sendEmptyMessage(0);
			}},2000);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}

	private void initUI()
	{
		tv_hw = (TextView)findViewById(R.id.textView_hw);
		findViewById(R.id.button_pass).setOnClickListener(this);
		findViewById(R.id.button_fail).setOnClickListener(this);
		if(FunctionApplication.isAutoMode)
		{
			tv_hw.setText("Model: "+Build.MODEL+"\n"+"CPU: "+GetPlatform.getCPUname()+"\n"+"RAM: "+getRamSize()+"\n"+"Flash: "+getSDCardMemory());
			String [] ramflash= XmlUtils.getEntity(this.getClass().getSimpleName()).getParameter().split("x");
			if(getRamSize().equals(ramflash[0])&&getSDCardMemory().equals(ramflash[1]))
			{
				tv_hw.setTextColor(Color.GREEN);
				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_hw), HwActivity.this, XmlUtils.activityClasses.get(0), true);
				HwActivity.this.finish();
			}
			else {
				tv_hw.setTextColor(Color.RED);
				findViewById(R.id.button_pass).setEnabled(false);
				}
			return;
		}
		getOpenGL();
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
			this.setResult(R.id.button_hw,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_hw), HwActivity.this, XmlUtils.activityClasses.get(0), true);
			this.finish();
			break;
		case R.id.button_fail:
			intent.putExtra("pass", false);
			this.setResult(R.id.button_hw,intent);
			if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_hw), HwActivity.this, XmlUtils.activityClasses.get(0), false);
			this.finish();
			break;
		}
	}
	
	public Handler uHandler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch(msg.what)
			{
			case 0:
			tv_hw.setTextColor(Color.BLUE);
			tv_hw.setTextSize(30);
			tv_hw.setText(getResources().getString(R.string.label_hw_brand)+Build.BRAND+"\n"+getResources().getString(R.string.label_hw_model)+Build.MODEL+"\n"+"CPU: "+GetPlatform.getCPUname()+" * "+getNumCores()+" Cores "+getCPUVendor()+"\n"+getResources().getString(R.string.label_hw_cpufreq)+getMaxCpuFreq()+" GHz"+"\n"+"GPU: "+HwActivity.this.GLVENDOR+" "+HwActivity.this.GLRENDERER+"\n"+getResources().getString(R.string.label_hw_gpuversion)+HwActivity.this.GLVERSION+"\n"+
			"RAM: "+getRamSize()+"\n"+"Flash: "+getSDCardMemory()+"\n"+getResources().getString(R.string.label_hw_lcd_resolution)+getdpi()+"\n"+getResources().getString(R.string.label_hw_lcd_dpi)+getLCDdensity());	
			new Thread()
			{
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						piexls = getResources().getString(R.string.label_hw_frontcamera)+CameraUtils.getCameraPixels(CameraUtils.HasFrontCamera())+getResources().getString(R.string.label_hw_rearcamera)+CameraUtils.getCameraPixels(CameraUtils.HasBackCamera());
						uHandler.sendEmptyMessage(1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						piexls = getResources().getString(R.string.label_hw_camera_exception);
						uHandler.sendEmptyMessage(1); 
						e.printStackTrace();
					}
				}
				
			}.start();
			break;
			case 1:
				tv_hw.append(piexls);
				break;
		    }
		}
	};
	public void getOpenGL()
	{
		GLSurfaceView mGLSurfaceView = new GLSurfaceView( this);
		DemoRenderer mrenderer = new DemoRenderer();
		mGLSurfaceView.setRenderer(mrenderer);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1,1);  
		params.topMargin = 0;  
		params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;  
		HwActivity.this.addContentView(mGLSurfaceView, params);
	}
	
	public static String getRamSize()
	{
		String line = null;
       	long memSize = 0;
       	
        try {
        
        	File f = new File("/proc/meminfo");
        	BufferedReader r = new BufferedReader(new InputStreamReader( new FileInputStream( f )),32);
				line = r.readLine();
				 memSize =  Long.parseLong(line.substring(line.indexOf(":")+1,
									line.lastIndexOf("k")).trim())/1024;
				 r.close();
        }
		catch (IOException e) {
				return null;
		}
        
        if (memSize >256 && memSize <=512)
        {
        	return "512MB";
        } else if (memSize > 512 && memSize <= 1024){
        	return "1GB";
        }
        else if (memSize > 1024 && memSize <= 2048){
        	return "2GB";
        }
        
        return null;
	}
	
	public int getNumCores() {
	    //Private Class to display only CPU devices in the directory listing
	    class CpuFilter implements FileFilter {
	        @Override
	        public boolean accept(File pathname) {
	            //Check if filename is "cpu", followed by a single digit number
	            if(Pattern.matches("cpu[0-9]", pathname.getName())) {
	                return true;
	            }
	            return false;
	        }      
	    }
	    try {
	        //Get directory containing CPU info
	        File dir = new File("/sys/devices/system/cpu/");
	        //Filter to only list the devices we care about
	        File[] files = dir.listFiles(new CpuFilter());
	        //Return the number of cores (virtual CPU devices)
	        return files.length;
	    } catch(Exception e) {
	        //Default to return 1 core
	        return 1;
	    }
	}

	public static String getCPUVendor() {
    	String str = "", strCPU = "", cpuVendor = "null";
    	try {
    	//读取CPU信息
    	Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
    	InputStreamReader ir = new InputStreamReader(pp.getInputStream());
    	LineNumberReader input = new LineNumberReader(ir);
    	//查找CPU序列号
    	for (int i = 1; i < 100; i++) {
    	str = input.readLine();
    	if (str != null) {
    	//查找到序列号所在行  兼容intel chip
    	if (str.indexOf("Processor") > -1||str.indexOf("model name") > -1) {
    	//提取序列号
    	strCPU = str.substring(str.indexOf(":") + 1,str.length());
    	//去空格
    	cpuVendor = strCPU.trim();
    	if(cpuVendor!=null)
    	break;
    	}
    	}else{
    	//文件结尾
    	break;
    	}
    	}
    	} catch (IOException ex) {
    	//赋予默认值
    	ex.printStackTrace();
    	}
    	
    	return cpuVendor;
    	}
	
	public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
                String[] args = { "/system/bin/cat",
                                "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
                cmd = new ProcessBuilder(args);
                Process process = cmd.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[24];
                while (in.read(re) != -1) {
                        result = result + new String(re);
                }
                in.close();
        } catch (IOException ex) {
                ex.printStackTrace();
                result = "N/A";
        }
        String a= result.trim();
        int b=Integer.parseInt(a)/1000;
        double d = (double)b/1000;
        //return new DecimalFormat("#.#").format(d);
        return String.valueOf(d);
        
}
    //获取屏幕分辨率
    public String getdpi()
    {   String dpi=null;
    	Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics(); 
        @SuppressWarnings("rawtypes")
		Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics",DisplayMetrics.class);
            method.invoke(display, dm);
            dpi=dm.widthPixels+"*"+dm.heightPixels;
        }catch(Exception e){
            e.printStackTrace();
        }  
        return dpi;
    }
    
    public String getLCDdensity()
    {
    	DisplayMetrics dm = new DisplayMetrics();  
    	dm = getResources().getDisplayMetrics();
    	int densityDPI = dm.densityDpi;
    	return densityDPI+" dpi";
    }
    //获取内部SD卡存储容量
    public String getSDCardMemory() {  
    	try{
        long sdCardInfo=0;  
        String state = Environment.getExternalStorageState();  
        if (Environment.MEDIA_MOUNTED.equals(state)) {  
            File sdcardDir = Environment.getExternalStorageDirectory();  
            StatFs sf = new StatFs(sdcardDir.getPath());  
            long bSize = sf.getBlockSize();  
            long bCount = sf.getBlockCount();  
         
            sdCardInfo = bSize * bCount;//总大小  
            
            long sdSize = sdCardInfo/(1024*1024);
            if(sdSize < 4*1024)
            {
            	return "4GB";
            }
            else if(sdSize < 8*1024)
            {
            	return "8GB";
            }
            else if(sdSize < 16*1024)
            {
            	return "16GB";
            }
            else if(sdSize < 32*1024)
            {
            	return "32GB";
            }
            else if(sdSize < 64*1024)
            {
            	return "64GB";
            }
        }  
    	}
    	catch(Exception e)
    	{
    		return "0";
    	}
    	return "0";
    } 
    
   private class DemoRenderer  implements GLSurfaceView.Renderer {  
  	  	
    	    public void onDrawFrame(GL10 gl) {  
    	        // TODO Auto-generated method stub  
    	  
    	    }  
    	  
    	  
    	    public void onSurfaceChanged(GL10 gl, int arg1, int arg2) {  
    	        // TODO Auto-generated method stub  
    	  
    	    }

    		@Override
    		public void onSurfaceCreated(GL10 gl,
    				EGLConfig config) {
    			// TODO Auto-generated method stub
    			HwActivity.this.GLRENDERER = gl.glGetString(GL10.GL_RENDERER);
    			HwActivity.this.GLVENDOR = gl.glGetString(GL10.GL_VENDOR);
    			HwActivity.this.GLVERSION = gl.glGetString(GL10.GL_VERSION);
    		}  
    		
    	  
    	}  
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) { 
   	if (keyCode == KeyEvent.KEYCODE_BACK)
   	{
   		new AlertDialog.Builder(HwActivity.this)
   		.setTitle(R.string.title_tips).setMessage(R.string.message_resultNG)
   		.setPositiveButton("NG", new DialogInterface.OnClickListener(){
   			public void onClick(DialogInterface dialog, int whichButton)
   			{
   				Intent intent = new Intent();
   				intent.putExtra("pass", false);
   			    HwActivity.this.setResult(R.id.button_hw,intent);
   				if(FunctionApplication.isAutoMode)jumpUtils.jumpTo(getResources().getString(R.string.title_activity_hw),HwActivity.this,XmlUtils.activityClasses.get(0), false);
   				HwActivity.this.finish();
   			}
   		})
   		.show();
   		return true;
   	}
		return false;
   }
}
