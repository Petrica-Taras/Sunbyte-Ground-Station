package uk.co.sunbyte.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import uk.co.sunbyte.controller.ConnectionListener;

/**
 * Keeps track of whatever details are relevant for connecting to 1
 * particular device (e.g. an Arduino device, etc).
 * 
 * @author Petrica Taras
 */
public class SensorConnectionManagement {
	private String ip;
	private int port;
	private EthernetConnection ethConn; 
	public String response;
	private List<ConnectionListener> listeners;
	
	public SensorConnectionManagement(String ip, int port) {
		this.ip = ip;
		this.port = port;
		
		this.listeners = new ArrayList<ConnectionListener>();
		this.ethConn = new EthernetConnection(); 
	}

	public void addListener(ConnectionListener c) {
		this.listeners.add(c);
	}
	
	public void connect() {
		this.response = ethConn.sendCommandToClient(this.ip, this.port, "20112");
		if(response!= null) {
			this.update(response+"\n");
		}
		else {
			;//this.update("No sensor retrieved\n");
		}
	}
	
	public void update(String data) {
		for(ConnectionListener c: this.listeners) {
			c.notifyDestination(data);
		}
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
