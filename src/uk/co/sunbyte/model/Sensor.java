package uk.co.sunbyte.model;


/**
 * @author Petrica Taras
 * @version 1.0
 * 
 * Holds data from the sensors (both text and converts to floats).
 * Can conveniently flush the 
 */
public class Sensor {
    private String name;
    /* floatData: for storing actual data
     * format (column headers): time, data1, data2 and so on
     * */
    private double[][] floatData; 
    private StringBuffer data;
    
    // flags;
    private boolean isInitialized = false;
    
    public Sensor(String name) {
        this.name = name;
    }
    
    public Sensor(String name, String data) {
    	this.name = name;
    	this.data = new StringBuffer(data);
    	this.isInitialized = true;
    	
    	this.stripStringData();
    }    
    
    public boolean isEmpty() {
    	return this.isInitialized;
    }
    
    public String toString() {
    	return this.name+"\n\n"+data.toString();
    }
    
    public String getName() {
		return name;
	}
    
    // TODO: floatData is not initialized everywhere is 
    // needed!
    public double[][] getFloatData() {
    	return floatData;
    }

    public String flushContent() {
    	this.isInitialized = false;
    	StringBuffer auxData = new StringBuffer(this.data.toString());
    	this.data.setLength(0);
    	this.floatData = new double[0][0]; // make sure it is empty!
    	
    	return auxData.toString();    	
    }
    
    /**
     * redundant functionality here! 
     *
     */
    public void add(String newData) {
    	if(this.isEmpty()) {
    		this.data = new StringBuffer(); 
    		this.isInitialized = true;
    	}
    	
    	data.append("\n"+newData);
    	this.stripStringData();
    }
    
    /*
     * Splits string data (assuming it comes into data columns)
     * does not make any assumptions about the number of rows and
     * columns. 
     * does not do any safety checks either, damn it!
     * **/
    private void stripStringData() {
    	String[] lines = this.data.toString().split("\n");
    	
    	// both assumes nicely formatted string
    	int rows = lines.length;
    	int cols = lines[0].split(" ").length;
    	
    	this.floatData = new double[rows][cols]; // if floatData already exists it overrides it
    	
    	int rowIndex = 0; 
    	
    	for(String s: lines) {
    	    String[] currentRow = s.split(" ");
    	    for(int i = 0; i < cols; i++) {
    	    	this.floatData[rowIndex][i] = Double.parseDouble(currentRow[i]);
    	    }
    	    // System.out.println(colIndex);    	    
    	    rowIndex++;
    	}
    	
    	// bleah!
    }
}
