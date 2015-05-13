package com.liyu.itester.utils;


import com.liyu.itester.FunctionApplication;

import android.content.Context;
import android.content.Intent;

public class jumpUtils {
	
private static long lastClickTime;
public static boolean isFastDoubleClick() {
	        long time = System.currentTimeMillis();
	        long timeD = time - lastClickTime;
	        if ( 0 < timeD && timeD < 500) {   
	            return true;   
	        }   
	        lastClickTime = time;   
	        return false;   
	    }
public static void jumpTo(String item,Context context,Class cls,boolean isPass)
   {
	if(FunctionApplication.isAutoMode)
	{
		Intent i = new Intent();
		i.setClass(context, cls);
		XmlUtils.activityClasses.remove(0);
		if(isPass)
		{
			XmlUtils.passList.add(item);
		}
		else
		{
			XmlUtils.failList.add(item);
		}
		context.startActivity(i);
	}
	
	}
}
