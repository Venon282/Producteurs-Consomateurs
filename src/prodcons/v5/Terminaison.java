package prodcons.v5;

public class Terminaison {
	private int margin;

	Terminaison(int margin){
		this.margin=margin;
	}
	
	public int getMargin() {
		return margin;
	}

	public synchronized void setMargin() {
		this.margin--;
	}
}
