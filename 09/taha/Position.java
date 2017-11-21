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
        
        //Taha Sukru Karabacakoglu ex9
        
		// TODO: Do we need synchronization for this method?
        public synchronized void changeBoth() {
        	// In Pass-Through, synchronization is done in helper methods
        	this.y = (int) (this.y * 1.4);
        }
        
		// TODO: Do we need synchronization for this method?
        public synchronized void changeOne() {
        	// In Pass-Through, synchronization is done in helper methods
        	this.x = (int) (this.x * 1.1);
        	this.y = (int) (this.y * 0.8);
        }
        
        public int x() {
        	return this.x;
        }
        
        public int y() {
        	return this.y;
        }
        
        public void x(int newValue) {
        	this.x = newValue;
        }
        
        public void y(int newValue) {
        	this.y = newValue;
        }
}
