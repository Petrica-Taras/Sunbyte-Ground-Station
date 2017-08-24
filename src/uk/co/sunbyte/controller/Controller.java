package uk.co.sunbyte.controller;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import uk.co.sunbyte.model.EthernetConnection;
import uk.co.sunbyte.view.PlotCartesian;
import uk.co.sunbyte.view.Window;


public class Controller {
	JButton LeoDiagnose;
	JButton EthMDiagnose; 
	JButton Vibes;
	JPanel bringTogether;
	JTextArea InfoSensors; 
	
    public Controller() {
    	// start the dancing
    	Window MainWin = new Window(); 
    	
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
}
