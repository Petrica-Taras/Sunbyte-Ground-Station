package uk.co.sunbyte.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Petrica Taras
 * @version 1.1
 * 
 * Creates a server which can communicate with Arduino and Raspberry Pi
 * using preestablished commands
 * 
 **********************************************************************
 * Yes .. ahaah ... this is a tough game ... Jimmy knows that but ...
 * what on Earth possessed him to think that horse racing was going to
 * be any easier? 
 **********************************************************************
 */
public class EthernetConnection {
    int portNumber;
    
    int connectionStatus;
    
    Socket clientSocket;
    
    /**
     * 
     * @param hosts - available hosts (including this PC) and
     * their ports
     */
	public EthernetConnection() {
 
	}
	
	public String sendCommandToClient(String client, int portNumber, String send) {
		String response = null; 
		try {
	        clientSocket = new Socket(client, portNumber);
	        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	        outToServer.writeBytes(send);
	        InputStream clientInputStream = clientSocket.getInputStream();
	        response = readFromClient(new BufferedReader(new InputStreamReader(clientInputStream)));
	        System.out.println(response);
	        clientSocket.close();
	        return response; 
	    }
	    catch (IOException e) {
	        this.connectionStatus = 0; // shit happened ... expand this into a range of errors!
	    }
		return response;	
	}

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
