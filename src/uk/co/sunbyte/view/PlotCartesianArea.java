package uk.co.sunbyte.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author Petrica Taras
 * @version 1.1
 *
 ************************************************************************
 * Does represent both data plus axis figure labels (numbers) and legends
 * because trying to align numbers with ticks externally is painful. Can
 * accomodate up to 7 different plots:
 * * blue line
 * * red line 
 * * green line
 * * black line with squares
 * * dashed blue line
 * * dashed red line
 * * dashed green line
 * 
 ************************************************************************
 * The sales task is over and both teams have made massive profits with
 * their sensible ideas.
 * "So well done everyone, unfortunately the format requires that one of 
 * you be fired. Obviously that is a complete departure from what would
 * really happen. I wouldn't be a millionaire if I fired fifteenth of my
 * workforce every week. Just to help me out, does anyone wants to 
 * pointlessly lie or try to take credit for something they didn't do?"
 * " No ... No ... No"
 * "Worth a try ... uhmmm ... okay ... I'll fire the fat one ..... Sorry" 
 */
public class PlotCartesianArea extends JPanel{
	private int[][] locations; // stores points location with respect to upper left corner of the widget 
	private double[][] data;   // stores actual data
	
	private int[] plotReference; 
	private int[] plotSize;
	
	private int plotNumber;
 
	int xTicks;
	int yTicks;
	
	double xInterval;
	double yInterval;
	
	private String[] legend = null; 
	private String[] xlabels = null;
	private String[] ylabels = null;
	
	private Color[] legendColors = null;

	public PlotCartesianArea(Dimension size, Dimension ticks, double[][] data) {
    	this(data);  
    	
		this.setPreferredSize(size);
    	this.setMaximumSize(size);
    	this.setMinimumSize(size);
    	this.setSize(size);	
    	
    	this.xTicks = ticks.width;
    	this.yTicks = ticks.height;
    	
    	this.xInterval = (data[data.length-1][0]-data[0][0])/(this.xTicks-1); 	
    }
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        // g.setColor(Color.BLUE);
        
        // this.double2int(data, this.getWidth(), this.getHeight());

   	    double[] min = getMin(data);
   	    double[] max = getMax(data);        
        
   	    this.yInterval = (max[1]-min[1])/(this.yTicks-1);
   	    
   	    int a = g.getFontMetrics().stringWidth(String.valueOf(min[0]));
   	    int b = g.getFontMetrics().stringWidth(String.valueOf(max[0]));
   	    
   	    int xLabelSize = (a>b)?a:b;

   	    int c = g.getFontMetrics().stringWidth(String.valueOf(min[1]));
   	    int d = g.getFontMetrics().stringWidth(String.valueOf(max[1]));
   	      	    
   	    int e = g.getFontMetrics().getHeight();
   	    
   	    int yLabelSize = (c>d)?c:d;
   	    
   	    this.plotReference[0] = yLabelSize + 20;
   	    this.plotReference[1] = e/2 + 5;
   	    
   	    this.plotSize[0] = this.getWidth() - this.plotReference[0]-yLabelSize/2;
   	    this.plotSize[1] = this.getHeight() - this.plotReference[1] - e - 10;
   	    
   	    g.drawRect(this.plotReference[0], this.plotReference[1], 
   	    		   this.plotSize[0], this.plotSize[1]);
   	    
    	this.double2int(this.plotSize[0], 
    			        this.plotSize[1]);   	  
    	
    	// draw xlabels
    	
    	int[] minMaxX = new int[] {this.plotReference[0] - xLabelSize/2, 
    			                  this.plotReference[0] + this.plotSize[0] - xLabelSize/2};
        int[] minMaxY = new int[] {this.plotReference[1] - yLabelSize/2, 
                                   this.plotReference[1] + this.plotSize[1] + e/2 - 3};
    	
        
        
    	for(int i = 0; i < this.xTicks; i++) {
    		g.drawString(String.valueOf(data[0][0]+this.xInterval*i), 
    				     minMaxX[0]+this.plotSize[0]/(this.xTicks-1)*i, 
    				     this.plotReference[1] + this.plotSize[1] + e + 2);
    	}
    	
    	for(int i = 0; i < this.yTicks; i++) {
    		String s = String.valueOf(min[1]+this.yInterval*i);
    		g.drawString(s, 
    				     this.plotReference[0] - 5 - g.getFontMetrics().stringWidth(s),
    				     minMaxY[1]-this.plotSize[1]/(this.yTicks-1)*i);
    	}
   	    
        for(int[] i: this.locations) {
    		for(int col = 1; col < this.locations[0].length; col++) { 
    			g.setColor(Color.BLACK);
    			int xLabelPosition = g.getFontMetrics().stringWidth(String.valueOf(i[0]))/2;
    			g.setColor(this.legendColors[col-1]);
      	        g.fillRect(i[0]+this.plotReference[0]-2, 
        	        		   i[col]+this.plotReference[1]-2, 
        	        		   5, 5);
 
            }
        }
        
