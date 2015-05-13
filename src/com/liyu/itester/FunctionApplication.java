package com.liyu.itester;

import java.util.Locale;

import com.liyu.itester.entity.test_config_entity;
import com.liyu.itester.utils.SdPath;
import com.liyu.itester.utils.XmlUtils;
import android.app.Application;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

public class FunctionApplication extends Application{
public static boolean isAutoMode = false;
public static test_config_entity tce;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		 String configPath = SdPath.getConfigPath();
		 if(configPath!=null)
		 {
			 isAutoMode = true;
			 XmlUtils.configPath = configPath;
			 tce = XmlUtils.getConfigEntityInstance();
			 if(tce.getLanguage().toString().equalsIgnoreCase("chinese"))
			 {
				 toChinese();
			 }
			 else
				 toEnglish();
			 
			
		 }
		 else
		 {
			toChinese();
			// toEnglish();
		 }
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		loadMyConfig();
	}

	public void loadMyConfig()
	{
		if(FunctionApplication.tce!=null)
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
