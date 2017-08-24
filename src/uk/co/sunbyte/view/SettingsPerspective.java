package uk.co.sunbyte.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class SettingsPerspective extends JPanel {
	private JLabel LeonardoLabel, EtherMegaLabel, localhostLabel;
	
	private JTextField LeonardoIP, EtherMegaIP, localhostIP;
	
	private JPanel Leonardo, EtherMega, localhost;
	
	public SettingsPerspective() {

        this.createLayout();		
	}
	
	private void createLayout() {		
		this.setLayout(new GridLayout(4, 1, 5, 5)); 
		this.add(new JLabel("IP Adresses"));

		LeonardoLabel = new JLabel("Leonardo");
		EtherMegaLabel = new JLabel("EtherMega");	
		localhostLabel = new JLabel("Local IP address (Ethernet port)");
		
		LeonardoIP = new JTextField("169.254.131.159");
		EtherMegaIP = new JTextField("169.254.131.158");
		localhostIP = new JTextField("169.254.131.160");	
		
		Leonardo = new JPanel();
		EtherMega = new JPanel();
	    localhost = new JPanel();	
	    
	    this.add(Leonardo);
		this.add(EtherMega);
		this.add(localhost);
		
	    Leonardo.setLayout(new GridLayout(1, 2, 0, 50));
	    EtherMega.setLayout(new GridLayout(1, 2, 0,50));
	    localhost.setLayout(new GridLayout(1, 2, 0, 50));
	    
	    Leonardo.add(LeonardoLabel);
	    Leonardo.add(LeonardoIP);
	    
	    EtherMega.add(EtherMegaLabel);
	    EtherMega.add(EtherMegaIP);
	    
	    localhost.add(localhostLabel);
	    localhost.add(localhostIP);
	}
}
