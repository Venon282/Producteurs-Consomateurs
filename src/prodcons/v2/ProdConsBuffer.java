package prodcons.v2;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Semaphore;



/*
Opération 				Pre-action 		Garde 					Post-action
Produce(Message m) 						!full					new message
Message Consume() 						!empty 			consume message
  
 */

public class ProdConsBuffer implements IProdConsBuffer {
	private int nmessage, nmessage_buffer; 	//nb total message+ nb buffer message
	private int bifSz;						//Buffer size
	private Message[] buffer; //tampon
	int in,out;								//Buffer position
	int inside;
	
	ProdConsBuffer(int bifSz){
		//Init buffer
		this.bifSz=bifSz;
		if(this.bifSz>0)
			this.buffer=new Message[this.bifSz];
		else
			this.buffer=new Message[1];
		this.in=0;
		this.out=0;
		
		this.inside = 0;
		//Init info
		this.nmessage=0;
		this.nmessage_buffer=0;
		
	}
	
	private boolean full() {
		return nmsg() == this.bifSz;
	}
	
	private boolean empty() {
		//boolean in =  (this.in % this.bifSz) == 0;
		//boolean out = (this.in % this.bifSz) == (this.out % this.bifSz);
		//return in == out;
		return nmsg() == 0;
	}

	@Override
	public synchronized void put(Message m) throws InterruptedException {
			while (full())
				wait();
			
			this.buffer[this.in]=m;
			this.in=(this.in+ 1)%this.bifSz ;
			this.nmessage_buffer++;	
			this.nmessage++;
			notifyAll();
	}

	@Override
	public synchronized Message get() throws InterruptedException {
		
		while(empty())
			wait();
		
			Message m=this.buffer[out];
			System.out.println("get :"+m);
			this.out= (out+ 1)%this.bifSz ;
			this.nmessage_buffer--;
			
			notifyAll();
		return m;
	}

	@Override
	public int nmsg() {
		return this.nmessage_buffer;
	}

	@Override
	public int totmsg() {
		return this.nmessage;
	}
	


}
