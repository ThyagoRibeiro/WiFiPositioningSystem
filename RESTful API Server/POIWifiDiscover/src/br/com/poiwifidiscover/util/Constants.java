package br.com.poiwifidiscover.util;

public class Constants {

	protected static final String RESOURCES_PATH = System.getProperty("catalina.base") + "/POIWifiDiscover/resources";

	private static final String WSML_PATH = RESOURCES_PATH + "/wsml";
	public static final int MIN_RSSI = -300;
	public static final String WIFIDATA_FILE = WSML_PATH + "/WifiDataInstances.wsml";
	private static final String ARFF_PATH = RESOURCES_PATH + "/arff";
	public static final String ARFF_FILE = ARFF_PATH + "/poiData.arff";
}
