package uk.co.sunbyte.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Petrica Taras
 * @version 1.0
 * 
 ********************************************************
 * Keeps track of experiment status - basically manages
 * or groups data so that the Controller works with a 
 * single object instead of tracking images and sensors
 *
 ********************************************************
 * "Sorry what kind of people are you?
 * "Uhmm ..."
 * "The bath is not white"
 * "Uhmm, no, no, is sort of green isn't it?" 
 * "IT'S AVOCADO YOU CUNT"
 */
public class ExperimentStatus {
    private Map<String, Sensor> sensors;
    private Map<String, String> images; 
    private Map<String, String> onlineBoards;
    private Map<String, String> onlineRelays;
    
	public ExperimentStatus() {
	    this.sensors = new HashMap<String, Sensor>();
	    this.images = new HashMap<String, String>(); 
	    this.onlineRelays = new HashMap<String, String>(); 
	}
	
	public void addSensor(Sensor sensor) {
		this.sensors.put(sensor.getName(), sensor);
	}
	
	public void addSensors(Map<String, Sensor> input) {
			this.sensors.putAll(input);
	}
	
}
