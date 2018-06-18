package br.com.poiwifidiscover.model;

import java.util.Date;
import java.util.HashMap;

public class WifiData {

	private Date date;
	private int idThing;
	private HashMap<String, Integer> wifiMap;

	public WifiData() {
		wifiMap = new HashMap<>();
		date = new Date();
	}

	public int get(String bssid) {
		return wifiMap.get(bssid);
	}

	public Date getDate() {
		return date;
	}

	public int getIDThing() {
		return idThing;
	}

	public HashMap<String, Integer> getWifiMap() {
		return wifiMap;
	}

	public void put(String bssid, int rssi) {
		wifiMap.put(bssid, rssi);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setIDThing(int qrCodeID) {
		this.idThing = qrCodeID;
	}

	public void setWifiMap(HashMap<String, Integer> wifiMap) {
		this.wifiMap = wifiMap;
	}

}