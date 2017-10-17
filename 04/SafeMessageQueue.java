import java.util.ArrayList;
import java.util.List;

public class SafeMessageQueue {
	
	private List<String> queue;
	
	private int capacity;
	
	public SafeMessageQueue(int capacity){
		if(0 < capacity && capacity < Integer.MAX_VALUE){
			this.queue = new ArrayList<String>(capacity);
			this.capacity = capacity;
		}else{
			throw new RuntimeException("Invalid capacity");			
		}
	}
	
	public synchronized void add(String msg){
		while(queue.size() >= capacity){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(queue.size() < capacity){
			queue.add(msg);
		}else{
			throw new RuntimeException("Cannot exceed capacity size");
		}
	}
	
	public synchronized String remove(){
		if(queue.size() > 0){
			String value = queue.remove(0);
			notifyAll();
			return value;
		}else{
			throw new RuntimeException("Cannot remove from empty queue");
		}
	}

}
