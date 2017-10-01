package uk.co.sunbyte.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Petrica Taras
 * @version 1.0
 *
 *********************************************************
 * SSD: object to deal with writing to disk
 * Strategy: to keep a flag and flush the content 
 * to hdd
 * 
 *********************************************************
 * ... and we have an absolute cracking night of late night dog
 * poker in prospect .. and I for one I am very excited ... What
 * about you Peter?
 *********************************************************
 * 
 */
public class SSD implements IO {
	// TODO: make a flag to make sure when
    private String filename;
    private StringBuffer content;

    public SSD(String filename) {
    	this.filename = filename;
    	this.content = new StringBuffer();
    }
    
	public SSD(String filename, String content) {
		this.filename = filename;
		this.content = new StringBuffer(content);
	}
	
	@Override
	public String pullData() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(this.filename));
        String sCurrentLine = "";
        try {
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
			    this.content.append(sCurrentLine+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return sCurrentLine;
	}

	public void pullData(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String sCurrentLine;
        try {
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
			    this.content.append(sCurrentLine+"\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	@Override
	public void pushData() throws IOException {
		PrintWriter writer = new PrintWriter(this.filename, "UTF-8");
		// PrintWriter pw = new PrintWriter(new FileOutputStream(new File("persons.txt"),true));
	    writer.println(this.content.toString());
	    writer.close();
	}
	
	public void pushData(String content) throws IOException {
		PrintWriter writer = new PrintWriter(this.filename, "UTF-8");
	    writer.println(content);
	    writer.close();
	}
	@Override
	public void writeToLog(Session session) throws IOException {
		session.write("Flushed to disk!");
		
	}	
		
}
