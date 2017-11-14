package eggFarm;

public class Semaphore {
	
	public int value;
	
	public Semaphore() { // binary semaphore
		this.value = 1;
	}
	
	public Semaphore(int sem) { // general semaphore
		this.value = sem;
	}

	public synchronized void down() {
		while(value == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		value--;	
	}
	
	public synchronized void up() {
		value++;
		notifyAll();  // notify all threads waiting on this semaphore
	}
	
}
