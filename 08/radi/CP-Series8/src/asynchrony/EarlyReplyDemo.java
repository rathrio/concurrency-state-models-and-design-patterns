package asynchrony;

import slot.Slot;

/**
 * We start several client Threads that pose requests to our server.
 * For each request, the server spawns a Thread that computes the
 * result, puts it in the output queue, and performs a cleanup.
 * Asynchronously, the output result is returned to the client.
 * The server Thread holds the server lock till the cleanup is done.
 * 
 * Lecture: Liveness and Asynchrony
 * 
 * $Id: EarlyReplyDemo.java 24404 2009-01-26 19:36:11Z oscar $
 *
 */
public class EarlyReplyDemo {
	protected int request=0;

	protected int id() {
		return request;
	}
	
	public static void main(String args[]) {
		EarlyReplyDemo server = new EarlyReplyDemo();
		startDemoThread(server, 45);
		startDemoThread(server, 35);
		startDemoThread(server, 25);
		startDemoThread(server, 15);
		startDemoThread(server, 5);
	}

	protected static void startDemoThread(final EarlyReplyDemo server, final int n) {
		new Thread() {
			public void run() {
				System.out.println(n + " CALLING service(" + n + ")");
				int val = ((Integer) server.service(n)).intValue();
				System.out.println(n + " GOT service(" + n + ") = " + val);
			}
		}.start();
	}

	public Object service(final int n) {			// unsynchronized
		final Slot<Integer> reply = new Slot<Integer>();
		final EarlyReplyDemo host = this;
		new Thread() {								// Helper
			public void run() {
				synchronized (host) {
					reply.put(host.compute(n));
					host.cleanup();					// retain lock
				}
			}
		}.start();
		return reply.get(); // early reply
	}

	protected Integer compute(int request) {
		this.request = request;
		//randomSleep(); // simulate longer computation time
		int result = fibonacci(this.request);
		System.out.println(id() + " Computed result: " + result);
		return new Integer(result);
	}
	
	public static int fibonacci(int n) {
		if (n<2) { return 1; }
		else { return fibonacci(n-1) + fibonacci(n-2); }
	}

	protected void cleanup() {
		System.out.println(id() + " Cleaning up");
		Thread.yield(); // will not be interrupted
		System.out.println(id() + " FINISHED cleaning up");
		this.request = 0;
	}

	protected void randomSleep() {
		try {
			Thread.sleep((int)(Math.random() * 1000));
		} catch (InterruptedException e) { }
	}


}