        // another loop to add the plot line:
        // this is the code to be generalised, in this method!
        for(int i=1; i < this.locations.length; i++) {
        	for(int j=1; j < this.locations[0].length; j++) {
        	    g.drawLine(this.locations[i-1][0]+this.plotReference[0], this.locations[i-1][j]+this.plotReference[1], 
      			           this.locations[i][0]+this.plotReference[0], this.locations[i][j]+this.plotReference[1]);   
        	}
        	
        }
        
        // going for ticks, X axis
        for(int i = 1; i < this.xTicks-1; i++) {
        	g.drawLine(i*this.getWidth()/(this.xTicks-1), 
        			   this.getHeight(), 
        			   i*this.getWidth()/(this.xTicks-1), 
        			   this.getHeight()-8);
        }
        
        // going for ticks, Y axis
        for(int i = 1; i < this.yTicks-1; i++) {
        	g.drawLine(0, 
        			   i*this.getHeight()/(this.yTicks-1), 
        			   8, 
        			   i*this.getHeight()/(this.yTicks-1));
        }        
	}

	/*
	 * A basic constructor, without imposing area size
	 * */
	public PlotCartesianArea(double[][] data) {
    	this.setBackground(new Color(255, 255, 255));    
    	this.plotNumber = data[0].length - 1; // number of plots in one figure
    	this.data = data;
    	this.locations = new int[data.length][data[0].length];
    	
    	this.plotReference = new int[]{0, 0};
    	this.plotSize = new int[] {this.getWidth(), this.getHeight()};
    	
    	this.legendColors = new Color[] {Color.BLUE, Color.RED, Color.GREEN, Color.BLUE, Color.RED, Color.GREEN, Color.BLACK};
    }
	
	
     /*
      * should only keep at most max(width, height) points
      */        
     private void double2int(double[][] data, int width, int height) { 
    	 double[] min = getMin(data);
    	 double[] max = getMax(data);
    	 
    	 double minX = min[0];
    	 double maxX = max[0];
    	 
    	 double minY = min[1];
    	 double maxY = max[1];
    	 
    	 double diffX = maxX - minX;
    	 double diffY = maxY - minY;

    	 for(int i = 0; i < data.length; i++) {
    		 // deal with x axis
    		 this.locations[i][0] = (int) (((data[i][0]-minX)/diffX)*width);    	
    		 // then y axis
    		 for(int j = 1; j < data[0].length; j++) {
    		     this.locations[i][j] = height-(int) (((data[i][j]-minY)/diffY)*height);
    	     }    		 
    	 }
    }

     private void double2int(int width, int height) { 
    	 double[] min = getMin(data);
    	 double[] max = getMax(data);
    	 
    	 double minX = min[0];
    	 double maxX = max[0];
    	 
//    	 for(int i = 0; i < min.data; i++) {
    	 double minY = min[1];
    	 double maxY = max[1];
//    	 }
    	 
    	 double diffX = maxX - minX;
    	 
    	 double diffY = maxY - minY;
    	 
    	 for(int i = 0; i < data.length; i++) {
    		 // deal with x axis
    		 this.locations[i][0] = (int) (((data[i][0]-minX)/diffX)*width);    	
    		 // then y axis
    		 for(int j = 1; j < data[0].length; j++) {
    		     this.locations[i][j] = height-(int) (((data[i][j]-minY)/diffY)*height);
    	     }
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
	
	private double[] getMax(double[][] v) {
		// result - first is the double of time
		// result - second concerns the y axis (all available plots)
		
		double xm = v[0][0];
		double ym = v[0][1];
		
		for(int i = 0; i < v.length; i++) {
			if(v[i][0] > xm) {
				xm = v[i][0];
			}
			for(int j = 1; j < v[0].length; j++) {
				if(v[i][j] > ym) {
					ym = v[i][j];
				}
			}
		}	
		return new double[]{xm, ym};
	}
	
	private double getMin(double[] v) {
		double m = v[0];
		
		for(double f: v) {
			if(f < m)
				m = f; 
		}
		
		return m;
	}
	
	private double[] getMin(double[][] v) {
		// result - first is the double of time
		// result - second concerns the y axis (all available plots)
		
		double xm = v[0][0];
		double ym = v[0][1];
		
		for(int i = 0; i < v.length; i++) {
			if(v[i][0] < xm) {
				xm = v[i][0];
			}
			for(int j = 1; j < v[0].length; j++) {
				if(v[i][j] < ym) {
					ym = v[i][j];
				}
			}
		}
		
		return new double[]{xm, ym};
	}
	
    private void addXTicks(Graphics g, int n) {
    	if (n <= 1 ) return; 
    	// get intervals (results n-1 intervals)
    	int intervalX = this.getWidth()/(n-1);
    	
    }
    
    private void addYTicks(Graphics g, int n) {
    }    
    
    private void addGrid(Graphics g) {
    	
    }
    
    public void refresh(double[][] data) {
    	this.double2int(data, this.getWidth(), this.getHeight());
    	this.repaint();
    }
}
