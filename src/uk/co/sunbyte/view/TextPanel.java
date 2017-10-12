package uk.co.sunbyte.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import uk.co.sunbyte.controller.ConnectionListener;

public class TextPanel extends JPanel implements ConnectionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6978478625611614861L;
	private JTextArea textArea; 
    
    public TextPanel(Dimension d) {
    	this.textArea = new JTextArea(); 
        setLayout(new BorderLayout()); // setting the layout not on the JFrame but on the JPanel
        add(new JScrollPane(textArea), BorderLayout.CENTER); // not adding anything else, means that it will fill the entire area available
        textArea.setSize(d);
        this.setSize(d);
    }
    
    public void appendText(String text) {
        textArea.append(text);
    }

	@Override
	public synchronized void notifyDestination(String text) {
		this.appendText(text);
		
	}

    
}
