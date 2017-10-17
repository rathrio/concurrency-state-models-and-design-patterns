import org.junit.Test;

public class SafeMessageQueueTest {
	
	@Test
	public void testQueue() {
		final SafeMessageQueue messageQueue = new SafeMessageQueue(5);
		new Thread() {
			public void run() {
				messageQueue.add("first1");
				messageQueue.add("first2");
				messageQueue.add("first3");
				messageQueue.add("first4");
				messageQueue.add("first5");
				for(int i = 0; i<5; i++){
					System.out.println("Removing values :" + messageQueue.remove());
				}
			}
		}.start();
		new Thread() {
			public void run() {
				messageQueue.add("second1");
				messageQueue.add("second2");
				messageQueue.add("second3");
				for(int i = 0; i<3; i++){
					System.out.println("Removing values :" + messageQueue.remove());
				}
			}
		}.start();
		new Thread() {
			public void run() {
				messageQueue.add("third1");
				messageQueue.add("third2");
				for(int i = 0; i<2; i++){
					System.out.println("Removing values :" + messageQueue.remove());
				}
			}
		}.start();
		new Thread() {
			public void run() {
				messageQueue.add("fourth1");
				messageQueue.add("fourth2");
				messageQueue.add("fourth3");
				messageQueue.add("fourth4");
				messageQueue.add("fourth5");
				for(int i = 0; i<5; i++){
					System.out.println("Removing values :" + messageQueue.remove());
				}
			}
		}.start();
	}

}
