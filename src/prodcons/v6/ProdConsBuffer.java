package prodcons.v6;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Semaphore;


/*
Opération 				Pre-action 		Garde 					Post-action
Produce(Message m) 						notfull					new message
Message Consume() 						not empty				consume message
  
 */

public class ProdConsBuffer implements IProdConsBuffer {
	private int nmessage, nmessage_buffer; 	//nb total message+ nb buffer message
	private int bifSz;						//Buffer size
	private Message[] buffer;
	private Semaphore notFull,notEmpty,mutexIn, mutexOut,fifo;
	int in,out;								//Buffer position
	
	ProdConsBuffer(int bifSz){
		//Init buffer
		this.bifSz=bifSz;
		if(this.bifSz>0)
			this.buffer=new Message[this.bifSz];
		else this.buffer=new Message[1];
		this.in=0;
		this.out=0;
		
		//Init info
		this.nmessage=0;
		this.nmessage_buffer=0;
		
		//Init sémaphore
		this.notFull = new Semaphore(this.bifSz);
		this.notEmpty = new Semaphore(0,true);
		this.mutexIn = new Semaphore(1,true);
		this.mutexOut = new Semaphore(1,true);
		this.fifo = new Semaphore(1,true);
	}
	
	private boolean full() {
		return nmsg() == this.bifSz;
	}
	
	private boolean empty() {
		return nmsg() == 0;
	}

	@Override
	public void put(Message m) throws InterruptedException {
		this.notFull.acquire();
		
		this.mutexIn.acquire();
			this.buffer[this.in]=m;
			this.in=(this.in+1)%this.bifSz;
			this.nmessage_buffer++;	
			this.nmessage++;
		this.mutexIn.release();
		
		notEmpty.release();
		System.out.println("put "+ m.toString());
	}
	/*
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
	}*/
	
	
	@Override
	public synchronized void put(Message m, int n) throws InterruptedException {
		while(this.nmsg()>=this.bifSz)
			wait();

		this.buffer[this.in]=m;
		this.in=(this.in+1)%this.bifSz;
		this.nmessage_buffer++;	
		this.nmessage++;

		notifyAll();

		System.out.println("put "+n+ " -> " + m.toString());

		while(m.getNb_messages()>0)
			wait();

	}

/*	
	@Override
	public  void put(Message m, int n) throws InterruptedException {
		fifo.acquire();
		synchronized(this) {
		while(this.nmsg()>=this.bifSz)
			wait();

		this.buffer[this.in]=m;
		this.in=(this.in+1)%this.bifSz;
		this.nmessage_buffer++;	
		this.nmessage++;

		//notifyAll();

		System.out.println("put "+n+ " -> " + m.toString());

		while(m.getNb_messages()>0)
			wait();
		}
		
		fifo.release();
	}
	*/
	
	@Override
	public synchronized Message get() throws InterruptedException {
		while(this.nmsg()<=0)
			wait();
		
		Message m=this.buffer[out];
		m.setNb_messages(m.getNb_messages()-1);
		if(m.getNb_messages()<=0) {
			this.out=(out+1)%this.bifSz;
			this.nmessage_buffer--;
			notifyAll();
		}
		
		System.out.println("get "+ m.toString());
		while(m.getNb_messages()>0)
			wait();
		
		return m;
	}
	
	/*
	@Override
	public synchronized Message get() throws InterruptedException {
		
		/*while(empty())
			wait();
		
		Message m=this.buffer[out];
		m.setNb_messages(m.getNb_messages()-1);
		if(m.getNb_messages()<=0) {
			this.out=(out+1)%this.bifSz;
			this.nmessage_buffer--;
			
		}
		
		System.out.println("get "+ m.toString());
		/*while(m.getNb_messages()>0)
			wait();
		notifyAll();
		return m;
	}
	*/
	@Override
	public synchronized Message[] get(int k) throws InterruptedException {
		if(k<=0) return null;

		Message []m=new Message[k];
		for(int i=0;i<k;i++) {
			this.notEmpty.acquire();
				m[i]=this.buffer[out];
				this.out=(out+1)%this.bifSz;
				this.nmessage_buffer--;
			this.notFull.release();
		}
		System.out.println("get "+k+ " -> " + m.toString());
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