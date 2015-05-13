package com.liyu.itester.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class GetPlatform {
	public static String getCPUname() {
    	String str = "", strCPU = "", cpuVendor = "";
    	try {
    	//读取CPU信息
    	Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
    	InputStreamReader ir = new InputStreamReader(pp.getInputStream());
    	LineNumberReader input = new LineNumberReader(ir);
    	//查找CPU序列号
    	for (int i = 1; i < 100; i++) {
    	str = input.readLine();
    	if (str != null) {
    	//查找到序列号所在行
    	if (str.indexOf("Hardware") > -1) {
    	//提取序列号
    	strCPU = str.substring(str.indexOf(":") + 1,
    	str.length());
    	//去空格
    	cpuVendor = strCPU.trim();
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
}
