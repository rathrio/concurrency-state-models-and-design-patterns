package pd;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MessageQueueTest {

	@Test
	public void queueStressTest() throws InterruptedException {
		int threadCount = 10;
		final MessageQueue mq = new MessageQueue(5);
		List<Thread> threads = new ArrayList<Thread>();
		for (int i=0; i < threadCount; i++) {
			threads.add(new MessageProducer(mq, 100));
			threads.add(new MessageConsumer(mq, 100));
		}
		for (Thread thread: threads) { thread.start(); }
		
		for (Thread thread: threads) { thread.join(); }
		
		assertEquals(true, mq.isEmpty());
	}
}

class MessageProducer extends Thread {

	private final MessageQueue mq;
	private final int nrMessages;

	MessageProducer(MessageQueue mq, int nrMessages) {
		this.mq = mq;
		this.nrMessages = nrMessages;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < nrMessages; i++) {
			mq.add("Message " + i + " from thread " + this.getName());
			System.out.println("Added message from thread " + i + "\t\tQueue Size: " + mq.size()); // Be aware that the output here is not synchronized, so the size could be wrong. We use it just for illustration purposes here.
		}
	}
}

class MessageConsumer extends Thread {
	
	private final MessageQueue mq;
	private final int nrMessages;

	MessageConsumer(MessageQueue mq, int nrMessages) {
		this.mq = mq;
		this.nrMessages = nrMessages;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < nrMessages; i++) {
			mq.remove();
			System.out.println("Removed message from thread " + i + "\t\tQueue Size: " + mq.size()); // Be aware that the output here is not synchronized, so the size could be wrong. We use it just for illustration purposes here.
		}
	}
}