package uk.co.sunbyte.model;

import java.util.ArrayList;

/**
 * @author Petrica Taras
 * @version 1.0
 * 
 ********************************************************* 
 * Holds data from the sensors.
 * Can accomodate sensors with multiple outputs (i.e. the
 * power sensors) but also to group same type of sensors
 * in a single data structure, convenient for later use
 * with plot classes (example: temperature sensors) 
 * Can conveniently flush the sensor if MAX_DATA entries 
 * are reached, in order to show only the last MAX_DATA
 * collected entries 
 * TODO - make stringData an array (correlated with floatData)
 * in order to easily remove data from the beginning!
 * 
 * ******************************************************** 
 * ... uh and actually ... now I notice this ... this is an
 * alcoholic lager beer, isn't it John? ... 10% ... blimey 
 * ... that's quite a lot isn't it John? 
 * Yes Hugh, it's the most alcohol per mililiter, at the
 * lowest cost in this corner shop 
 * 
 * ******************************************************** 
 * 
 */
public class Sensor {
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
    
    public void writeToLog(Session session) {
    	session.write(this.toString()); 
    }
}
