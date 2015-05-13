package com.liyu.itester;

import android.view.View;

public class Utils {

	public static void setResult(View v,boolean pass)
	{
		if(pass)
		{
		    v.setBackgroundResource(R.drawable.btn_style_3);
		}
		else
		{
			v.setBackgroundResource(R.drawable.btn_style_2);
		}
	}
	
}
