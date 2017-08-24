package uk.co.sunbyte.model;

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
	// private static final Session INSTANCE = new Session();
	
	// private UUID uuid;
	private String uuid; // keep it sexy!
	
	// should get this from the View
	// should dig into Java, surely a string can be escaped 
	// with some method to get rid off "\"	
	private String localPathFolder = "F:\\code\\java\\workspace\\ground station";
	private String sessionPathFolder; 
	private String sessionLogFilename;
	
    public Session () throws IOException {
    	// this.uuid = UUID.randomUUID();
    	// make it out of date for now, should be unique enough!
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
    	Date date = new Date();
    	this.uuid = dateFormat.format(date);    	
    	
    	// creates folder
    	// Path path = Paths.get(localPath+"\\"+uuid.toString());
    	sessionPathFolder = localPathFolder+"\\"+uuid;
    	
    	if(this.checkItExistOnDisk()) {
    		sessionPathFolder = sessionPathFolder +" "+ UUID.randomUUID().toString();
    	}
    	
    	Path path = Paths.get(sessionPathFolder);
    	
    	Files.createDirectories(path);
    	
    	
    	
    }
    
    // this one should be easier on test cases!
    public Session(String uuid) throws IOException {
	    this.sessionPathFolder = this.localPathFolder+"\\" + uuid;     	
    	if(!this.checkItExistOnDisk()) {
    	    Path path = Paths.get(this.sessionPathFolder);
    	    Files.createDirectories(path);
    	    
    	} else {
    		// TODO: need to finish this constructor!
    	}
    }

	public void write(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * @return true if {@link Session#sessionPathFolder} already exists 
	 */	
	private boolean checkItExistOnDisk() {
		Path path = Paths.get(this.sessionPathFolder);
		if(Files.exists(path)) {
			return true;
		};
		return false;
	}

 
    
}
