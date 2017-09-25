package uk.co.sunbyte.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextPanel extends JPanel {
    private JTextArea textArea; 
    
    public TextPanel() {
    	this.textArea = new JTextArea(); 
        setLayout(new BorderLayout()); // setting the layout not on the JFrame but on the JPanel
        add(new JScrollPane(textArea), BorderLayout.CENTER); // not adding anything else, means that it will fill the entire area available     	
    }
    
    public void appendText(String text) {
        textArea.append(text);
    }

    
}
