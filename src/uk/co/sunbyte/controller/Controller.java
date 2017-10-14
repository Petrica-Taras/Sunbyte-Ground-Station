package uk.co.sunbyte.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import uk.co.sunbyte.model.EthernetConnection;
import uk.co.sunbyte.model.Sensor;
import uk.co.sunbyte.model.SensorConnectionManagement;
import uk.co.sunbyte.model.Session;
import uk.co.sunbyte.view.ImagePanel;
import uk.co.sunbyte.view.Menubar;
import uk.co.sunbyte.view.PlotCartesianPanel;
import uk.co.sunbyte.view.PlotPolar;
import uk.co.sunbyte.view.StatusBar;
import uk.co.sunbyte.view.TextPanel;

@SuppressWarnings("serial")
public class Controller extends JFrame {
	// JButton LeoDiagnose;
	// JButton EthMDiagnose;
	// JButton Vibes;
	// JPanel bringTogether;
	// JTextArea InfoSensors;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	double width = screenSize.getWidth();
	double height = screenSize.getHeight();

	private String winTitle = "Sunbyte Project: ";
	public Menubar menubar;

	private GridBagLayout layout;
	private GridBagConstraints constraints;

	public StatusBar statusbar;

	public PlotCartesianPanel plotChannel_1;
	public PlotCartesianPanel plotChannel_2;
	public PlotCartesianPanel plotChannel_3;
	public PlotCartesianPanel plotChannel_4;
	public PlotCartesianPanel plotChannel_5;
	public PlotCartesianPanel plotChannel_6;

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

	Session session; // should have a persistence object as a member - this should remember the
						// widget sizes

	EthernetConnection ethConn;

	/**
	 * 
	 * Important for layout management and keeping track of various widgets
	 * dimensions If widget border resizing is to be implemented later on, this is
	 * crucial. Also, it should be populated as soon as components are added.
	 * 
	 */
	private Dimension[] layoutPartitions;

	public Map<String, String> settings; // centralises all of the settings

	private Sensor dataChannel_1;
	private Sensor dataChannel_2;
	private Sensor dataChannel_3;
	private Sensor dataChannel_4;
	private Sensor dataChannel_5;
	private Sensor dataChannel_6;
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
		this.imgTrack = new ImagePanel(session.getLocalPathFolder() + "\\test.png");
		this.imgFocus = new ImagePanel(session.getLocalPathFolder() + "\\hot_air_balloon_kidnapping.png");

		imagingPerspective = this.iniImagingPerspectiveUI();
		settingsPerspective = new JPanel();

		mainBL = new BorderLayout();
		this.setLayout(this.mainBL);

		this.initDataChannels();
		
		this.session.writeSensorData(dataChannel_1);
		this.session.writeSensorData(dataChannel_2);

		// start the dancing
		this.settings = new HashMap<String, String>();
		this.settings.put(session.getAppPref().getGroundStationName(), session.getAppPref().getGroundStationIP());
		this.settings.put("Leonardo IP", "169.254.131.159");
		this.settings.put("EtherMega IP", "169.254.131.158");

		// ethConn = new EthernetConnection("172.16.18.131", 9999);

		ethConn = new EthernetConnection();

		// while(!exit) {
		/*
		 * ethConn.sendCommandToClient("172.16.18.131", 9999, "20011", "20021");
		 */

		Dimension plotDim = new Dimension((int) (this.width * 0.35), (int) ((this.height - 28) * 0.3));
		Dimension restPlotDim = new Dimension((int) (this.width * 0.30), (int) (this.height - 28));
		this.plotChannel_1 = new PlotCartesianPanel(dataChannel_1, plotDim, new Dimension(5, 4));
		this.plotChannel_2 = new PlotCartesianPanel(dataChannel_2, plotDim, new Dimension(5, 4));
		this.plotChannel_3 = new PlotCartesianPanel(dataChannel_3, plotDim, new Dimension(5, 4));
		this.plotChannel_4 = new PlotCartesianPanel(dataChannel_4, plotDim, new Dimension(5, 4));
		this.plotChannel_5 = new PlotCartesianPanel(dataChannel_5, plotDim, new Dimension(5, 4));
		this.plotChannel_6 = new PlotCartesianPanel(dataChannel_6, plotDim, new Dimension(5, 4));
		
