package geom.shape.locksplitting;

import java.util.concurrent.locks.ReentrantLock;

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
        
    	// TODO: Apply "Lock Splitting" concept here (you may need some final variables here? :)
        
        // Use separate locks for guarding position and dimension changes.
        final ReentrantLock positionLock;
        final ReentrantLock dimensionLock;
        
        public LockSplittingShape() {
        	// TODO: Apply "Lock Splitting" concept here
        	this.positionLock = new ReentrantLock();
        	this.dimensionLock = new ReentrantLock();
        }
        
        @Override
        public void changePosition() {
        	// TODO: Apply "Lock Splitting" concept here
        	this.positionLock.lock();
        	this.x = (int) (this.x * 1.1);
        	this.y = (int) (this.y * 0.8);
        	this.positionLock.unlock();
        }
        
        @Override
        public void changeDimension() {
        	// TODO: Apply "Lock Splitting" concept here
        	this.dimensionLock.lock();
        	this.width = (int) (this.width * 1.5);
        	this.height = (int) (this.height * 0.3);
        	this.dimensionLock.unlock();
        }
        
        @Override
        public void changePositionAndDimension() {
        	// TODO: Apply "Lock Splitting" concept here
        	this.positionLock.lock();
        	this.y = (int) (this.y * 1.4);
        	this.positionLock.unlock();
        	
        	this.dimensionLock.lock();
        	this.height = (int) (this.height * 0.4);
        	this.dimensionLock.unlock();
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
			// Since this is a public setter method, we'll sync here, too.
			this.positionLock.lock();
			this.x = x;
			this.y = y;
			this.positionLock.unlock();
			
			this.dimensionLock.lock();
			this.width = width;
			this.height = height;
			this.dimensionLock.unlock();
		}
}
