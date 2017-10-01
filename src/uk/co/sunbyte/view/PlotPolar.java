package uk.co.sunbyte.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class PlotPolar extends JPanel{
	private JLabel title;
	
	private PlotPolarDrawableArea drawableArea;

    public PlotPolar(String titleText, Dimension size, Dimension ticks, double[] dataDeadZone, double dataHeading) {
    	this.setBackground(new Color(255, 255, 255));
    	this.setLayout(new GridBagLayout());
    	    	
    	/* fix size of the entire plot widget so we can 
    	then distribute to its subcomponents*/    	
    	this.setPreferredSize(size);
    	this.setMaximumSize(size);
    	this.setMinimumSize(size);
    	   	
    	// text widgets are created here and placed in their cells later!
    	title = new JLabel(titleText);  
    	
    	Dimension dimTitle = title.getPreferredSize();
    	
    	drawableArea = new PlotPolarDrawableArea(size.width-20, size.height-dimTitle.height-30, ticks, dataDeadZone, dataHeading);
    	
    	GridBagConstraints gc = new GridBagConstraints();

        /////////////////// first row ////////////////
        gc.weightx = 1;
        gc.weighty = 0.1; 
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;

        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.CENTER;
    	
    	this.add(title, gc);
        Border bd1 = BorderFactory.createLineBorder(new Color(255, 0, 0));
        
        title.setBorder(bd1);   

        /////////////////// second row ////////////////   
        gc.weightx = 1;
        gc.weighty = 0.1; 
        gc.gridx = 0;
        gc.gridy = 1;  
        gc.gridwidth = 1;
    	
    	Border bd = BorderFactory.createLineBorder(new Color(255, 0, 0));
        drawableArea.setBorder(bd);
        this.add(drawableArea, gc);
        // drawableArea.refresh(new double[][]{{10.0, 55.0}, {200.0, 15.0}, {350.0, 105.0}, {500.0, 600.0}, {600.0, 750.0}});
    	
    	gc.fill = GridBagConstraints.NONE; 
        gc.anchor = GridBagConstraints.CENTER;        
    }
}
