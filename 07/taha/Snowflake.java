package sse;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Snowflake implements Runnable {

	private int levelOfDetail;
	private int snowflakeSize;
	private int xPos;
	private int yPos;
	private float gravity;
	private Graphics2D g;
	private int outOfViewLimit;
	private float horizontalMovementAmplifier;
	private int horizontalMovement;
	private int verticalMovement;
	
	/**
	 * Snowflake Simulation Environment (SSE)
	 * November 2017
	 * 
	 * @author Pascal Gadient (gadient@inf.unibe.ch) 
	 * 
	 * SCG University of Bern, Concurrency Course
	 * 
	 */
	public Snowflake(int levelOfDetail, int snowflakeSize, int xPos, float gravity, Graphics2D g, int frameHeight, int horizontalMovement, int verticalMovement) {
		Random r = new Random(System.nanoTime());
		r.nextFloat(); // better results (shouldn't be)
		this.levelOfDetail = levelOfDetail;
		this.snowflakeSize = (int) (snowflakeSize * r.nextFloat() + 20);
		this.xPos = xPos;
		this.yPos = 0;
		this.gravity = gravity * (r.nextFloat() * 2) + 5;
		this.g = g;
		this.outOfViewLimit = frameHeight;
		this.horizontalMovementAmplifier = r.nextFloat() * 10 + 1;
		this.horizontalMovement = horizontalMovement;
		this.verticalMovement = verticalMovement;
	}
	
	/**
	 * Renders the snowflake to the assigned canvas.
	 */
	public void render() {
			int x1 = (int) (this.horizontalMovementAmplifier * Math.sin((this.yPos / this.gravity)) / 2) + this.xPos;
			int x2 = x1 + this.snowflakeSize;
			
			this.xPos = this.xPos + horizontalMovement;
			this.yPos = this.yPos + verticalMovement;
			
			int y1 = this.yPos;
			int y2 = y1;
			
			this.yPos = this.yPos + (int) this.gravity;
			Point2D.Double p3 = this.get3rdStartingPoint(x1, y1, x2, y2);
			
			koch(this.g, x1, y1, x2, y2, this.levelOfDetail);
			koch(this.g, p3.getX(), p3.getY(), x1, y1, this.levelOfDetail);
			koch(this.g, x2, y2, p3.getX(), p3.getY(), this.levelOfDetail);
	}
	
	/**
	 * Calculates the von Koch fractal and performs the actual drawing.
	 * @param g Graphics2D object that should be painted
	 * @param x1 start x coordinate of the fractal to generate
	 * @param y1 start y coordindate of the fractal to generate
	 * @param x2 end x coordinate of the fractal to generate
	 * @param y2 end y coordinate of the fractal to generate
	 * @param level represents the level of detail, i.e. fractal iterations
	 */
	private void koch(Graphics g, double x1, double y1, double x2, double y2, int level) {
		// von Koch algorithm
		double a1, b1, a2, b2, a3, b3;
		
		if (level > 1) {
			a1 = (2 * x1 + x2) / 3;
			b1 = (2 * y1 + y2) / 3;
			a2 = (x1 + x2) / 2 + (Math.sqrt(3) * (y2 - y1) / 6);
			b2 = (y1 + y2) / 2 + (Math.sqrt(3) * (x1 - x2) / 6);
			a3 = (2 * x2 + x1) / 3;
			b3 = (2 * y2 + y1) / 3;
			
			koch(g, x1, y1, a1, b1, level - 1);
			koch(g, a1, b1, a2, b2, level - 1);
			koch(g, a2, b2, a3, b3, level - 1);
			koch(g, a3, b3, x2, y2, level - 1);
		} else {
			g.drawLine((int) Math.round(x1), (int) Math.round(y1), (int) Math.round(x2), (int) Math.round(y2));
		}
	}
	
	/**
	 * Calculates the 3rd point that the two other fractal lines should go to (3 fractal lines are needed to create the snowflake, has triangle like shape with no iteration (level of detail = 0).
	 * @param x1 x coordinate of the first point of the final triangle
	 * @param y1 y coordinate of the first point of the final triangle
	 * @param x2 x coordinate of the second point of the final triangle
	 * @param y2 y coordinate of the second point of the final triangle
	 * @return Point2D.Double containing the 3rd point of the final triangle
	 */
	private Point2D.Double get3rdStartingPoint(double x1, double y1, double x2, double y2) {
		double length = x2 - x1;
		double height = (Math.sqrt(3) / 2) * length;
		
		return new Point2D.Double(x1 + length / 2, y1 + height);
	}

	/**
	 * Main render loop of the snowflake threads.
	 */
	public void run() {
		while (!isOutOfView()) {
			
			// ToDo:
			// Here we should synchronize the draw calls, so that all snowflakes together produce drawcalls in batches and not just 
			// arbitrarily. Random draw calls produce flickering UI elements, caused by frequent repaints of the UI framework.
			synchronized(g){
				this.render();
				try {
					g.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("A Snowflake reaches the end of life.");
	}
	
	/**
	 * Lets snowflakes die as soon as they are out of the view.
	 * @return boolean if snowflake is still visible 
	 */
	private boolean isOutOfView() {
		if (this.yPos > this.outOfViewLimit) {
			return true;
		} else {
			return false;
		}
	}
}
 
 
