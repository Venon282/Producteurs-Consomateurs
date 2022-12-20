package prodcons.v6;

public class Message {
	
	private int id;
	private String message;
	private int nb_messages;

	Message(){
		this.id=-1;
		this.message="I am a message";
		this.nb_messages=1;
	}
	
	Message(String message){
		this.message=message;
		this.id=-1;
		this.nb_messages=1;
	}
	
	Message(int id){
		this.message="I am a message";
		this.id=id;
		this.nb_messages=1;
	}
	
	Message(int id, String message){
		this.id=id;
		this.message=message;
		this.nb_messages=1;
	}
	
	Message(String message, int nb_messages){
		this.id=-1;
		this.message=message;
		this.nb_messages=nb_messages;
	}
	
	Message(int id, String message, int nb_messages){
		this.id=id;
		this.message=message;
		this.nb_messages=nb_messages;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNb_messages() {
		return nb_messages;
	}

	public void setNb_messages(int nb_messages) {
		this.nb_messages = nb_messages;
	}

	@Override
	public String toString() {
		return this.id + " : " + this.message + " présent " + this.nb_messages + " fois";
	}
	
}