package prodcons.v5;

public class Producteur extends Thread{
	private int id;
	private ProdConsBuffer management;
	private int prodTime;
	private int minProd;
	private int maxProd;

	public Producteur(ProdConsBuffer management,int prodTime, int minProd, int maxProd) {
		this.id=-1;
		this.management=management;
		this.prodTime=prodTime;
		this.minProd=minProd;
		this.maxProd=maxProd;
		this.start();
	}

	public Producteur(ProdConsBuffer management, int id,int prodTime, int minProd, int maxProd) {
		this.id=id;
		this.management=management;
		this.prodTime=prodTime;
		this.minProd=minProd;
		this.maxProd=maxProd;
		this.start();
	}

	public void run(){
		int prod=(int) (Math.random()*(this.maxProd-this.minProd));
		for(int i=0;i<prod;i++) {
			try {
				this.management.put(new Message(this.id,"Je suis un producteur"));
				sleep(this.prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}