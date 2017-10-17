import java.util.ArrayList;

class Ex4 {

    private static boolean testing = false;

    // A thread-safe message queue using full synchronization with balking.
    //
    // I chose this pattern because it was the simplest to reason about for
    // this example, even though it might lead to starvation of one thread
    // if multiple treads attempt to add messages, for instance.
    static class MessageQueue {
        private final int size;
        private final String[] messages;

        // Where to remove stuff
        private int front = 0;

        // Where to add stuff
        private int rear = 0;

        public MessageQueue(int size) {
            this.size = size;
            this.messages = new String[size];
        }

        public synchronized void add(String msg) throws BalkingException {
            // Balk if precondition fails.
            //
            // NB: You could also busy-wait here until a slot becomes available,
            // but for consistency reasons, and because it's easier to unit
            // test, I decided to balk here, too.
            if (full()) {
                throw new BalkingException("queue is full");
            }

            // Otherwise, add a message to the rear...
            messages[rear] = msg;

            if (!testing) {
                System.out.println("Message added at " + rear);
            }

            // ... and compute the new rear position.
            rear = (rear + 1) % size;
        }

        public synchronized String remove() throws BalkingException {
            // Balk if precondition fails.
            if (empty()) {
                throw new BalkingException("queue is empty");
            }

            // Otherwise, remove the message from the front position...
            String msg = messages[front];
            messages[front] = null;

            if (!testing) {
                System.out.println("Message removed from " + front);
            }

            // ... and compute the new front position.
            front = (front + 1) % size;

            return msg;
        }

        private boolean full() {
            return (messages[rear] != null);
        }

        private boolean empty() {
            return (messages[front] == null);
        }
    }

    static class BalkingException extends Throwable {
        public BalkingException(String s) {
            super(s);
        }
    }

    // Thread that keeps sending messages for demo purposes.
    static class Producer extends Thread {
        private MessageQueue queue;

        public Producer(MessageQueue queue) {
            this.queue = queue;
        }

        public void run() {
            while(true) {
                try {
                    queue.add("HI THERE!");
                } catch (BalkingException e) {
                    System.out.println("Queue full, retrying...");
                    Thread.yield();
                }
            }
        }
    }

    // Thread that keeps removing messages for demo purposes.
    static class Consumer extends Thread {
        private MessageQueue queue;

        public Consumer(MessageQueue queue) {
            this.queue = queue;
        }

        public void run() {
            while(true) {
                try {
                    queue.remove();
                } catch (BalkingException e) {
                    System.out.println("Queue empty, retrying...");
                    Thread.yield();
                }
            }
        }
    }

    // Who needs a test framework, right?
    static class MessageQueueTest {
        private final String PASS = "✓";
        private final String FAIL = "✕";

        private void testFIFO() throws BalkingException {
            MessageQueue queue = new MessageQueue(3);

            queue.add("hi");
            queue.add("there");
            queue.add("pascal");

            assertEquals("hi", queue.remove());
            assertEquals("there", queue.remove());
            assertEquals("pascal", queue.remove());
        }

        private void testBalkingWhenFull() throws BalkingException {
            MessageQueue queue = new MessageQueue(1);

            queue.add("hi");

            try {
                queue.add("there");
            } catch (BalkingException e) {
                pass();
                return;
            }

            fail();
            System.out.println("Should have balked when trying to add to a full queue!");
        }

        private void testBalkingWhenEmpty() throws BalkingException {
            MessageQueue queue = new MessageQueue(2);

            try {
                queue.remove();
            } catch (BalkingException e) {
                pass();
                return;
            }

            fail();
            System.out.println("Should have balked when trying to remove from an empty queue!");
        }

        private void testComplexBalkingExample() throws BalkingException {
            MessageQueue queue = new MessageQueue(3);

            queue.add("hi");
            queue.add("there");

            assertEquals("hi", queue.remove());

            queue.add("foobar");
            queue.add("spongebob");

            try {
                queue.add("squarepants");
            } catch (BalkingException e) {
                pass();
                return;
            }

            fail();
            System.out.println("Should have balked when trying to add to a full queue!");
        }

        // Struggled to guarantee safety in a test. Here, I'm just checking that
        // the results "makes sense" in the end.
        private void testConcurrentBehaviour() {
            final MessageQueue queue = new MessageQueue(3);

            // How many messages to send around.
            final int numMessages = 6;

            final ArrayList<String> received = new ArrayList<>();

            Thread producer = new Thread() {
                public void run() {
                    for (int i = 0; i < numMessages; i++) {
                        boolean messageSent = false;
                        while (!messageSent) {
                            try {
                                queue.add("Message " + i);
                                messageSent = true;
                            } catch (BalkingException e) {
                                Thread.yield();
                            }
                        }
                    }
                }
            };

            Thread consumer = new Thread() {
                public void run() {
                    for (int i = 0; i < numMessages; i++) {
                        boolean messageReceived = false;
                        while (!messageReceived) {
                            try {
                                received.add(queue.remove());
                                messageReceived = true;
                            } catch (BalkingException e) {
                                Thread.yield();
                            }
                        }
                    }
                }
            };

            producer.start();
            consumer.start();

            try {
                producer.join();
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals(6, received.size());

            assertEquals("Message 0", received.get(0));
            assertEquals("Message 5", received.get(5));
        }

        public void run() {
            try {
                testFIFO();
                testBalkingWhenFull();
                testBalkingWhenEmpty();
                testComplexBalkingExample();
                testConcurrentBehaviour();
            } catch (BalkingException e) {
                e.printStackTrace();
            }
        }

        private void pass() {
            System.out.print(PASS);
        }

        private void fail() {
            System.out.println(FAIL);
        }

        private void assertEquals(Object expected, Object actual) {
            if (expected.equals(actual)) {
                pass();
            } else {
                fail();
                int callerLine = Thread.currentThread().getStackTrace()[2].getLineNumber();
                System.out.println("Expected \""+ expected + "\" to equal \"" +
                        actual + "\" on line " + callerLine);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Ex4 [test|<QUEUE_SIZE>]");
            System.exit(1);
        }

        if (args[0].toLowerCase().equals("test")) {
            testing = true;
            new MessageQueueTest().run();
            System.exit(0);
        }

        int size = Integer.parseInt(args[0]);

        MessageQueue queue = new MessageQueue(size);

        new Producer(queue).start();
        new Consumer(queue).start();
    }
}