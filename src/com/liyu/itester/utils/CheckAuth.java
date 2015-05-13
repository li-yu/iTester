package com.liyu.itester.utils;

public class CheckAuth {
	
	public static String AllModels = "aa,bb,cc,dd";
	public static boolean isAuth()
	{
		 return AllModels.contains(android.os.Build.MODEL);
	}

}
