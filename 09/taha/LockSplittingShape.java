package geom.shape.locksplitting;

import geom.shape.Shape;

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
public class LockSplittingShape implements Shape {
        private int x, y, width, height;
        
        //Taha Sukru Karabacakoglu ex9
        
    	// TODO: Apply "Lock Splitting" concept here (you may need some final variables here? :)
        
        // This is my kind of lock splitting, I hope I get the idea.
        // Several subsets have its own lock. x, y -> positionLock, width, height -> dimensionLock
        private final Object positionLock = new Object();
        private final Object dimensionLock = new Object();
        
        public LockSplittingShape() {
        	// TODO: Apply "Lock Splitting" concept here
        }
        
        @Override
        public void changePosition() {
        	// TODO: Apply "Lock Splitting" concept here
        	synchronized (positionLock) {
        		this.x = (int) (this.x * 1.1);
        		this.y = (int) (this.y * 0.8);
			}
        }
        
        @Override
        public void changeDimension() {
        	// TODO: Apply "Lock Splitting" concept here
        	synchronized (dimensionLock) {
        		this.width = (int) (this.width * 1.5);
        		this.height = (int) (this.height * 0.3);
			}
        }
        
        @Override
        public void changePositionAndDimension() {
        	synchronized (positionLock) {
        		this.y = (int) (this.y * 1.4);
        	}
        	synchronized (dimensionLock) {
        		this.height = (int) (this.height * 0.4);
        	}
        }

		@Override
		public int getX() {
			return this.x;
		}

		@Override
		public int getY() {
			return this.y;
		}

		@Override
		public int getWidth() {
			return this.width;
		}

		@Override
		public int getHeight() {
			return this.height;
		}

		@Override
		public void setRectangle(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
}
