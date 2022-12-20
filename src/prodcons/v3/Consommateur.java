package prodcons.v3;

public class Consommateur extends Thread{
	private int id;
	private ProdConsBuffer management;
	private int consTime;
	
	public Consommateur(ProdConsBuffer management, int consTime) {
		this.id=-1;
		this.management=management;
		this.consTime=consTime;
		setDaemon(true);
		this.start();
	}

	public Consommateur(ProdConsBuffer management, int id,int consTime) {
		this.id=id;
		this.management=management;
		this.consTime=consTime;
		setDaemon(true);
		this.start();
	}

	public void run() {
		while(true) {
			try {
				Message m = this.management.get();
				System.out.println("Consumer "+ this.id+" get the message :");
				System.out.println("\t" + m.toString());
				sleep(this.consTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}
