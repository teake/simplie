package edu.rug.hep.simplie.ui.shapes;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;


public class LinesWithArrow implements Shape
{
	private final GeneralPath shape;
	
	public LinesWithArrow(Point2D begin, Point2D end, int numLines, float width, boolean arrow)
	{
		float x1 = (float) begin.getX();
		float y1 = (float) begin.getY();
		float x2 = (float) end.getX();
		float y2 = (float) end.getY();
		
		// Calculate some stuff first.
		float spacing	= width / (numLines - 1);
		float length	= (float) Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
		
		// The lines.
		shape = new GeneralPath();
		for (int i = 0; i < numLines; i++)
		{
			shape.moveTo(x1, y1+(i*spacing)-width/2);
			shape.lineTo(x1 + length, y1+(i*spacing)-width/2);
		}
		
		// The arrow.
		if(arrow)
		{
			shape.moveTo(x1 + length/2 - width/5, y1 - width);
			shape.lineTo(x1 + length/2 + width/5, y1);
			shape.lineTo(x1 + length/2 - width/5, y1 + width);
		}
		
		// And finally rotate it.
		double rad = this.calcAngle(x1,y1,x2,y2);
		shape.transform(AffineTransform.getRotateInstance(rad,x1,y1));
	}
	
	
	private double calcAngle(float x1, float y1, float x2, float y2)
	{
		double rad = 0.0d;
		float dx = x2-x1;
		float dy = y2-y1;
		
		if (dx == 0.0)
		{
			if (dy == 0.0)
			{
				rad = 0.0;
			}
			else if (dy > 0.0)
			{
				rad = Math.PI / 2.0;
			}
			else
			{
				rad = Math.PI * 3.0 / 2.0;
			}
		}
		else if (dy == 0.0)
		{
			if (dx > 0.0)
			{
				rad = 0.0;
			}
			else
			{
				rad = Math.PI;
			}
		}
		else
		{
			if (dx < 0.0)
			{
				rad = Math.atan(dy/dx) + Math.PI;
			}
			else if (dy < 0.0)
			{
				rad = Math.atan(dy/dx) + (2*Math.PI);
			}
			else
			{
				rad = Math.atan(dy/dx);
			}
		}
		
		return rad;
	}
	
	public Rectangle2D getBounds2D()
	{
		return this.shape.getBounds2D();
	}
	
	public Rectangle getBounds()
	{
		return this.shape.getBounds();
	}
	
	public boolean contains(double x, double y)
	{
		return this.shape.contains(x,y);
	}
	
	public boolean contains(double x, double y, double w, double h)
	{
		return this.shape.contains(x,y,w,h);
	}
	
	public boolean intersects(double x, double y, double w, double h)
	{
		return this.shape.intersects(x,y,w,h);
	}
	
	public boolean contains(Point2D p)
	{
		return this.shape.contains(p);
	}
	
	public boolean contains(Rectangle2D r)
	{
		return this.shape.contains(r);
	}
	
	public boolean intersects(Rectangle2D r)
	{
		return this.shape.intersects(r);
	}
	
	public PathIterator getPathIterator(AffineTransform at)
	{
		return this.shape.getPathIterator(at);
	}
	
	public PathIterator getPathIterator(AffineTransform at, double flatness)
	{
		return this.shape.getPathIterator(at,flatness);
	}
}
