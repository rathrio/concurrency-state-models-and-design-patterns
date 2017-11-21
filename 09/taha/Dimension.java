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
public class Dimension {
        private int width, height;
        
        //Taha Sukru Karabacakoglu ex9
        
		// TODO: Do we need synchronization for this method?
        public synchronized void changeBoth() {
            // In Pass-Through, synchronization is done in helper methods
        	this.height = (int) (this.height * 0.4);
        }
        
		// TODO: Do we need synchronization for this method?
        public synchronized void changeOne() {
            // In Pass-Through, synchronization is done in helper methods
        	this.width = (int) (this.width * 1.5);
        	this.height = (int) (this.height * 0.3);
        }
        
        public int width() {
        	return this.width;
        }
        
        public int height() {
        	return this.height;
        }
        
        public void width(int newValue) {
        	this.width = newValue;
        }
        
        public void height(int newValue) {
        	this.height = newValue;
        }
}
