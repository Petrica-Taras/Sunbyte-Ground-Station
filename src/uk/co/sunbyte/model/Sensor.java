package uk.co.sunbyte.model;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.sunbyte.controller.ConnectionListener;

/**
 * @author Petrica Taras
 * @version 1.0
 * 
 ********************************************************* 
 * Holds data from the sensors.
 * Can accommodate sensors with multiple outputs (i.e. the
 * power sensors) but also to group same type of sensors
 * in a single data structure, convenient for later use
 * with plot classes (example: temperature sensors) 
 * Can conveniently flush the sensor if MAX_DATA entries 
 * are reached, in order to show only the last MAX_DATA
 * collected entries 
 * 
 * ******************************************************** 
 * "... uh and actually ... now I notice this ... this is an
 * alcoholic lager beer, isn't it John? ... 10% ... blimey 
 * ... that's quite a lot isn't it John?" 
 * "Yes Hugh, it's the most alcohol per mililiter, at the
 * lowest cost in this corner shop" 
 * 
 * ******************************************************** 
 * 
 */
public class Sensor implements ConnectionListener {
	private String sensorName; 
    private ArrayList<String> dataNames;
    /* sensorName: to store the sensor/sensor batch name
     * floatData: for storing actual data in double format
     * stringDataEntries: for storing actual data in String format
     * format (column headers): time, data1, data2 and so on
     * maximum data available is MAX_DATA - after this, data at 
     * the beginning of floatData & stringDataEntries is flushed out
     * 
     * */
    private int MAX_DATA = 200;
    private ArrayList<double[]> floatData; 
    private ArrayList<String> stringDataEntries;
    
    // flags;
    private boolean isInitialized = false;
    private boolean isEmpty = true;
    
    public Sensor(String sensorName) {
    	this.sensorName = sensorName;
    }
    
    public Sensor(String sensorName, String[] dataNames) {
    	this(sensorName);
    	this.dataNames = new ArrayList<String>(); 

    	for(String s: dataNames) {
    		this.dataNames.add(s);    		
    	}
        
        floatData = new ArrayList<double[]>(); 
    	this.isInitialized = true;        
    }
    
    public Sensor(String sensorName, String[] dataNames, String data) {
    	this(sensorName, dataNames);
    	this.stringDataEntries = new ArrayList<String>();
    	
    	String[] lines = data.split("\n");
    	for(String s: lines) {
    	    this.stringToDataOneField(s);
    	}
    }    
    
    /**
     * tells if the sensor object is ready to receive data or not
     * @return true if the proper constructors have been used 
     */
    public boolean isEmpty() {
    	return !this.isInitialized;
    }
    
    public String toString() {
    	StringBuffer result = new StringBuffer("");
    	for(String s: this.dataNames) {
    		result.append(s + " ");
    	}
    	
    	result.append("\n\n");
    	
    	for(String s: this.stringDataEntries) {
    		result.append(s + "\n");
    	}
    	
    	return result.toString();
    }
    
    public String getName() {
		return this.sensorName;
	}
    
    public ArrayList<String> getDataNames() {
    	return this.dataNames;
    }
    
    public ArrayList<String> getStringData() {
    	return this.stringDataEntries;
    }
    
    public String getAbcissae() {
    	return this.dataNames.get(0);
    }
    
    public ArrayList<double[]> getFloatData() {
    	return floatData;
    }

    public ArrayList<String> flushContent() {
    	this.isInitialized = false;
    	// ArrayList<String> auxData = (ArrayList<String>) this.stringDataEntries.clone();
    	ArrayList<String> auxData = new ArrayList<String>(this.stringDataEntries);
    	this.stringDataEntries.clear();
    	this.floatData.clear(); // make sure it is empty!
    	
    	return auxData;    	
    }
    
    /**
     * receives data in the expected format and appends it to the existing 
     * one. TODO: DO NOT FORGET TO IMPLEMENT size checks against MAX_DATA
     * @param 
     *
     */
    public void add(String newData) {
    	if(this.isEmpty()) {
    		this.stringDataEntries = new ArrayList<String>(); 
    		this.isInitialized = true;
    	}
    	
    	stringDataEntries.add("\n"+newData);
    	this.stringToDataOneField(newData);
    }
    
    @SuppressWarnings("unused")
	private void add(double[] newData) {
    	if(this.isEmpty()) {
    		this.stringDataEntries = new ArrayList<String>();  
    		this.isInitialized = true;
    	}
    	
    	int n = newData.length; 
    	
    	StringBuffer auxData = new StringBuffer(); 
    	
    	for(int i = 0; i < n; i++) {
    		auxData.append(newData[i]);
			auxData.append(" ");
    	}
    	this.stringToDataOneField(auxData.toString());
    }
    
