package com.liyu.itester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import com.liyu.itester.BatActivity;
import com.liyu.itester.BrightnessActivity;
import com.liyu.itester.BtActivity;
import com.liyu.itester.CameraActivity;
import com.liyu.itester.FlashlightActivity;
import com.liyu.itester.GPSActivity;
import com.liyu.itester.GsensorActivity;
import com.liyu.itester.GyroscopeActivity;
import com.liyu.itester.HwActivity;
import com.liyu.itester.KeyActivity;
import com.liyu.itester.LsensorActivity;
import com.liyu.itester.MP3Activity;
import com.liyu.itester.MP4Activity;
import com.liyu.itester.MacActivity;
import com.liyu.itester.MobileNetActivity;
import com.liyu.itester.OTGActivity;
import com.liyu.itester.RecorderActivity;
import com.liyu.itester.RgbActivity;
import com.liyu.itester.SDActivity;
import com.liyu.itester.TouchActivity;
import com.liyu.itester.VibrateActivity;
import com.liyu.itester.WifiActivity;
import com.liyu.itester.compass.CompassActivity;
import com.liyu.itester.entity.test_config_entity;
import com.liyu.itester.entity.test_item_entity;


public class XmlUtils {

	public static Map<String, test_item_entity> parameterMap = null;
	public static test_config_entity config_entity_vo = null;
	public static List<Class> activityClasses;
	public static List passList;
	public static List failList;
	public static String configPath;
	public static Map<String, Class> allTestItemMap = null;
	
	public static test_config_entity getConfigEntityInstance()
	{
		if (config_entity_vo == null) {
			init();
			return config_entity_vo;
		} else {
			return config_entity_vo;
		}
	}
	public static test_item_entity getEntity(String activityName){
		if(parameterMap == null){
			init();
		}else {
			return parameterMap.get(activityName);
		}
		return null;
	}
	
	public static void init_ItemMap()
	{
		allTestItemMap = new HashMap<String, Class>();
		allTestItemMap.put("GSENSOR",GsensorActivity.class);
		allTestItemMap.put("TP",TouchActivity.class);
		allTestItemMap.put("RGB",RgbActivity.class);
		allTestItemMap.put("WIFI",WifiActivity.class);
		allTestItemMap.put("BRIGHTNESS",BrightnessActivity.class);
		allTestItemMap.put("AUDIO",MP3Activity.class);
		allTestItemMap.put("VIDEO",MP4Activity.class);
		allTestItemMap.put("SDCARD",SDActivity.class);
		allTestItemMap.put("RECORD",RecorderActivity.class);
		allTestItemMap.put("CAMERA",CameraActivity.class);
		allTestItemMap.put("RAMFLASH",HwActivity.class);
		allTestItemMap.put("BLUETOOTH",BtActivity.class);
		allTestItemMap.put("GYROSCOPE",GyroscopeActivity.class);
		allTestItemMap.put("LIGHTSENSOR",LsensorActivity.class);
		allTestItemMap.put("OTG",OTGActivity.class);
		allTestItemMap.put("BATTERY",BatActivity.class);
		allTestItemMap.put("3G",MobileNetActivity.class);
		allTestItemMap.put("BUTTON",KeyActivity.class);
		allTestItemMap.put("GPS",GPSActivity.class);
		allTestItemMap.put("COMPASS",CompassActivity.class);
		allTestItemMap.put("VIBRATE",VibrateActivity.class);
		allTestItemMap.put("FLASHLIGHT",FlashlightActivity.class);
		allTestItemMap.put("MACADDRESS",MacActivity.class);
	}
	private static void init(){
		if(new File(configPath).exists()){
			try {
				init_ItemMap();
				init_xml(configPath);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void init_xml(String path) throws XmlPullParserException, IOException {
		FileInputStream fileInputStream = new FileInputStream(path);

		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser parser = factory.newPullParser();
		parser.setInput(fileInputStream, "UTF-8");

		test_item_entity entity = null;
		int event = parser.getEventType();
		activityClasses = new ArrayList<Class>();

		passList = new ArrayList();
		failList = new ArrayList();

		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				parameterMap = new HashMap<String, test_item_entity>();
				config_entity_vo = new test_config_entity();
				break;
			case XmlPullParser.START_TAG:
				if("TabletTest".equalsIgnoreCase(parser.getName())){
					config_entity_vo.setLanguage(parser.getAttributeValue(null, "Language"));
					config_entity_vo.setExternalSDPath(parser.getAttributeValue(null, "ExternalSDPath"));
					config_entity_vo.setWiFiSSID(parser.getAttributeValue(null, "WiFiSSID"));
					config_entity_vo.setWiFiPassWord(parser.getAttributeValue(null, "WiFiPassWord"));
					config_entity_vo.setImageVersion(parser.getAttributeValue(null, "ImageVersion"));
					config_entity_vo.setWiFiAddressStart(parser.getAttributeValue(null, "WiFiAddressStart"));
					config_entity_vo.setBTAddressStart(parser.getAttributeValue(null, "BTAddressStart"));
					config_entity_vo.setIMEIStart(parser.getAttributeValue(null, "IMEIStart"));
				}
				
				if("Item".equalsIgnoreCase(parser.getName())){
					entity = new test_item_entity();
					entity.setName(parser.getAttributeValue(null, "name"));
					entity.setEnable(parser.getAttributeValue(null, "enable"));
					entity.setParameter(parser.getAttributeValue(null, "parameter"));
					if("true".equalsIgnoreCase(entity.getEnable())){
						if(entityToClass(entity.getName().toUpperCase()) != null){
							activityClasses.add(entityToClass(entity.getName().toUpperCase()));
						}
					}
					parameterMap.put(entityToClass(entity.getName().toUpperCase()).getSimpleName(), entity);
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		fileInputStream.close();
	}
	
	public static Class entityToClass(String entityName){

		if(allTestItemMap!=null)
		{
			return allTestItemMap.get(entityName);
		}
		else 
		{
			init_ItemMap();
			return allTestItemMap.get(entityName);
		}
	}

}
