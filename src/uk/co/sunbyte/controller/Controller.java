package uk.co.sunbyte.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import uk.co.sunbyte.model.EthernetConnection;
import uk.co.sunbyte.model.Sensor;
import uk.co.sunbyte.model.Session;
import uk.co.sunbyte.view.Menubar;
import uk.co.sunbyte.view.PlotCartesian;
import uk.co.sunbyte.view.PlotPolar;
import uk.co.sunbyte.view.StatusBar;
import uk.co.sunbyte.view.TextPanel;


public class Controller extends JFrame {
//	JButton LeoDiagnose;
//	JButton EthMDiagnose; 
//	JButton Vibes;
//	JPanel bringTogether;
//	JTextArea InfoSensors; 
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();
	
    private String winTitle = "Sunbyte Project: ";		
    public Menubar menubar;
    private GridBagLayout layout;
    private GridBagConstraints constraints; 
    public StatusBar statusbar;	
    
    public PlotCartesian plotCart;
    public PlotPolar plotPolar;
    
    public TextPanel textPanel; 
    // default will start in full screen - does it have to?
    
    Session session; // should have a persistence object as a member - this should remember the widget sizes
    
    
    EthernetConnection ethConn;
    
    public Map<String, String> settings; // centralises all of the settings
    private Sensor sensor;             
    
    public Controller() throws IOException, InterruptedException {
    	session = Session.getInstance(); // this should be a singleton!
    	
		menubar = new Menubar(this);
    	
    	sensor = new Sensor("Temperature",
    	         new String[]{"Time", "CPU"}, 
    	            "80.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");
//    	System.out.println(width);
//    	System.out.println(height);
    	// start the dancing
    	this.settings = new HashMap<String, String>();
		this.settings.put(session.getAppPref().getGroundStationName(), session.getAppPref().getGroundStationIP());
		this.settings.put("Leonardo IP", "169.254.131.159");
		this.settings.put("EtherMega IP", "169.254.131.158");
		
		ethConn = new EthernetConnection("172.16.18.131", 9999);
		ethConn.pushData("testing"); 
		
		this.plotCart = new PlotCartesian(sensor,
                                          new Dimension(900, 500),
                                          new Dimension(5, 4));
		this.plotPolar = new PlotPolar("Orientation", 
                                          new Dimension(500, 500),
                                          new Dimension(5, 36),
	                                      new double[]{210.0, 310.0},
                                          80.0);
		
		textPanel = new TextPanel(new Dimension(1900, 500));
		
		statusbar = new StatusBar(); 
        this.statusbar.setLastMeasurement(session.getLastSeen());
        
        initMainPerspectiveUI(); // just put everything inside the layout so I can focus on logic here
        
        String s = new String(ethConn.pullData()); 
        String v = new String(ethConn.pullData()); 
        System.out.println(s);
        System.out.println(v);
        textPanel.appendText(s);
    	
    	// fast stuff okay!
//    	bringTogether = new JPanel();
//    	
//    	LeoDiagnose = new JButton("Test Leonardo");
//    	EthMDiagnose = new JButton("Test EtherMega");
//    	Vibes = new JButton("get vibrations");
//    	InfoSensors = new JTextArea();
//    	
//
//    	
//    	
//    	this.bringTogether.setLayout(new FlowLayout());  
//    	
//        EthernetConnection ethConnEtheMega = new EthernetConnection("169.254.131.158", 9999);
//        EthernetConnection ethConnLeonardo = new EthernetConnection("169.254.131.159", 9990);
//        
//        /*ethConn.pullData(); /*cannot overload pullData() with port number, 
//                             cause we will lose generality for SDD;
//                             maybe specify it in the constructor!*/
////        while(true) {
////        	ethConnEtheMega.pushData("d");}
//    	
//        LeoDiagnose.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	ethConnLeonardo.pushData("d");
//            }
//        });
//        
//        EthMDiagnose.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	ethConnEtheMega.pushData("d");
//            }
//        });
//        
//        Vibes.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	try {
//					ethConnLeonardo.pullData();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//            	//ethConnLeonardo.pushData("f");
//
//            }
//        });     
//        this.bringTogether.add(LeoDiagnose);
//        this.bringTogether.add(EthMDiagnose);
//        this.bringTogether.add(Vibes);
//        // this.bringTogether.add(InfoSensors);
//        
//        MainWin.addWidget(this.bringTogether);
//        MainWin.setVisible(true);
//    	Plot plot = new Plot("Time [s]", 
//    	 
//    			             "Temperature [C]", 
//    			             new double[][]{{80.0, 20.0}, {200.0, 40.0}, {350.0, 75.0}, {500.0, 100.0}});
//		MainWin.addWidget(plot);   		
      

    }
	private void initMainPerspectiveUI() {		
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
		
		this.add(plotCart, constraints);
        // second cell		
		constraints.gridx = 1; 
		constraints.gridy = 0; 
		constraints.weightx = 1; // these shall be computed!
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
				                           
		this.add(plotPolar, constraints);
		Border bd1 = BorderFactory.createLineBorder(new Color(255, 0, 0));    
	    plotCart.setBorder(bd1);
	    plotPolar.setBorder(bd1);
	    
	    // third/text cell
	    constraints.gridx = 0; 
		constraints.gridy = 2; 
		constraints.weightx = 1; // these shall be computed!
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		constraints.gridwidth = 2;
        
        Border bd2 = BorderFactory.createLineBorder(new Color(255, 0, 0));  
		this.add(textPanel, constraints);
		textPanel.setBorder(bd2); 
	    
	    // bottom cell, statusbar location
		constraints.gridx = 0; 
		constraints.gridy = 3; 
		constraints.weightx = 1; // these shall be computed!
		constraints.weighty = 0.1;		
		constraints.gridwidth = 2; // or as many cols available
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.LAST_LINE_START;	
		
		this.add(statusbar, constraints);
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setVisible(true); 
		
		this.setMinimumSize(new Dimension(1024, 768));
		
		this.setTitle(winTitle);
		
		ImageIcon img = new ImageIcon("C:/Users/Petrica Taras/workspace/ground station/src/res/images/iconApp_32x32.png");
		this.setIconImage(img.getImage());	
		
	}
	
	public void addWidget(JPanel obj) {
		this.add(obj);
	}
}
