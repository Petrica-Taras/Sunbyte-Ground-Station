package uk.co.sunbyte.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Petrica Taras
 * @version 1.0 
 *
 *********************************************************
 * Keeps track of the current
 * session. Should be a singleton. Two constructors are necessary:
 * 1) new, fresh session
 * 2) resuming an old or interrupted existing session
 *  
 ********************************************************* 
 * It works like this:
 * 1) creates a folder (constructor 1) - if constructor 2 
 * is called it uses the already existing constructor
 * 2) keeps a path to that folder
 * 3) creates a session log (only for that particular session)
 * if constructor 1 is used. If not (constructor 2), it just 
 * appends to existing ones
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
	private String uuid; // keep it sexy!
	
	// should get this from the View
	// should dig into Java, surely a string can be escaped 
	// with some method to get rid off "\"	
	private String localPathFolder;// = "H:\\code\\java\\workspace\\ground station";
	
	private String sessionPathFolder; 
	private String sessionLogFilename;
	private BufferedWriter sessionLog;
	
	private AppPreferences appPref;
	
	private String lastSeen;
	
    private Session () throws IOException, InterruptedException {
    	appPref = new AppPreferences(); 
    	// this.uuid = UUID.randomUUID();
    	// make it out of date for now, should be unique enough!
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");

    	this.uuid = dateFormat.format(new Date());    	
    	
    	// creates folder
    	Path currentRelativePath = Paths.get("");
    	localPathFolder = currentRelativePath.toAbsolutePath().toString();
    	//System.out.println(localPathFolder);
    	// Path path = Paths.get(localPath+"\\"+uuid.toString());
    	sessionPathFolder = localPathFolder+"\\"+this.uuid;
    	
    	if(this.checkItExistOnDisk()) {
    		Thread.sleep(1500);
        	this.uuid = dateFormat.format(new Date());
    		sessionPathFolder = sessionPathFolder +" "+ this.uuid;
    	}
    	
    	Path path = Paths.get(sessionPathFolder);
    	
    	Files.createDirectories(path);
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

    public String getLastSeen() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	return dateFormat.format(new Date());
    }
    
    public static synchronized Session getInstance() throws IOException, InterruptedException {
        if (INSTANCE == null) INSTANCE = new Session();
        return INSTANCE;
    }
}
