package com.liyu.itester;


import java.util.Locale;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.liyu.itester.MainActivityWiperReceiver.MainActivity;
import com.liyu.itester.entity.test_config_entity;
import com.liyu.itester.utils.CheckAuth;
import com.liyu.itester.utils.SdPath;
import com.liyu.itester.utils.XmlUtils;
import com.liyu.itester.utils.jumpUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;


	public  class StartActivity extends Activity implements OnClickListener{
		int mode = 1;
		String configPath = null;
		test_config_entity entity_config;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		configPath=SdPath.getConfigPath();
		if(configPath==null)
		{
			startActivity(new Intent(StartActivity.this,MainActivity.class));
			StartActivity.this.finish();
			return;
		}
		else if(!CheckAuth.isAuth())
		{
			findViewById(R.id.button_auto).setEnabled(false);
			//findViewById(R.id.button_manual).setEnabled(false);
		}
		init();
	}

	public void init()
	{
		findViewById(R.id.button_manual).setOnClickListener(this);
		findViewById(R.id.button_auto).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(jumpUtils.isFastDoubleClick())
			return;
		switch(v.getId())
		{
		case R.id.button_manual:
			FunctionApplication.isAutoMode = false;
			startActivity(new Intent(StartActivity.this,MainActivity.class));
			StartActivity.this.finish();
			break;
		case R.id.button_auto:
			
				if(FunctionApplication.isAutoMode)
				{
					XmlUtils.configPath = configPath;
					entity_config = XmlUtils.getConfigEntityInstance();
					XmlUtils.activityClasses.add(ResultsActivity.class);
		     		startActivity(new Intent(StartActivity.this, XmlUtils.activityClasses.get(0)));
		 			XmlUtils.activityClasses.remove(0);
		 			StartActivity.this.finish();
				}
			
			break;
		
	}
		}
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    	if (keyCode == KeyEvent.KEYCODE_BACK)
	    	{
	    		new AlertDialog.Builder(StartActivity.this)
	    		.setTitle(R.string.title_tips).setMessage(R.string.label_confirmexit)
	    		.setPositiveButton(R.string.label_exit, new DialogInterface.OnClickListener(){
	    			public void onClick(DialogInterface dialog, int whichButton)
	    			{
	    				StartActivity.this.finish();
	    				System.exit(0);
	    			}
	    		})
	    		.show();
	    		return true;
	    	}
			return false;
	    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		YoYo.with(Techniques.RollIn)
        .duration(1000)
        .playOn(findViewById(R.id.textView_start));
		
		YoYo.with(Techniques.SlideInLeft)
        .duration(1000)
        .playOn(findViewById(R.id.button_manual));
		
		YoYo.with(Techniques.SlideInRight)
        .duration(1000)
        .playOn(findViewById(R.id.button_auto));
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		loadMyConfig();
	}
	public void loadMyConfig()
	{
		if(FunctionApplication.isAutoMode)
		{
			if(FunctionApplication.tce.getLanguage().toString().equalsIgnoreCase("chinese"))
			{
				toChinese();
			}
			else
			{
				toEnglish();
			}
		}
		else
		{
		       toChinese();
			   //toEnglish();
		}
	}

	public void toEnglish()
	{
		String languageToLoad  = "en_US"; 
        Locale locale = new Locale(languageToLoad);  
        Locale.setDefault(locale);  
        Configuration config = getResources().getConfiguration();  
        DisplayMetrics metrics = getResources().getDisplayMetrics();  
        config.locale = Locale.ENGLISH;  
        getResources().updateConfiguration(config, metrics); 
	}
	
	public void toChinese()
	{
		String languageToLoad  = "zh"; 
        Locale locale = new Locale(languageToLoad);  
        Locale.setDefault(locale);  
        Configuration config = getResources().getConfiguration();  
        DisplayMetrics metrics = getResources().getDisplayMetrics();  
        config.locale = Locale.SIMPLIFIED_CHINESE;  
        getResources().updateConfiguration(config, metrics); 
	}
	 
}

