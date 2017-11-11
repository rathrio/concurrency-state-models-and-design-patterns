package pd;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueue {
	private final Lock mutex = new ReentrantLock();
	private final Condition notFull = mutex.newCondition();
	private final Condition notEmpty = mutex.newCondition();

	private String[] messages;
	private int head = 0;
	private int tail = 0;
	private int size = 0;
			
	public MessageQueue(int maxSize) {
		assert maxSize > 0;
		this.messages = new String[maxSize];
	}
	
	public int size() {
		return this.size;
	}
	
	public int maxSize() {
		return this.messages.length;
	}
	
	public boolean isFull() {
		if(!(this.size < this.messages.length)) {
			return true;
		}
		return false;
	}
	
	public boolean isEmpty() {
		if(this.size == 0) {
			return true;
		}
		return false;
	}
	
	public void add(String s) {
		this.mutex.lock();
		try {
			while (this.isFull()) {
				this.notFull.await();
			}
			
			this.messages[this.tail] = s;
			this.tail = (this.tail + 1) % this.messages.length;
			this.size++;
			this.notEmpty.signal();
		} catch (InterruptedException e) {
		} finally { this.mutex.unlock(); }
	}
	
	public String remove() {
		this.mutex.lock();
		
		try {
			while (this.isEmpty()) {
				this.notEmpty.await();
			}
			
			String msg = this.messages[this.head];
			this.messages[this.head] = null;
			this.head = (this.head + 1 ) % this.messages.length;	
			this.size--;
			this.notFull.signal();
			return msg;
		} catch (InterruptedException e) {
		} finally { this.mutex.unlock(); }
		
		return null;
	}
}
