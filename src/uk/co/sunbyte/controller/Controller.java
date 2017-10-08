package uk.co.sunbyte.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import uk.co.sunbyte.model.EthernetConnection;
import uk.co.sunbyte.model.Sensor;
import uk.co.sunbyte.model.Session;
import uk.co.sunbyte.view.ImagePanel;
import uk.co.sunbyte.view.Menubar;
import uk.co.sunbyte.view.PlotCartesianPanel;
import uk.co.sunbyte.view.PlotPolar;
import uk.co.sunbyte.view.StatusBar;
import uk.co.sunbyte.view.TextPanel;


@SuppressWarnings("serial")
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
    
    public PlotCartesianPanel plotCart11;
    public PlotCartesianPanel plotCart12;
    public PlotCartesianPanel plotCart21;
    public PlotCartesianPanel plotCart22;
    public PlotCartesianPanel plotCart31;
    public PlotCartesianPanel plotCart32;
    
    public PlotPolar plotPolar;
    
    public ImagePanel imgTrack; 
    
    public TextPanel textPanel; 
    /**
     * main containers
     */
    JTabbedPane main;
	JPanel defaultPerspective;
	JPanel imagingPerspective;
	JPanel settingsPerspective;  
	// top level can be used for a JToolBar() if you want a quick toolbar
	BorderLayout mainBL;
    
    // default will start in full screen - does it have to?
    
    Session session; // should have a persistence object as a member - this should remember the widget sizes
    
    EthernetConnection ethConn;
    
    /**
     * 
     * Important for layout management and keeping track of various 
     * widgets dimensions
     * If widget border resizing is to be 
     * implemented later on, this is crucial.
     * Also, it should be populated as soon as components are added.  
     * 
     * */
    private Dimension[] layoutPartitions; 
    
    public Map<String, String> settings; // centralises all of the settings
    
    private Sensor sensor11;    
    private Sensor sensor12;
    private Sensor sensor21;    
    private Sensor sensor22;
    private Sensor sensor31;    
    private Sensor sensor32;
	private ImagePanel imgFocus;
    
    public Controller() throws IOException, InterruptedException {
    	// first of all this as it contains settings for the rest of the app!
    	session = Session.getInstance(); 
    	
    	// always on screen
		menubar = new Menubar(this);
		main = new JTabbedPane();
		statusbar = new StatusBar();
		
        this.statusbar.setLastMeasurement(session.getLastSeen());
        
		// this.img = new ImagePanel("hot_air_balloon_kidnapping.png");
		this.imgTrack = new ImagePanel(session.getLocalPathFolder()+"\\test.png");
		this.imgFocus = new ImagePanel(session.getLocalPathFolder()+"\\hot_air_balloon_kidnapping.png");
        
		imagingPerspective  = this.iniImagingPerspectiveUI();
		settingsPerspective = new JPanel(); 
		
		mainBL = new BorderLayout();
		this.setLayout(this.mainBL);
			
		
    	sensor11 = new Sensor("Plot 11",
    	         new String[]{"Time", "CPU", "CPU1"}, 
    	            "80.0 20.0 32.1\n95.0 22.0 89.0\n101.0 22.1 90.0\n200.0 40.0 99.1\n350.0 75.0 45.0\n500.0 100.0 33.0\n750 215 120.09");
    	sensor12 = new Sensor("Plot 12",
   	         new String[]{"Time", "CPU"}, 
   	            "40.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");

    	sensor21 = new Sensor("Plot 21",
   	         new String[]{"Time", "CPU"}, 
   	            "80.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");
      	sensor22 = new Sensor("Plot 22",
  	         new String[]{"Time", "CPU"}, 
  	            "40.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");

      	sensor31 = new Sensor("Plot 31",
   	         new String[]{"Time", "CPU"}, 
   	            "80.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");
     	sensor32 = new Sensor("Plot 32",
  	         new String[]{"Time", "CPU"}, 
  	            "40.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");

      	this.session.writeSensorData(sensor11);
      	this.session.writeSensorData(sensor12);
      	
    	// start the dancing
    	this.settings = new HashMap<String, String>();
		this.settings.put(session.getAppPref().getGroundStationName(), session.getAppPref().getGroundStationIP());
		this.settings.put("Leonardo IP", "169.254.131.159");
		this.settings.put("EtherMega IP", "169.254.131.158");
		
		ethConn = new EthernetConnection("172.16.18.131", 9999);
		// ethConn.pushData("testing"); 
		
		// Dimension plot = this.getPlotSize();
		// System.out.println(plot);
		
		Dimension plotDim = new Dimension((int) (this.width*0.35), 
				                          (int) ((this.height-28)*0.3));
		Dimension restPlotDim = new Dimension((int) (this.width*0.30),
				                              (int) (this.height-28));
		this.plotCart11 = new PlotCartesianPanel(sensor11,
                                          plotDim,
                                          new Dimension(5, 4));
		this.plotCart12 = new PlotCartesianPanel(sensor12,
                plotDim,
                new Dimension(5, 4));
		this.plotCart21 = new PlotCartesianPanel(sensor21,
                plotDim,
                new Dimension(5, 4));
		this.plotCart22 = new PlotCartesianPanel(sensor22,
                plotDim,
                new Dimension(5, 4));
		this.plotCart31 = new PlotCartesianPanel(sensor31,
                plotDim,
                new Dimension(5, 4));
		this.plotCart32 = new PlotCartesianPanel(sensor32,
                plotDim,
                new Dimension(5, 4));		
//		this.plotCart = new PlotCartesian(sensor12,
//                new Dimension(900, 500),
//                new Dimension(5, 4));
		
		this.plotPolar = new PlotPolar("Orientation", 
                                          new Dimension((int) plotDim.getHeight(), (int) plotDim.getHeight()),
                                          new Dimension(5, 36),
	                                      new double[]{210.0, 310.0},
                                          80.0);
		
		textPanel = new TextPanel(restPlotDim);
        
//        initMainPerspectiveUI(); // just put everything inside the layout so I can focus on logic here
        
        // String s = new String(ethConn.pullData()); 
        // String v = new String(ethConn.pullData()); 
//        System.out.println(s);
//        System.out.println(v);
//        textPanel.appendText(s);

        // do this kind of stuff at the end to make sure all widgets are initialized
		// this.initMainPerspectiveUI(this.defaultPerspective);        
        defaultPerspective  = this.initMainPerspectiveUI(); // new JPanel();
        
		this.main.addTab("Sensors", this.defaultPerspective);
		this.main.addTab("Imaging", this.imagingPerspective);
		this.main.addTab("Settings", settingsPerspective);		
		
		this.add(this.main, BorderLayout.CENTER);
		this.add(this.statusbar, BorderLayout.SOUTH);		
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setVisible(true);
		

		
		this.setMinimumSize(new Dimension(1024, 768));
		
		this.setTitle(winTitle);
		
		ImageIcon img = new ImageIcon("C:/Users/Petrica Taras/workspace/ground station/src/res/images/iconApp_32x32.png");
		this.setIconImage(img.getImage());	
		
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
    
    private JPanel iniImagingPerspectiveUI() {
    	JPanel container = new JPanel(); 
    	JPanel trackPanel = new JPanel(new BorderLayout());
    	JPanel focusPanel = new JPanel(new BorderLayout());
    	
    	JLabel trackTitle = new JLabel("Tracking Image", SwingConstants.CENTER);
    	JLabel focusTitle = new JLabel("Focusing Image", SwingConstants.CENTER);
    	
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		container.setLayout(layout);
		
		trackPanel.add(trackTitle, BorderLayout.NORTH);
		trackPanel.add(this.imgTrack, BorderLayout.CENTER);
		
		focusPanel.add(focusTitle, BorderLayout.NORTH);
		focusPanel.add(this.imgFocus, BorderLayout.CENTER);
		
		// two columns cells		
		constraints.gridx = 0;
		constraints.gridy = 0; 
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		container.add(trackPanel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0; 
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;
		
		container.add(focusPanel, constraints);		
		
		return container;
    }
    
	private JPanel initMainPerspectiveUI() {
		JPanel container = new JPanel(); 
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		container.setLayout(layout);
		
		// 11
		constraints.gridx = 0;
		constraints.gridy = 0; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(plotCart11, constraints);

		// 12
		constraints.gridx = 1;
		constraints.gridy = 0; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(plotCart12, constraints);

		// 21
		constraints.gridx = 0;
		constraints.gridy = 1; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(plotCart21, constraints);

		// 22
		constraints.gridx = 1;
		constraints.gridy = 1; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(plotCart22, constraints);	
		
		// 31
		constraints.gridx = 0;
		constraints.gridy = 2; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(plotCart31, constraints);

		// 32
		constraints.gridx = 1;
		constraints.gridy = 2; 
		constraints.weightx = 0.01;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(plotCart32, constraints);	

		constraints.gridx = 2;
		constraints.gridy = 0; 
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(this.plotPolar, constraints);
		
		
		// 23 - 33
		constraints.gridx = 2;
		constraints.gridy = 1; 
		constraints.gridheight = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
		
		container.add(this.textPanel, constraints);			
		
//        // second cell		
//		constraints.gridx = 1; 
//		constraints.gridy = 0; 
//		constraints.weightx = 1; // these shall be computed!
//		constraints.weighty = 1;
//		
//		constraints.fill = GridBagConstraints.NONE;
//		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
				                           
//		container.add(plotPolar, constraints);
//		Border bd1 = BorderFactory.createLineBorder(new Color(255, 0, 0));    
//	    plotCart.setBorder(bd1);
//	    plotPolar.setBorder(bd1);
//	    
//	    // image cell
//	    constraints.gridx = 2; 
//		constraints.gridy = 0; 
//		constraints.weightx = 1; // these shall be computed!
//		constraints.weighty = 1;
//		
//		constraints.fill = GridBagConstraints.CENTER;
//		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
//				                           
//		// this.add(this.img, constraints);
//		Border bd3 = BorderFactory.createLineBorder(new Color(255, 0, 0));    
//	    this.img.setBorder(bd3);
//
//	    // third/text cell
//	    constraints.gridx = 0; 
//		constraints.gridy = 1; 
//		constraints.weightx = 1; // these shall be computed!
//		constraints.weighty = 1;
//		constraints.fill = GridBagConstraints.BOTH;
//		constraints.anchor = GridBagConstraints.FIRST_LINE_START;
//		constraints.gridwidth = 2;
//        
//        Border bd2 = BorderFactory.createLineBorder(new Color(255, 0, 0));  
//		//this.add(textPanel, constraints);
//        container.add(this.img, constraints);
//		textPanel.setBorder(bd2);     		

		return container;
	}
	
	// call this only after the menub
    private Dimension getPlotSize() {
    	
    	Dimension result = new Dimension((int) (this.getSize().getWidth()*0.7), 
    			                         (int )(this.getSize().getHeight()*0.3));
    	// this.getSize(); //this.getContentPane().getSize(); // this.menubar.getSize();
        // theFrame.getContentPane().getSize();
    	return result;
    }
}
