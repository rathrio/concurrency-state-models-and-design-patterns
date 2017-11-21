package geom.shape.passthrough;

/**
 * Geometry Simulation Environment (GSE)
 * November 2017
 * 
 * 
 * @author Pascal Gadient (gadient@inf.unibe.ch) 
 * 
 * SCG University of Bern, Concurrency Course
 * 
 */
public class Position {
        private int x, y;
        
		// TODO: Do we need synchronization for this method?
        public synchronized void changeBoth() {
        	// TODO: Apply "Pass Through" concept here
        	this.x = (int) (this.x * 1.1);
        	this.y = (int) (this.y * 0.8);
        }
        
		// TODO: Do we need synchronization for this method?
        public synchronized void changeOne() {
        	// TODO: Apply "Pass Through" concept here
        	this.y = (int) (this.y * 1.4);
        }
        
        public int x() {
        	return this.x;
        }
        
        public int y() {
        	return this.y;
        }
        
        // Since the setters are public and also change state, they're synchronized, too.
        public synchronized void x(int newValue) {
        	this.x = newValue;
        }
        
        public synchronized void y(int newValue) {
        	this.y = newValue;
        }
}
