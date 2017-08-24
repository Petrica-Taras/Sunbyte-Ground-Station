package uk.co.sunbyte.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Menubar extends JMenuBar {
	
    public Menubar(Window win) {
    	//Build the Sessions menu.
    	JMenu sessions = new JMenu("Sessions");
    	sessions.setMnemonic(KeyEvent.VK_A);
    	sessions.getAccessibleContext().setAccessibleDescription(
    	        "The only menu in this program that has menu items");   
    	
    	JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
    	
        sessions.add(eMenuItem);
    	this.add(sessions);

    	//Build the Performance menu.
    	JMenu performance = new JMenu("Performance");
    	performance.setMnemonic(KeyEvent.VK_B);
    	performance.getAccessibleContext().setAccessibleDescription(
    	        "The only menu in this program that has menu items");   

    	this.add(performance);    	

    	
    	//Build the Settings menu.
    	JMenu settings = new JMenu("Settings");
    	settings.setMnemonic(KeyEvent.VK_B);
    	settings.getAccessibleContext().setAccessibleDescription(
    	        "The only menu in this program that has menu items");   

    	this.add(settings); 
    	JMenuItem ipAddresses = new JMenuItem("IP Adresses");

    	ipAddresses.addActionListener((ActionEvent event) -> {
    		JFrame parent = (JFrame) this.getTopLevelAncestor();
            JFrame jframe = new JFrame();
            jframe.setTitle("IP addresses");
            SettingsPerspective ip = new SettingsPerspective();

            JButton ok = new JButton("OK");
            JButton cancel = new JButton("Cancel");
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(1, 2));
            
            buttons.add(ok);
            buttons.add(cancel);
            
            jframe.setLayout(new GridLayout(2, 1));
            jframe.add(ip);
            jframe.add(buttons);
            jframe.setLocation(800, 400);
            jframe.setVisible(true);
            jframe.pack();
            //jframe.setExtendedState(jframe.NORMAL);
            
            //System.exit(0);
        });
    	settings.add(ipAddresses);
    	
    	//Build the Help menu.
    	JMenu help = new JMenu("Help");
    	help.setMnemonic(KeyEvent.VK_B);
    	help.getAccessibleContext().setAccessibleDescription(
    	        "The only menu in this program that has menu items");   

    	this.add(help);
    	
    	win.setJMenuBar(this);

    }
    
}

