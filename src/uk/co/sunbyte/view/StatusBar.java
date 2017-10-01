package uk.co.sunbyte.view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class StatusBar extends JPanel {
    JLabel meas;
    JLabel online;
    
    private Border bd;
    
    public StatusBar() {
    	this.meas = new JLabel("Last measurement: ");
    	this.meas.setForeground(new Color(0, 0, 255)); 
    	this.online = new JLabel("Main: yes| Sensors: yes| Motors: yes");
    	
    	this.bd = BorderFactory.createBevelBorder(BevelBorder.LOWERED); 
    	
    	this.add(this.meas);
    	this.add(this.online);
    	
    	this.setBorder(this.bd);
    }
    
    public void setLastMeasurement(String date) {
    	this.meas.setText("Last measurement: " + date);
    }
}
