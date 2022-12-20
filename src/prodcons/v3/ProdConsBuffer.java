package prodcons.v3;

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
	private Semaphore notFull,notEmpty,mutexIn, mutexOut;
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
		this.mutexIn = new Semaphore(1,true); //initilialiser à 1 pour ne pas laisser plusieurs methodes y accéder
		this.mutexOut = new Semaphore(1,true);
	}

	@Override
	public void put(Message m) throws InterruptedException {
		this.notFull.acquire();

		this.mutexIn.acquire();
		this.buffer[this.in%this.bifSz]=m;
		this.in=(this.in+1)%this.bifSz;
		this.nmessage_buffer++;	
		this.nmessage++;
		this.mutexIn.release();

		notEmpty.release();
	}

	@Override
	public Message get() throws InterruptedException {
		this.notEmpty.acquire();

		this.mutexOut.acquire();
		Message m=this.buffer[out%this.bifSz];
		this.out=(out+1)%this.bifSz;
		this.nmessage_buffer--;
		this.mutexOut.release();

		this.notFull.release();
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
