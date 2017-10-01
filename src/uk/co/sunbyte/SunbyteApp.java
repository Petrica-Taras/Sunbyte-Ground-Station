package uk.co.sunbyte;

import java.io.IOException;

import uk.co.sunbyte.controller.ControllerMainPerspective;
import uk.co.sunbyte.model.*;
import uk.co.sunbyte.view.PlotCartesian;

public class SunbyteApp {
	public static void main(String[] args) throws IOException, InterruptedException {
		// Window mainWin = new Window(); // let the party begin :D
        //EthernetConnection ethConn = new EthernetConnection("169.254.131.159", 9999);
        
        /*ethConn.pullData(); /*cannot overload pullData() with port number, 
                             cause we will lose generality for SDD;
                             maybe specify it in the constructor!*/
        //ethConn.pushData();
		/*SDD s = new SDD("test.txt", "Sensor to disk!");
		
		try {
			s.pushData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        // new Window();
		
		try {
			Session session = new Session();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println(System.getProperty("file.separator"));*/
		
		// Sensor s = new Sensor("Temperature", "1.0 35.02\n2.0 35.12\n3.0 36.23");
		
		// System.out.println(s.toString());
		
		ControllerMainPerspective c = new ControllerMainPerspective(); 

	}

}
