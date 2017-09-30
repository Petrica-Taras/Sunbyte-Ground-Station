package uk.co.sunbyte.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import uk.co.sunbyte.model.Sensor;

/**
 * @author Petrica Taras
 *
 * TODO: make sure the drawableArea size is not gonna be bigger
 * than available space (widget will not work correctly)
 * 
 * Strategy - align the xLabels/yLabels using carefully computed weights
 * add validation (i.e. what happens when no data is passed on)
 * add possibility of keeping the old data and just append and refresh the newest one!
 * 
 */
public class PlotCartesian extends JPanel{
	// private String xLabelText;
	// private String yLabelText;
	// private String titleText;
	
	// JLabel yLabel;
	private JLabel[] xTicks;
	private JLabel[] yTicks;

	private JLabel title; 
	private JLabel xLabel;
	
	private PlotCartesianDrawableArea drawableArea; 
	
	private int xLength;
	private int yLength;
	
	int[] drawableAreaSizes; // wtf is this?
	
	public PlotCartesian(Sensor sensor, Dimension size, Dimension ticks) {
		this(sensor.getAbcissae(), sensor.getName(), size, ticks, PlotCartesian.sensorToDouble(sensor)); // ugly fix
	}
	
	
    public PlotCartesian(String xLabelText, String titleText, Dimension size, Dimension ticks, double[][] data) {
    	this.setBackground(new Color(255, 255, 255));
    	this.setLayout(new GridBagLayout());
    	
    	this.xLength = data.length;
    	this.yLength = data[0].length; // validation should happen here 
    	
    	/* fix size of the entire plot widget so we can 
    	then distribute to its subcomponents*/    	
    	this.setPreferredSize(size);
    	this.setMaximumSize(size);
    	this.setMinimumSize(size);
    	   	
    	// text widgets are created here and placed in their cells later!
    	title = new JLabel(titleText);   	
    	
    	this.xTicks = new JLabel[ticks.width]; // this size constant should be propagated
    	this.yTicks = new JLabel[ticks.height];
    	
    	xLabel = new JLabel(xLabelText);

    	double xInterval = (data[this.xLength-1][0]-data[0][0])/(ticks.width-1);
    	
        for(int i = 0; i < ticks.width; i++) {
    	    xTicks[i] = new JLabel(Double.toString(data[0][0]+xInterval*i));
        	// xTicks[i].setText(Double.toString(data[i][0]));
            // System.out.println(Double.toString(data[i][0]));
        }    	
    	
    	// end text widget creation
        
        /* Calculate sizes for title and xLabel widgets
         * so we can fix the drawableArea height, based
         * on available space
         */
    	Dimension dimTitle = title.getPreferredSize();  
    	Dimension dimXTicks = xTicks[0].getPreferredSize(); // only interested in height now 
    	Dimension dimXLabel = xLabel.getPreferredSize();
    	
        drawableArea = new PlotCartesianDrawableArea(size.width-20, size.height-dimTitle.height-dimXTicks.height-dimXLabel.height-30, ticks, data);
    	
    	GridBagConstraints gc = new GridBagConstraints();

        /////////////////// first row ////////////////
        gc.weightx = 1;
        gc.weighty = 0.1; 
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 5;

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
        gc.gridwidth = 5;
    	
    	Border bd = BorderFactory.createLineBorder(new Color(255, 0, 0));
        drawableArea.setBorder(bd);
        this.add(drawableArea, gc);
        // drawableArea.refresh(new double[][]{{10.0, 55.0}, {200.0, 15.0}, {350.0, 105.0}, {500.0, 600.0}, {600.0, 750.0}});
    	
    	gc.fill = GridBagConstraints.NONE; 
        gc.anchor = GridBagConstraints.CENTER;
               
        /////////////////// third row ////////////////
        gc.weightx = 0.1;
        gc.weighty = 0.2; 
        gc.gridy = 2;
        gc.gridwidth = 1;
        
        Border[] bx = new Border[ticks.width];
        for(int i = 0; i < ticks.width; i++) {
        	bx[i] = BorderFactory.createLineBorder(new Color(255, 0, 0));
        }
        
        gc.fill = GridBagConstraints.NONE;
        
        for(int i = 0; i < ticks.width; i++) {
        	gc.gridx = i; 
        	
            if(i == (ticks.width-1)) {
            	gc.weightx = 0.1;
            	gc.anchor = GridBagConstraints.EAST; 
            } else if(i == 0) {
                gc.anchor = GridBagConstraints.WEST;
                gc.weightx = 0.1;
            } else {
            	gc.anchor = GridBagConstraints.CENTER;
            	gc.weightx = 0.2;
            }
            
            this.add(xTicks[i], gc);
            xTicks[i].setBorder(bx[i]);
            // gc.anchor = GridBagConstraints.CENTER;
        }

        /////////////////// fourth row ////////////////
        gc.weightx = 0.1;
        gc.weighty = 0.2; 
        
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 5;        
         
        gc.fill = GridBagConstraints.NONE;  
        gc.anchor = GridBagConstraints.CENTER;
        
        this.add(this.xLabel, gc);
    }
    
    /** very ugly fix, should not be static, it is only static because the 
     *  constructor using a Sensor object as an argument was chained with
     *  the one using a double[][] array!
     */
    private static double[][] sensorToDouble(Sensor sensor) {
    	int m = sensor.getColumnSize(); // columns 
		int n = sensor.getFieldsSize(); // rows
		
		double[][] data = new double[n][m]; 
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < m; j++) {
				data[i][j] = sensor.getFloatData().get(i)[j]; 
			}
		}
		
		return data; 
    }
}
