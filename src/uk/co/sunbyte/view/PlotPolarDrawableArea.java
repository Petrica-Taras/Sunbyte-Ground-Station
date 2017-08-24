package uk.co.sunbyte.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.JPanel;

public class PlotPolarDrawableArea extends JPanel{
	private int[][] locations; // we assume data contains only angles now
	int width;
	int height; 
	int xTicks;
	int yTicks;	
	
	public PlotPolarDrawableArea(int width, int height, Dimension ticks, double[][] data) {
		this.setPreferredSize(new Dimension(width, height));
		this.width = width;
		this.height = height;
		
		this.xTicks = ticks.width;
		this.yTicks = ticks.height;
		
    	this.setBackground(new Color(255, 255, 255));		
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.setColor(Color.BLUE);
        
        int h = g.getFontMetrics().getAscent();
        
        g.drawString("N", this.width/2, h);
        g.drawString("S", this.width/2, this.height-1);
        g.drawString("W", 1, this.height/2);
        g.drawString("E", this.width-h, this.height/2);
        
        g.drawArc(h+1, h+1, 
        		this.width-5-2*h, this.height-5-2*h,
        		0, 360);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        
        for (int i = 0; i < yTicks; i++) {
        	g2d.drawLine(this.width/2, this.height/2, 
        			     (int) ((this.width-5-2*h)*Math.cos(10*i*Math.PI/180.0)/2.0), 
        			     (int) ((this.width-5-2*h)*Math.sin(10*i*Math.PI/180.0)/2.0));
        	System.out.println("-----------");
        	System.out.println(i);
        	System.out.println((int) ((this.width-5-2*h)*Math.cos(10*i*Math.PI/180.0)/2.0));
        	System.out.println((int) ((this.width-5-2*h)*Math.sin(10*i*Math.PI/180.0)/2.0));
        }
        
        g2d.dispose();
	}
}
