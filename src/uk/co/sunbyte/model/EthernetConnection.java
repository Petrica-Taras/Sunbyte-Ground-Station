package uk.co.sunbyte.model;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Petrica Taras
 * 
 * Yes .. ahaah ... this is a tough game ... Jimmy knows that but ...
 * what on Earth possessed him to think that horse racing was going to
 * be any easier? 
 * 
 * This is crap so far, I have mixed both client and server stuff
 * have no clue what I am doing, at least the Server part works
 */
public class EthernetConnection implements IO {
    String ipAddress;
    int portNumber;
    String local;
    
    int connectionStatus;
    
    Socket clientSocket;
    ServerSocket serverSocket;
    
    /**
     * 
     * @param clientAddress - a string "169.254.170.169"
     */
	public EthernetConnection(String clientAddress) {
		this.ipAddress = clientAddress;
		this.portNumber = 9999;     // defaults to telnet, but do we need it?
		this.local = "hostname";
		
		
	}
	
	// custom port constructor 1
	public EthernetConnection(String address, int number) {
		this.ipAddress = address;
		this.portNumber = number;
		this.local = address;
	}	
	
	// custom constructor 2
	public EthernetConnection(String address, int number, String machine) {
		this.ipAddress = address;
		this.portNumber = number;
		this.local = machine;
	}	
	
	// custom port constructor 3
		public EthernetConnection(String clientAddress, int clientPortNumber, int ServerPortNumber, String machine) {
			this.ipAddress = clientAddress;
			this.portNumber = clientPortNumber;
			this.local = machine;
		}		
	
	@SuppressWarnings("deprecation")
	@Override
	public String pullData() throws IOException {
		String line = new String(); 
		try {
	        this.serverSocket = new ServerSocket(9999); // need to make this an input
	        /*assumes arduino and stuff is already sending towards the ground station
	         * need to make a new object for each device - a manager (factory) is 
	         * necessary!*/
	        
	        clientSocket = serverSocket.accept();
//	           BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//	    // As long as we receive data, echo that data back to the client.
//	           //while (true) {
//	             line = is.readLine();
	             // System.out.println(line); 
	           //}
	        line = readInputStream(clientSocket.getInputStream());
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }   
		finally {
//			clientSocket.close();
//			serverSocket.close(); 
		}
		// line = "very much testing";//return line;
		System.out.println(line);
		return line;//return line;
	    }

	@Override
	public void pushData() {
		try {
	        clientSocket = new Socket(this.local, this.portNumber);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        outToServer.writeBytes("Testing\n");
	        clientSocket.close();
	    }
	    catch (IOException e) {
	        this.connectionStatus = 0; // shit happened ... expand this into a range of errors!
	    }	

	}
	
	public void pushData(String s) {
		try {
	        clientSocket = new Socket(this.local, this.portNumber);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        outToServer.writeBytes(s);
	        clientSocket.close();
	    }
	    catch (IOException e) {
	        this.connectionStatus = 0; // shit happened ... expand this into a range of errors!
	    }	

	}

	@Override
	public void Log(Session log) {
		// TODO Auto-generated method stub
		
	}	
	
	String readInputStream(InputStream inputStream) throws IOException {
		  final int bufferSize = 1024;
		  final char[] buffer = new char[bufferSize];
		  final StringBuilder out = new StringBuilder();
		  Reader in = new InputStreamReader(inputStream, "UTF-8");
		  //for (; ; ) {

		  while (in.ready()) {
		      int rsz = in.read(buffer, 0, buffer.length);
		      if (rsz < 0)
		          break;
		      out.append(buffer, 0, rsz);
		  }
		  return out.toString();
		 }
	
}
