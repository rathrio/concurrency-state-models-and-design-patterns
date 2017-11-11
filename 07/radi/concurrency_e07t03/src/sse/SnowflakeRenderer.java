package sse;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Snowflake Simulation Environment (SSE)
 * November 2017
 * 
 * @author Pascal Gadient (gadient@inf.unibe.ch) 
 * 
 * SCG University of Bern, Concurrency Course
 * 
 */
public class SnowflakeRenderer {

	private JFrame frame;
	private int levelOfDetail = 3;				// how many fractal iterations we should perform (greatly impacts performance!)
	private int snowflakeSize = 30;				// how large the snowflakes should be
	private float gravity = 9.81f;				// how fast the snowflakes should fall to the bottom
	private int frameHeight = 500;				// window height
	private int frameWidth = 500;				// window width
	private int snowflakeIncreaseRate = 1;		// how many snowflakes will be added each addSnowflakesEachNthRound
	private int addSnowflakesEachNthRound = 7;	// adds [snowflakeIncreaseRate] snowflakes after each ...th  snowflake draw call session (that should be synchronized by you :)
	
	public static void main(String[] args) {
		SnowflakeRenderer instance = new SnowflakeRenderer();
		instance.letItSnow();
	}
	
	public SnowflakeRenderer() {
		this.frame = new JFrame();
		this.frame.setTitle("SCG - Snowflake Simulation Environment (SSE)");
		this.frame.setBackground(new Color(255, 255, 255));
		this.frame.setSize(this.frameWidth, this.frameHeight);
		this.frame.setVisible(true);
		this.frame.setIgnoreRepaint(true);
		this.frame.addWindowListener( new WindowAdapter() {
             @Override
             public void windowClosing(WindowEvent we) {
                 System.exit(0);
             }
         } );
		
		URL iconURL = getClass().getResource("/sse/scg-logo.png");
		ImageIcon icon = new ImageIcon(iconURL);
		this.frame.setIconImage(icon.getImage());
	}
	
	/**
	 * As it says: Let it snow! :)
	 */
	private void letItSnow() {
		Graphics2D g = (Graphics2D) this.frame.getGraphics();
		int currentBatch = 0;
		int currentSnowflakes = 0;

		while (true) {
			// introduces new snowflakes when we finally reached the nth run of draw calls
			if ((currentBatch % this.addSnowflakesEachNthRound) == 0) {
				
				// spawn #snowflakeIncreaseRate snowflake threads
				createNewSnowflakes(g);
				
				currentSnowflakes = currentSnowflakes + this.snowflakeIncreaseRate;
				System.out.println("Current #snowflakes: " + currentSnowflakes);
				
				if (currentBatch > 0) {
					currentBatch = 0;
				}
			}
			
			currentBatch++;

			// clean canvas
			this.frame.paintComponents(g);
			
			// ToDo:
			// Here we should let all snowflakes draw themselves and wait until they finished redrawing, before we progress.
			// So this smells like time for more synchronisation :)
			
			// Snowflakes await a signal on the graphics 2d object before rendering, so we send it here to all of them.
			synchronized (g) {
				g.notifyAll();
			}
			
			
			// sleep some time before we start the next cycle
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Here we instantiate the snowflakes, i.e, spawn the snowflake threads.
	 * @param g is the Graphics2D object to paint on
	 */
	private void createNewSnowflakes(Graphics2D g) {
		for (int i = 0; i < this.snowflakeIncreaseRate; i++) {
			Random r = new Random();
			int xPosSnowflake = (int) (r.nextDouble() * this.frameWidth);
			Snowflake s = new Snowflake(this.levelOfDetail, this.snowflakeSize, xPosSnowflake, this.gravity, g, this.frameHeight);
			
			
//			s.run();
			// ToDo:
			// Instead of s.run() you should make Snowflake a Runnable, i.e. be able to perform: 
			// Thread t = new Thread(s);
			// t.start();
			//
			// Then the Snowflake should draw itself on the Graphics2D object without any further interaction.
			System.out.println("Spawning snowflake.");
			Thread t = new Thread(s);
			t.start();
		}
	}
}
