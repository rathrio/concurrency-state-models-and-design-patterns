package eggFarm;

public class Farm {

	public static void main(String args[]) {
		(new Farm()).demo();
	}

	public void demo() {
		Buffer buffer = new TheNest(5);
		new Chicken("Clarissa", buffer).start();
		new Farmer("Napoleon", buffer).start();
	}

	private class Chicken extends Thread {
		protected Buffer buffer;
		protected String[] eggs = { "whiteEgg", "brownEgg", "pinkEgg",
				"greenEgg", "yellowEgg" };

		Chicken(String name, Buffer buffer) {
			super(name);
			this.buffer = buffer;
		}

		public void run() {
			while (true) {
				String egg = new String(eggs[random(5)]);
				System.out.println(getName() + " is laying a " + egg);
				buffer.put(egg);
				randomSleep();
			}
		}
	}

	private class Farmer extends Thread {
		protected Buffer buffer;

		Farmer(String name, Buffer buffer) {
			super(name);
			this.buffer = buffer;
		}

		public void run() {
			while (true) {
				String egg = buffer.get().toString();
				System.out.println(getName() + " got a " + egg);
				randomSleep();
			}
		}
	}

	private static int random(int n) {
		return (int)Math.floor(Math.random() * n);
	}
	
	protected void randomSleep() {
		try {
			Thread.sleep((int)(Math.random() * 1000));
		} catch (InterruptedException e) { }
	}
	
}
