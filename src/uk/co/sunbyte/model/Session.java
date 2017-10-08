package uk.co.sunbyte.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Petrica Taras
 * @version 1.0 
 *
 *********************************************************
 * Keeps track of the current
 * session (singleton)
 *  
 ********************************************************* 
 * It works like this:
 * 1) creates a folder 
 * 2) keeps a path to that folder
 * 3) creates a session log (only for that particular session)
 * 4) has a method write which logs all the events in the journal
 *  
 *********************************************************
 * Oooh and that's a bad miss ... 
 *********************************************************
 *
 */
public class Session {
	private static Session INSTANCE = null;
	// private UUID uuid;
	private String uuid;
	
	// should dig into Java, surely a string can be escaped 
	// with some method to get rid off "\"	
	private String localPathFolder;
	
	private String sessionPathFolder; 
	private String sessionLogFilename;
	private BufferedWriter sessionLog;
	
	private AppPreferences appPref; // persistence for all app settings
	
	private String lastSeen; // to be interogated by sensors and event managers
	
    private Session () throws IOException, InterruptedException {
    	appPref = new AppPreferences(); 
    	// this.uuid = UUID.randomUUID();
    	// make it out of date for now, should be unique enough!
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");

    	this.uuid = dateFormat.format(new Date());    	
    	
    	// creates folder
    	Path currentRelativePath = Paths.get("");
    	localPathFolder = currentRelativePath.toAbsolutePath().toString();
    	
    	// Path path = Paths.get(localPath+"\\"+uuid.toString());
    	sessionPathFolder = localPathFolder+"\\"+this.uuid;
    	
    	if(this.checkItExistOnDisk()) {
    		Thread.sleep(1500);
        	this.uuid = dateFormat.format(new Date());
    		sessionPathFolder = sessionPathFolder +" "+ this.uuid;
    	}
    	
    	Path path = Paths.get(sessionPathFolder);
    	
    	Files.createDirectories(path);
    	sessionLogFilename = this.uuid + ".log";
    	this.write("Session started at: " + this.uuid + "\n");

    	lastSeen = getLastSeen();
    }
    
	public AppPreferences getAppPref() {
		return appPref;
	}

	public void write(String s) throws IOException {
		this.sessionLog = new BufferedWriter(new FileWriter(sessionPathFolder +"\\"+ sessionLogFilename, true));
		this.sessionLog.write(s);
	    this.sessionLog.flush();
		this.sessionLog.close();
	}

	/**
	 * Tests if the session has already created the general
	 * unique folder, basically required to avoid clashes with 
	 * previously created folders as the uniqueness depends on 
	 * time and date
	 *  
	 * @return true if {@link Session#sessionPathFolder} already exists 
	 */	
	private boolean checkItExistOnDisk() {
		Path path = Paths.get(this.sessionPathFolder);
		if(Files.exists(path)) {
			return true;
		}
		return false;
	}

	/**
	 * Tests if the session has already created a fileName file into
	 * the session folder. Intended for uses with repeated writings to
	 * the same file for sensors in order to avoid writing the sensor
	 * headers multiple times
	 *  
	 * @return true if fileName already exists 
	 */	
	private boolean checkItExistOnDisk(String fileName) {
		Path path = Paths.get(this.sessionPathFolder + "\\" + fileName);
		if(Files.exists(path)) {
			return true;
		}
		return false;
	}
	
    public String getLastSeen() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	return dateFormat.format(new Date());
    }
    
    public static synchronized Session getInstance() throws IOException, InterruptedException {
        if (INSTANCE == null) INSTANCE = new Session();
        return INSTANCE;
    }
    
    public void writeSensorData(Sensor sensor) throws IOException {
    	// form file name:
    	String sensorLogFileName = sensor.getName() + this.uuid + ".csv";
    	BufferedWriter sensorFile = new BufferedWriter(new FileWriter(sessionPathFolder +"\\"+ sensorLogFileName, true));
    	
    	if(!this.checkItExistOnDisk(sensorLogFileName)) {
    		sensorFile.write(sensor.getName() + "\n\n");
    		ArrayList<String> names = sensor.getDataNames();
    		for(String s: names) {
    		    sensorFile.write(s + " ");
    		}
    		sensorFile.write("\n");
    	}
    	
    	ArrayList<String> data = sensor.getStringData();
    	for(String s: data) {
    	    sensorFile.write(s + "\n");
    	}
    	
    	sensorFile.close(); 
    }
}
