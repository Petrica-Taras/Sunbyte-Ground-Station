package uk.co.sunbyte.model;

import java.io.IOException;

/**
 * @author Petrica Taras
 * @version 1.0
 *
 * ********************************************************
 * I/O abstraction, basically to hide the details of reading and 
 * writing to data streams. Also provides an unified way of dealing
 * with I/O. 
 * 
 * ******************************************************** 
 * Would be nice to: implement a decorator design pattern, kind of
 * like the real Java I/O model which would allow cute stuff like 
 * sending an SDD object into a network one and other way around. 
 *
 *********************************************************
 * I wish I was at the Crucible now!
 * We both do Peter ... we both do. 
 * ********************************************************
 * 
 */
public interface IO {
    
	/* relying on exception handling to deal 
	 * with bad stuff! */
    public String pullData() throws IOException, Exception;
    public void pushData() throws IOException;
    
    /* Ok, not the most brilliant name scheme ever
     * every I/O object needs to let the session
     * manager know when it writes/reads from
     * data streams
     * */
    public void Log(Session log);
    
    /* to force implementations to strip data into strings
     * so it can be passed around */
    public String toString(); 
}
