package prodcons.v2;

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

	public Consommateur(ProdConsBuffer management, int id, int consTime) {
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
				System.out.println("\t" + m); // commenter car on facilite pas le debug
				sleep(this.consTime); //pour forcer la commutation
				
			} catch (InterruptedException e) {
				System.out.println("Excepiton dans le thread consommateur: "+this.id);
			}
		}	
	}
}
