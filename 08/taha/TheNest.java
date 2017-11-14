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

	protected Semaphore full;  // counts number of items
	protected Semaphore empty; // counts number of spaces

	public TheNest(int size) {
		this.size = size;
		this.buf = new Object[size];
		this.full = new Semaphore(0);
		this.empty = new Semaphore(size);
	}

	//remove synchronized from method and create synchronized block around critical section
	public void put(Object obj) {
		empty.down();
		synchronized (this) {
			buf[in] = obj;
			in = (in + 1) % size;
		}
		full.up();
	}

	//remove synchronized from method and create synchronized block around critical section
	public Object get() {
		full.down();
		Object obj;
		synchronized (this) {
		obj = buf[out];
			buf[out] = null;
			out = (out + 1) % size;
		}
		empty.up();
		return (obj);
	}

}
