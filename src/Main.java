
public class Main {
	
	public static void main(String[] args){
		Main starter = new Main();
		starter.start();
	}
	public void start() {
		try {
			LibLoader.loadSerialLibs();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		SerialListener main = new SerialListener();
		main.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
		t.start();
		System.out.println("Started");
	}
}
