package eggFarm;


/**
 * A bounded buffer class: 
 * condition synchronization is replaced by semaphore functionality.
 *
 */
public class TheNest implements Buffer {

	protected Object[] buf;
	protected int in = 0;
	protected int out = 0;
	protected int size;

	// Renamed the semaphores to make it more readable to me. They're still
	// used the same.
	protected Semaphore usedSlots;  // counts number of items
	protected Semaphore emptySlots; // counts number of spaces

	public TheNest(int size) {
		this.size = size;
		this.buf = new Object[size];
		this.usedSlots = new Semaphore(0);
		this.emptySlots = new Semaphore(size);
	}

	// No longer synchronized!
	public void put(Object obj) {
		emptySlots.down();
		
		// But here it is. So we reverse the synchronization order. There's no
		// need for a guard loop I believe, because the semaphore above 
		// guarantees that we "haven't missed the signal".
		synchronized(this) {
			buf[in] = obj;
			in = (in + 1) % size;
		}

		usedSlots.up();
	}

	public Object get() {
		usedSlots.down();
		
		Object obj;
		synchronized(this) {
			obj = buf[out];
			buf[out] = null;
			out = (out + 1) % size;
		}

		emptySlots.up();
		return (obj);
	}

}
