package uk.co.sunbyte.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class PlotPolarDrawableArea extends JPanel{
	private int[][] locations; // we assume data contains only angles now
	int width;
	int height; 
	int xTicks;
	int yTicks;	
	
	public PlotPolarDrawableArea(int width, int height, Dimension ticks, double[][] data) {
		this.setPreferredSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		
		this.width = width;
		this.height = height;
		
		this.xTicks = ticks.width;
		this.yTicks = ticks.height;
		
    	this.setBackground(new Color(255, 255, 255));		
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.setColor(Color.BLUE);
        
        Rectangle2D fontHW = g.getFontMetrics().getStringBounds("N", g);
        int h = (int) fontHW.getHeight();
        int w = (int) fontHW.getWidth();
        
        g.drawString("N", this.width/2-w/2, h);
        g.drawString("S", this.width/2-w/2, this.height-1);
        g.drawString("W", 1, this.height/2+h/2);
        g.drawString("E", this.width-h, this.height/2+h/2);
        
        int pixelRadius = this.height/2-5-h;
        
        g.drawArc(this.width/2-pixelRadius, this.height/2-pixelRadius, 
        		  pixelRadius*2, pixelRadius*2,
        		  0, 360);
                
        // deadzone delimitation
        g.setColor(Color.RED);
//        System.out.println(h);
//        System.out.println(this.width);
//        System.out.println(this.height);
//        System.out.println(w);
        g.fillArc(this.width/2-pixelRadius, this.height/2-pixelRadius, 
      		      pixelRadius*2, pixelRadius*2,
      		      210, 100);
        
        g.setColor(Color.BLUE);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        
        for (int i = 0; i < yTicks; i++) {
        	g2d.drawLine(this.width/2, this.height/2, 
        			     (int) (this.width/2+pixelRadius*Math.cos(10*i*Math.PI/180.0)), 
        			     (int) (this.height/2-pixelRadius*Math.sin(10*i*Math.PI/180.0)));
        }
        
        g.setColor(Color.BLACK);
        g.fillArc((int) (this.width/2+pixelRadius*Math.cos(210*Math.PI/180.0))-5, 
        		  (int) (this.height/2-pixelRadius*Math.sin(210*Math.PI/180.0)-5),
        		  10, 10, 0, 360);
        
        g2d.dispose();
	}
}
