package uk.co.sunbyte.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlotCartesianDrawableArea extends JPanel{
	private int[][] locations; 
	int width;
	int height; 
	int xTicks;
	int yTicks;

	public PlotCartesianDrawableArea(int width, int height, Dimension ticks, double[][] data) {
		// this.setSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
    	this.setBackground(new Color(255, 255, 255));

    	this.width = width;
    	this.height = height; 
    	
    	this.xTicks = ticks.width;
    	this.yTicks = ticks.height;
    	
    	this.double2int(data);
    	
//    	this.addXTicks(5); 
//    	this.addYTicks(4);
    }
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.setColor(Color.BLUE);
        
        for(int[] i: this.locations) {
        	if(i[0] == 0) {
        		if(i[1] == 0) {
        		    g.fillRect(1, 1, 3, 3);
        		} else if (i[1] == this.height) {
        			g.fillRect(1, this.height-4, 3, 3);
        		}
        		else {
        			g.fillRect(1, i[1], 3, 5);
        		}
        	} else if(i[0] == this.width) {
        		if(i[1] == 0) {
        		    g.fillRect(this.width-4, 1, 3, 3);
        		} else if (i[1] == this.height) {
        			g.fillRect(this.width-4, this.height-4, 3, 3);
        		}
        		else {
        			g.fillRect(this.width-4, i[1], 3, 5);
        		}
        	}
        	else {	
        	    g.fillRect(i[0], i[1], 5, 5);
        	}
        }
        
        // another loop to add the plot line:
        // this is the code to be generalized, in this method!
        for(int i=1; i < this.locations.length; i++) {
        	if(i == 1) {
        	    g.drawLine(this.locations[i-1][0], this.locations[i-1][1], 
        			       this.locations[i][0]+2, this.locations[i][1]+2);
        	} else if (i == (this.locations.length-1)) {
        	    g.drawLine(this.locations[i-1][0]+2, this.locations[i-1][1]+2, 
     			       this.locations[i][0], this.locations[i][1]);        		
        	} else {
        	    g.drawLine(this.locations[i-1][0]+2, this.locations[i-1][1]+2, 
      			       this.locations[i][0]+2, this.locations[i][1]+2);   
        	}
        	
        }
        
        // going for ticks, X axis
        for(int i = 1; i < this.xTicks-1; i++) {
        	g.drawLine(i*this.width/(this.xTicks-1), 
        			   height, 
        			   i*this.width/(this.xTicks-1), 
        			   height-8);
        }
        
        // going for ticks, Y axis
        for(int i = 1; i < this.yTicks-1; i++) {
        	g.drawLine(0, 
        			   i*this.height/(this.yTicks-1), 
        			   8, 
        			   i*this.height/(this.yTicks-1));
        }        
	}

	/*
	 * A second constructor, without imposing area size
	 * For testing for now
	 * */
	public PlotCartesianDrawableArea(double[][] data) {
		// this.setSize(new Dimension(width, height));
		Dimension localDim = this.getPreferredSize();

//		System.out.println(localDim.width);
//		System.out.println(localDim.height);
		
		// this.setPreferredSize(new Dimension(width, height));
    	this.setBackground(new Color(255, 255, 255));

    	this.width = localDim.width;
    	this.height = localDim.height;    	
    	
    	this.double2int(data);
    	
//    	this.addXTicks(5); 
//    	this.addYTicks(4);
    }
	
	
     /*
      * should only keep at most max(width, height) points
      */        
     private void double2int(double[][] data) {
    	 double[] x = new double[data.length];
    	 double[] y = new double[data.length];
    	 
    	 // populate x, y
    	 for(int i = 0; i < data.length; i++) {
    		 x[i] = data[i][0];
    		 y[i] = data[i][1];
    	 }
    	 
    	 double minX = getMin(x);
    	 double maxX = getMax(x);
    	 
    	 double minY = getMin(y);
    	 double maxY = getMax(y);
    	 
    	 double diffX = maxX - minX;
    	 double diffY = maxY - minY;
    	 
    	 int width = this.width; 
    	 int height = this.height;
    	 
    	 this.locations = new int[data.length][2];
    	 
    	 for(int i = 0; i < data.length; i++) {
    		 this.locations[i][0] = (int) (((x[i]-minX)/diffX)*width);
    		 this.locations[i][1] = height-(int) (((y[i]-minY)/diffY)*height);
//    		 System.out.println(this.locations[i][0]);
//    		 System.out.println(this.locations[i][1]);
//    		 System.out.println("*******************");
    	 }
    
        
    } 
	
	private double getMax(double[] v) {
		double m = v[0];
		
		for(double f: v) {
			if(f > m)
				m = f; 
		}
		
		return m;
	}
	
	private double getMin(double[] v) {
		double m = v[0];
		
		for(double f: v) {
			if(f < m)
				m = f; 
		}
		
		return m;
	}

    private void addXTicks(Graphics g, int n) {
    	if (n <= 1 ) return; 
    	// get intervals (results n-1 intervals)
    	int intervalX = this.width/(n-1);
    	
    }
    
    private void addYTicks(Graphics g, int n) {
    }    
    
    private void addGrid(Graphics g) {
    	
    }
    
    public void refresh(double[][] data) {
    	this.double2int(data);
    	this.repaint();
    }
}
