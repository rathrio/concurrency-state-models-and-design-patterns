import java.util.Random;

class Ex4 {
    static class MessageQueue {
        private final int size;
        private final String[] messages;

        private int front = 0;
        private int rear = 0;

        public MessageQueue(int size) {
            this.size = size;
            this.messages = new String[size];
        }

        public void add(String msg) {
            if (messages[rear] != null) {
                // queue is full
                return;
            }

            messages[rear] = msg;
            System.out.println("Message added at " + rear);

            rear = (rear + 1) % size;
        }

        public String remove() throws Exception {
            if (messages[front] == null) {
                // queue is empty
                throw new Exception("Queue is empty");
            }

            String msg = messages[front];
            messages[front] = null;
            System.out.println("Message removed from " + front);

            front = (front + 1) % size;

            return msg;
        }
    }

    static class Client extends Thread {
        private final int id;
        private MessageQueue queue;

        public Client(int id, MessageQueue queue) {
            this.id = id;
            this.queue = queue;
        }

        public void run() {
            while(true) {
                int decision = new Random().nextInt(2);
                if (decision == 0) {
                    queue.add("HI THERE!");
                    System.out.println("Client " + id + " added a message");
                } else {
                    try {
                        queue.remove();
                        System.out.println("Client " + id + " removed a message");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Ex4 <QUEUE_SIZE>");
            System.exit(1);
        }

        int size = Integer.parseInt(args[0]);

        MessageQueue queue = new MessageQueue(size);

        Client c1 = new Client(1, queue);
        Client c2 = new Client(2, queue);

        c1.start();
        c2.start();
    }
}