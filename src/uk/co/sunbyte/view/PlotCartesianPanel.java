package uk.co.sunbyte.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import uk.co.sunbyte.controller.ConnectionListener;
import uk.co.sunbyte.model.Sensor;

/**
 * @author Petrica Taras
 * @version 1.0
 *
 **************************************************************************************
 * TODO: make sure the drawableArea size is not gonna be bigger
 * than available space (widget will not work correctly)
 * 
 * Strategy - align the xLabels/yLabels using carefully computed weights
 * add validation (i.e. what happens when no data is passed on)
 * add possibility of keeping the old data and just append and refresh the newest one!
 *
 * *************************************************************************************
 * "When can we have a policy meeting?"
 * "We've taken the liberty of drawing up a list of priorities"
 * "Oh yeah?"
 * "Yeah ... so ... here is general Eisenhower's telephone number, here is the English 
 * for we give up and here is an analysis of our military situation in one rude word"  
 * *************************************************************************************
 * 
 */
@SuppressWarnings("serial")
public class PlotCartesianPanel extends JPanel implements ConnectionListener {	
	private JLabel[] xTicks;
	private JLabel[] yTicks;
	private String[] legend; 

	private JLabel title; 
	private JLabel xLabel;
	
	private PlotCartesianArea drawableArea; 
	
	private int xLength;
	private int yLength;
	
	int[] drawableAreaSizes; // wtf is this?
	
	Dimension size;
	Dimension ticks;
	Dimension dimTitle, dimXLabel; 
	
	double[][] data;
	
	public PlotCartesianPanel(Sensor sensor, Dimension size, Dimension ticks) {
		this(sensor.getAbcissae(), sensor.getName(), size, ticks, PlotCartesianPanel.sensorToDouble(sensor)); // ugly fix
	}
	
	
    public PlotCartesianPanel(String xLabelText, String titleText, Dimension size, Dimension ticks, double[][] data) {
    	this.data = data.clone(); 
    	this.size = size; 
    	this.ticks = ticks;
    	this.setBackground(new Color(255, 255, 255));
    	this.setLayout(new BorderLayout());
    	
    	this.xLength = data.length;
    	this.yLength = data[0].length; // validation should happen here 

    	this.legend = new String[this.yLength - 1]; // get rid of 1 column which is TIME    	
    	
    	/* fix size of the entire plot widget so we can 
    	then distribute to its subcomponents*/    	
    	this.setPreferredSize(size);
    	this.setMaximumSize(size);
    	this.setMinimumSize(size);
    	   	
    	// text widgets are created here and placed in their cells later!
    	title = new JLabel(titleText, SwingConstants.CENTER);   	
    	xLabel = new JLabel(xLabelText, SwingConstants.CENTER);
    	// end text widget creation
        
        /* Calculate sizes for title, xLabel and yTicks widgets
         * so we can set the drawableArea height, based
         * on available space
         */
    	this.dimTitle = title.getPreferredSize();  
    	this.dimXLabel = xLabel.getPreferredSize();
    	
    	drawableArea = new PlotCartesianArea(new Dimension(size.width, 
                                             size.height-dimTitle.height-dimXLabel.height-30), 
    			                             ticks, 
    			                             data);
    	  	
    	this.add(title, BorderLayout.PAGE_START);
        this.add(drawableArea, BorderLayout.CENTER);
        this.add(xLabel, BorderLayout.SOUTH);
    	
    	Border bd = BorderFactory.createLineBorder(new Color(255, 0, 0));
        drawableArea.setBorder(bd);
        
    }
    
    /** very ugly fix, should not be static, it is only static because the 
     *  constructor using a Sensor object as an argument was chained with
     *  the one using a double[][] array!
     */
    private static double[][] sensorToDouble(Sensor sensor) {
    	int m = sensor.getColumnSize(); // columns 
		int n = sensor.getFieldsSize(); // rows
		
		// System.out.println("data size is " + sensor.getFloatData().get(0).length);
		
		double[][] data = new double[n][m]; 
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				data[i][j] = sensor.getFloatData().get(i)[j]; 
			}
		}
		
		return data; 
    }


	@Override
	public void notifyDestination(String text) {
		String[] line = text.split(" ");		
		double[] oneLineDouble = new double[line.length];

    	for(int i = 0; i < line.length; i++) {
    		oneLineDouble[i] = Double.parseDouble(line[i]);
    	}    	
    	
    	double[][] local = new double[this.data.length+1][this.data[0].length]; 
    	for(int i = 0; i < this.data.length; i++) {
    		for(int j = 0; j < this.data[0].length; j++) {
    			local[i][j] = this.data[i][j];
    		}
    	}
    	for(int i = 0; i < this.data[0].length; i++) {
    	    local[this.data.length][i] = oneLineDouble[i];
    	}
  
    	
//		this.drawableArea = new PlotCartesianArea(new Dimension(size.width, 
//                                             size.height-dimTitle.height-dimXLabel.height-30), 
//    			                             ticks, 
//    			                             local);
		this.drawableArea.refresh(local);
		
	}
}
