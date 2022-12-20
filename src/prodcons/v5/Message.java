package prodcons.v5;

public class Message {
	
	private int id;
	private String message;
	
	Message(){
		this.id=-1;
		this.message="I am a message";
	}
	
	Message(String message){
		this.message=message;
		this.id=-1;
	}
	
	Message(int id){
		this.message="I am a message";
		this.id=id;
	}
	
	Message(int id, String message){
		this.id=id;
		this.message=message;
	}

	@Override
	public String toString() {
		return this.id + " : " + this.message;
	}
	
}