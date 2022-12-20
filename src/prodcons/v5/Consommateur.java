package prodcons.v5;

public class Consommateur extends Thread{
	private int id;
	private ProdConsBuffer management;
	private int consTime;
	
	public Consommateur(ProdConsBuffer management, int consTime) {
		this.id=-1;
		this.management=management;
		this.consTime=consTime;
		this.setDaemon(true);
		this.start();
	}

	public Consommateur(ProdConsBuffer management, int id, int consTime) {
		this.id=id;
		this.management=management;
		this.consTime=consTime;
		this.setDaemon(true);
		this.start();
	}

	public void run() {
		while(true) {
			try {
				int nb_consomation=1 + (int)(Math.random() * ((5 - 1) + 1));
				this.management.get(nb_consomation);
				sleep(this.consTime);
			} catch (InterruptedException e) {
				return;
			}
		}	
	}
}