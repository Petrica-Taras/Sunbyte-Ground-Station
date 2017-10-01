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
		String data = new String();		
		try {
 
	        this.serverSocket = new ServerSocket(9999); // need to make this an input
	        /*assumes arduino and stuff is already sending towards the ground station
	         * need to make a new object for each device - a manager (factory) is 
	         * necessary!*/
	        
	        clientSocket = serverSocket.accept();
            BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//	    // As long as we receive data, echo that data back to the client.
	       //while (true) {
//	             line = is.readLine();
//            new Thread() {
//                public void run() {
//                	String data = new String();
//                	try {
//						data = readFromClient(is);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//        	        System.out.println(data); 
//                }
//            }.start();
            // while(true) {
            data = readFromClient(is);
            System.out.println(data);
            System.out.println("xxxxx");
            // }
	        //}
	        
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }   
		finally {
//			clientSocket.close();
//			serverSocket.close(); 
		}
		
		return data;
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
	public void writeToLog(Session session) {
		// TODO Auto-generated method stub
		
	}	
	
	 private static String readFromClient(BufferedReader inFromClient) throws IOException {
		  int c = 0;
		  StringBuilder sb = new StringBuilder();
		  while ((c = inFromClient.read()) >= 0) {
		   sb.append((char)c);
		   if (!inFromClient.ready()) {
		    break; //end of current message
		   }
		  }
		  return sb.toString();
		 }
	
}