		this.plotChannel_1.notifyDestination("1000.0 120.0 32.1 19.5 2.0");

		this.plotPolar = new PlotPolar("Orientation",
				new Dimension((int) plotDim.getHeight(), (int) plotDim.getHeight()), new Dimension(5, 36),
				new double[] { 210.0, 310.0 }, 80.0);

		textPanel = new TextPanel(restPlotDim);

		final List<SensorConnectionManagement> devices = new ArrayList<>();
		SensorConnectionManagement scm = new SensorConnectionManagement("172.16.18.131", 9999);
		scm.addListener(textPanel);
		scm.addListener(this.plotChannel_1);
		
		devices.add(scm);
		devices.add(new SensorConnectionManagement("172.16.18.132", 9999));
		
		Timer scheduler = new Timer();
		scheduler.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					List<Thread> threadsForAllDevices = new ArrayList<>();
					// It is time to begin a new round of status check.	
					for (final SensorConnectionManagement device : devices) {
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								//Do work here related to connecting, whatever
								device.connect();
								plotChannel_1 = new PlotCartesianPanel(new Sensor("Plot 11",  new String[] { "Time", "CPU", "CPU1" }, "80.0 20.0 32.1\n95.0 22.0 89.0\n101.0 22.1 90.0\n200.0 40.0 99.1\n350.0 75.0 45.0\n500.0 100.0 33.0\n750 215 120.09"), plotDim, new Dimension(5, 4));
								System.out.println("Thread \"" + Thread.currentThread().getName() + "\" is doing work!");
							}
						});
						t.setName("THREAD FOR " + device.getIp() + ":" + device.getPort());
						t.setDaemon(true);
						threadsForAllDevices.add(t);
						
						t.start();
					}
					
					for (Thread startedThread : threadsForAllDevices) {
						try {
							// Tells the owning thread (in our case, the thread started by
							// our timer instance) to wait until startedThread finishes execution.
							startedThread.join();
						} catch (InterruptedException ex) {
							throw new RuntimeException("An error occurred while joining threads.", ex);
						}
					}
					
					//write here whatever needs to happen when all devices
					//have been checked (you ONLY get here once all threads finish)
				}
			}, 
		0, 5000);
		
