package uk.co.sunbyte.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    
	public EthernetConnection(String clientAddress) {
		this.ipAddress = clientAddress;
		this.portNumber = 23;     // defaults to telnet, but do we need it?
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
	public void pullData() throws IOException {
		try {
	        this.serverSocket = new ServerSocket(9990); // need to make this an input
	        /*assumes arduino and stuff is already sending towards the ground station
	         * need to make a new object for each device - a manager (factory) is 
	         * necessary!*/
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }   
		
		try {
	           clientSocket = serverSocket.accept();
	           DataInputStream is = new DataInputStream(clientSocket.getInputStream());
	    // As long as we receive data, echo that data back to the client.
	           //while (true) {
	             String line = is.readLine();
	             System.out.println(line); 
	           //}
	             
	        }   
	    catch (IOException e) {
	           System.out.println(e);
	        }
		finally {
			clientSocket.close();
		}
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
	
}
