package uk.co.sunbyte.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;



public class Window extends JFrame{
    private String winTitle = "Sunbyte Project: ";		
    public Menubar menubar;
    private GridBagLayout layout;
    private GridBagConstraints constraints; 
    public StatusBar statusbar;
    // default will start in full screen
    
    public Map<String, String> settings; // centralises all of the settings
    
	public Window() {
		this.settings = new HashMap<String, String>();
		this.settings.put("localhost IP", "169.254.131.160");
		this.settings.put("Leonardo IP", "169.254.131.159");
		this.settings.put("EtherMega IP", "169.254.131.158");
		
        initUI();
    }
	
	private void initUI() {
		menubar = new Menubar(this);
		statusbar = new StatusBar(); 
		
		layout = new GridBagLayout();
		this.constraints = new GridBagConstraints();
		this.setLayout(layout);
		
		// one cell for now
		constraints.gridx = 0;
		constraints.gridy = 0; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		PlotCartesian plotCart = new PlotCartesian("Time [s]", 
				             "Temperature [C]",
				             new Dimension(900, 500),
				             new Dimension(5, 4),
				             new double[][]{{80.0, 20.0}, 
                                            {95.0, 22.0},
                                            {101.0, 22.1},
			                                {200.0, 40.0}, 
			                                {350.0, 75.0}, 
			                                {500.0, 100.0}});
		this.add(plotCart, constraints);
        // second cell		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.weightx = 1; // these shall be computed!
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		PlotPolar plotPolar = new PlotPolar("Orientation", 
				                            new Dimension(500, 500),
				                            new Dimension(5, 36),
								            new double[][]{{80.0, 20.0}, 
	                                            {95.0, 22.0},
	                                            {101.0, 22.1},
				                                {200.0, 40.0}, 
				                                {350.0, 75.0}, 
				                                {500.0, 100.0}} 		                            );
		this.add(plotPolar, constraints);
		Border bd1 = BorderFactory.createLineBorder(new Color(255, 0, 0));    
	    plotCart.setBorder(bd1);
	    plotPolar.setBorder(bd1);
	    
	    // bottom cell, statusbar location
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.weightx = 1; // these shall be computed!
		constraints.weighty = 0.1;		
		constraints.gridwidth = 2; // or as many cols available
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LAST_LINE_START;
		
		this.add(statusbar, constraints);
          	
//    	
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setVisible(true); 
		
		this.setTitle(winTitle);
		
		ImageIcon img = new ImageIcon("C:/Users/Petrica Taras/workspace/ground station/src/res/images/iconApp_32x32.png");
		this.setIconImage(img.getImage());	
		
	}
	
	public void addWidget(JPanel obj) {
		this.add(obj);
	}
	
	private void initSettingsFromXMLfile() {
		
	}
}