//		SensorConnectionManagement scm = new SensorConnectionManagement("va merge", new StringBuffer(), "20011",
//				"20021");
//
//		SensorConnectionManagement scm1 = new SensorConnectionManagement("si asta va merge", new StringBuffer(),
//				"20011", "20021");
//
//		scm.addListener(this.textPanel);
//		scm1.addListener(this.textPanel);
//
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Timer timer = new Timer("Timer");
//					// Thread t1 = new Thread(new Runnable() {
//					timer.scheduleAtFixedRate(scm, 1000L, 1000L);
//					timer.scheduleAtFixedRate(scm1, 1000L, 1000L);
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//		});
//
//		t.setName("Waits for image reception");
//		t.setDaemon(true);
//		t.setPriority(Thread.NORM_PRIORITY);
//		t.start();

		// textPanel.appendText(s);

		// do this kind of stuff at the end to make sure all widgets are initialized
		defaultPerspective = this.initMainPerspectiveUI(); // new JPanel();

		this.main.addTab("Sensors", this.defaultPerspective);
		this.main.addTab("Imaging", this.imagingPerspective);
		this.main.addTab("Settings", settingsPerspective);

		this.add(this.main, BorderLayout.CENTER);
		this.add(this.statusbar, BorderLayout.SOUTH);

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);

		this.setMinimumSize(new Dimension(1024, 768));

		this.setTitle(winTitle);

		ImageIcon img = new ImageIcon(
				"C:/Users/Petrica Taras/workspace/ground station/src/res/images/iconApp_32x32.png");
		this.setIconImage(img.getImage());

		// fast stuff okay!
		// bringTogether = new JPanel();
		//
		// LeoDiagnose = new JButton("Test Leonardo");
		// EthMDiagnose = new JButton("Test EtherMega");
		// Vibes = new JButton("get vibrations");
		// InfoSensors = new JTextArea();
		//
		//
		//
		//
		// this.bringTogether.setLayout(new FlowLayout());
		//
		// EthernetConnection ethConnEtheMega = new
		// EthernetConnection("169.254.131.158", 9999);
		// EthernetConnection ethConnLeonardo = new
		// EthernetConnection("169.254.131.159", 9990);
		//
		// /*ethConn.pullData(); /*cannot overload pullData() with port number,
		// cause we will lose generality for SDD;
		// maybe specify it in the constructor!*/
		//// while(true) {
		//// ethConnEtheMega.pushData("d");}
		//
		// LeoDiagnose.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// ethConnLeonardo.pushData("d");
		// }
		// });
		//
		// EthMDiagnose.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// ethConnEtheMega.pushData("d");
		// }
		// });
		//
		// Vibes.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// try {
		// ethConnLeonardo.pullData();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// //ethConnLeonardo.pushData("f");
		//
		// }
		// });

	}

	private void initDataChannels() {
		this.dataChannel_1 = new Sensor("Temperature", 
				             new String[] { "Time", 
				            		        "Outside", 
				            		        "Box",
				            		        "Azimuth M.", 
				            		        "PC"});
		
		String s1 = new String("80.0 20.0 32.1 19.5 2.0\n95.0 22.0 89.0 22.5 1.02\n101.0 22.1 90.0 33.0 6.9\n200.0 40.0 99.1 66.2 11.2\n350.0 75.0 45.0 69.02 8.91\n500.0 100.0 33.0 72.0 12\n750 215 120.09 100.2 38"); 
		this.dataChannel_1.addStringBatch(s1);
		this.dataChannel_2 = new Sensor("Plot 12", new String[] { "Time", "CPU" },
				"40.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");

		this.dataChannel_3 = new Sensor("Plot 21", new String[] { "Time", "CPU" },
				"80.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");
		this.dataChannel_4 = new Sensor("Plot 22", new String[] { "Time", "CPU" },
				"40.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");

		this.dataChannel_5 = new Sensor("Plot 31", new String[] { "Time", "CPU" },
				"80.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");
		this.dataChannel_6 = new Sensor("Plot 32", new String[] { "Time", "CPU" },
				"40.0 20.0\n95.0 22.0\n101.0 22.1\n200.0 40.0\n350.0 75.0\n500.0 100.0\n750 215");		
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

		container.add(plotChannel_1, constraints);

		// 12
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 0.01;
		constraints.weighty = 1;

		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		container.add(plotChannel_2, constraints);

		// 21
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0.01;
		constraints.weighty = 1;

		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		container.add(plotChannel_3, constraints);

		// 22
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 0.01;
		constraints.weighty = 1;

		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		container.add(plotChannel_4, constraints);

		// 31
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weightx = 0.01;
		constraints.weighty = 1;

		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		container.add(plotChannel_5, constraints);

		// 32
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.weightx = 0.01;
		constraints.weighty = 1;

		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		container.add(plotChannel_6, constraints);

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

		return container;
	}

	// call this only after the menub
	private Dimension getPlotSize() {

		Dimension result = new Dimension((int) (this.getSize().getWidth() * 0.7),
				(int) (this.getSize().getHeight() * 0.3));
		// this.getSize(); //this.getContentPane().getSize(); // this.menubar.getSize();
		// theFrame.getContentPane().getSize();
		return result;
	}
}