    public void add(double[][] newData) {
    	if(this.isEmpty()) {
    		this.stringDataEntries = new ArrayList<String>(); 
    		this.isInitialized = true;
    	}
    	
    	int rows = newData.length;
    	int cols = newData[0].length;
    	
    	StringBuffer auxData = new StringBuffer(); 
    	
    	for(int i = 0; i < rows; i++) {
    		for(int j = 0; j < cols; j++) {
    			auxData.append(newData[i][j]);
    			auxData.append(" ");
    		}
    		this.stringToDataOneField(auxData.toString());
    		auxData.delete(0, auxData.length());
    	}  	
    }
    
    public void add(ArrayList<double[]> newData) {
    	
    }
       
    /**
     * Splits string data (assuming it comes into data columns)
     * does assume one row at a time. 
     * does not do any safety checks either, damn it!
     * 
     * @param oneLineString is a string in format 
     * "time data1 data 2 ...data N\n" representing one
     * measurement by a (set of) sensor(s)
     * 
     * TODO: should return the number of items (for safety checks)
     * 
     * */
    private void stringToDataOneField(String oneLineString) {
    	String[] line = oneLineString.toString().split(" ");
    	// assumes nicely formatted string
    	double[] oneLineDouble = new double[line.length];
    	for(int i = 0; i < line.length; i++) {
    		oneLineDouble[i] = Double.parseDouble(line[i]);
    	}    	    	
    	
    	if(this.floatData.size() < this.MAX_DATA) {
    	    this.floatData.add(oneLineDouble); 
    	} else {
    		this.floatData.remove(0); 
    		this.floatData.add(oneLineDouble);
    	}
    	
    	this.stringDataEntries.add(oneLineString);
    }
    
    public int getFieldsSize() {
    	return this.floatData.size();
    }
    
    public int getColumnSize() {
    	return this.dataNames.size(); 
    }
    
    /**
     * Very important as it allows separating data from the Sensor
     * into another one. Useful to separate compass data for
     * polarPlot
     * 
     * @param index an array with strings to be added into the new sensor
     * @return the newly created object
     */
    public Sensor getSlice(String[] index) {
    	String[] newDataNames = new String[index.length];
    	StringBuffer newDataEntries = new StringBuffer(); 
    	
    	double[][] auxData = new double[this.getFieldsSize()][index.length];
    	
    	int counter = 0;   	
    	for(int i = 0; i < this.dataNames.size(); i++) {
    		for(int j = 0; j < index.length; j++) {
    			if(this.dataNames.get(i).equals(index[j])) {
    				newDataNames[j] = index[j];
    				// build auxData now
    				for(int k = 0; k < this.getFieldsSize(); k++) {
    					auxData[k][counter] = this.floatData.get(k)[j];
    				}
    				counter++;  
    			}
    		}
    	}
    	
    	for(int i = 0; i < this.getFieldsSize(); i++) {
    		for(int j = 0; j < index.length; j++) {
    			newDataEntries.append(auxData[i][j] + " ");
    		}
    		newDataEntries.append("\n");
    	}
    	
    	
    	return new Sensor(this.sensorName, newDataNames, newDataEntries.toString());
    }
    
    public Sensor getSlice(String[] index, String newSensorName) {
    	String[] newDataNames = new String[index.length];
    	StringBuffer newDataEntries = new StringBuffer(); 
    	
    	double[][] auxData = new double[this.getFieldsSize()][index.length];
    	
    	int counter = 0;   	
    	for(int i = 0; i < this.dataNames.size(); i++) {
    		for(int j = 0; j < index.length; j++) {
    			if(this.dataNames.get(i).equals(index[j])) {
    				newDataNames[j] = index[j];
    				// build auxData now
    				for(int k = 0; k < this.getFieldsSize(); k++) {
    					auxData[k][counter] = this.floatData.get(k)[j];
    				}
    				counter++;  
    			}
    		}
    	}
    	
    	for(int i = 0; i < this.getFieldsSize(); i++) {
    		for(int j = 0; j < index.length; j++) {
    			newDataEntries.append(auxData[i][j] + " ");
    		}
    		newDataEntries.append("\n");
    	}
    	
    	return new Sensor(newSensorName, newDataNames, newDataEntries.toString());
    }
    /**
     * Notifies the Session object about sensor data being received.
     * This info goes into the main log, via the Session object. 
     *  
     * @param session
     * @throws IOException
     */
    public void writeToLog(Session session) throws IOException {
    	session.write(this.sensorName + " data was received\n");
    	// session.write(this.toString()); 
    }
    
    /**
     * 
     * 
     * @param session
     * @throws IOException
     */
    public void writeToSensorLog(Session session) throws IOException {
    	session.writeSensorData(this); 
    }

	@Override
	public void notifyDestination(String text) {
		this.add(text);
		
	}
}
