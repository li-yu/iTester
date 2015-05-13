package com.liyu.itester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.liyu.itester.FunctionApplication;

import android.os.Environment;

public class SdPath {
	
	public static String getConfigPath()
	{
		String internalConfig = getRealInternalSDpath()+"/iTester.xml";
		String externalConfig = getRealExternalSDpath()+"iTester.xml";
		String USBConfig = getRealUSBpath()+"iTester.xml";
		if(new File(internalConfig).exists())
		{
			return internalConfig;
		}
		else if(new File(externalConfig).exists())
		{
			try {
				copySDFileTo(externalConfig, internalConfig);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return externalConfig;
		}
		else if(new File(USBConfig).exists())
		{
			try {
				copySDFileTo(USBConfig, internalConfig);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return USBConfig;
		}
		else 
			return null;
	}
	
	//拷贝SD 卡上的单个文件
	public static boolean copySDFileTo(String srcFileName, String destFileName)throws IOException 
	{
	    File srcFile = new File(srcFileName);
	    File destFile = new File(destFileName);
	    return copyFileTo(srcFile, destFile);
    }
	
	//拷贝一个文件,srcFile 源文件，destFile 目标文件
	public static boolean copyFileTo(File srcFile, File destFile) throws IOException 
	{
		if (srcFile.isDirectory() || destFile.isDirectory())
		   return false;// 判断是否是文件
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		int readLen = 0;
		byte[] buf = new byte[1024];
		while ((readLen = fis.read(buf)) != -1) 
		{
		  fos.write(buf, 0, readLen);
		}
		fos.flush();
		fos.close();
		fis.close();
		return true;
	}
	
	public static String getRealInternalSDpath()
	{
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/**
	 * RK : /mnt/external_sd/
	 * Allwinner : /storage/extsd/
	 * MTK : /mnt/sdcard2/
	 * Intel : /mnt/sdcard_ext/;/storage/sdcard1/
	 * Amlogic :/storage/external_storage/sdcard1/
	 * @return
	 */
	public static String getRealExternalSDpath()
	{
		if(FunctionApplication.isAutoMode)
		{
			return FunctionApplication.tce.getExternalSDPath();
		}
		String[] Paths = {"/mnt/external_sd/","/storage/extsd/","/mnt/sdcard2/","/mnt/sdcard_ext/","/storage/sdcard1/","/storage/external_storage/sdcard1/"};
		String path = "/mnt/";
		try {
			for(int i = 0;i<Paths.length;i++)
			{
				File dir = new File(Paths[i]);
				if(dir.exists())
				{
					path = Paths[i];
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return path;
	}
	
	/**
	 * RK : /mnt/usb_storage/
	 * Allwinner : /mnt/usbhost1/
	 * MTK : /mnt/usbotg/;/storage/usbotg/usbotg-sda1/
	 * Intel : /storage/usbcard1/
	 * Amlogic:/storage/external_storage/sda1/
	 * @return
	 */
	public static String getRealUSBpath()
	{
		String[] Paths = {"/mnt/usb_storage/","/mnt/usbhost1/","/mnt/usbotg/","/storage/usbcard1/","/storage/usbotg/","/storage/external_storage/"};
		String path = "/mnt/";
		try {
			for(int i = 0;i<Paths.length;i++)
			{
				File dir = new File(Paths[i]);
				if(dir.exists())
				{
					path = Paths[i];
					if(path.equals("/storage/usbotg/")||path.equals("/storage/external_storage/"))
					{
						File[] files = new File(path).listFiles();
						if(files.length!=0)
							path = path+files[0].getName()+"/";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return path;
	}

}
