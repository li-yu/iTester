package com.liyu.itester.entity;

public class test_config_entity {

	private String Language,ExternalSDPath,WiFiSSID,WiFiPassWord,ImageVersion,WiFiAddressStart,
	BTAddressStart,IMEIStart;
	

	
	public String getExternalSDPath()
	{
		return this.ExternalSDPath;
	}
	public void setExternalSDPath(String path)
	{
		this.ExternalSDPath = path;
	}
	
	public String getWiFiSSID()
	{
		return this.WiFiSSID;
	}
	public void setWiFiSSID(String SSID)
	{
		this.WiFiSSID = SSID;
	}
	
	public String getWiFiPassWord()
	{
		return this.WiFiPassWord;
	}
	public void setWiFiPassWord(String Password)
	{
		this.WiFiPassWord = Password;
	}
	
	public String getImageVersion()
	{
		return this.ImageVersion;
	}
	public void setImageVersion(String version)
	{
		this.ImageVersion = version;
	}
	public String getLanguage()
	{
		return this.Language;
	}
	public void setLanguage(String language)
	{
		this.Language = language;
	}
	
	public String getWiFiAddressStart()
	{
		return this.WiFiAddressStart;
	}
	public void setWiFiAddressStart(String WiFiAddressStart)
	{
		this.WiFiAddressStart = WiFiAddressStart;
	}
	
	public String getBTAddressStart()
	{
		return this.BTAddressStart;
	}
	public void setBTAddressStart(String BTAddressStart)
	{
		this.BTAddressStart = BTAddressStart;
	}
	
	public String getIMEIStart()
	{
		return this.IMEIStart;
	}
	public void setIMEIStart(String IMEIStart)
	{
		this.IMEIStart = IMEIStart;
	}
}
