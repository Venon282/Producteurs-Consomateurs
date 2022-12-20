package prodcons.v3;

import java.io.IOException;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) {
		//Read settings
		Properties properties = new Properties();
		try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("prodcons/v3/option.xml"));
		} catch (IOException e) {
			System.out.println("ERROR : loadinf of option.xml");
			e.printStackTrace();
		}
		
		int nProd = Integer.parseInt(properties.getProperty("nProd"));
		int nCons = Integer.parseInt(properties.getProperty("nCons"));
		int bifSz = Integer.parseInt(properties.getProperty("bufSz"));
		int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
		int consTime = Integer.parseInt(properties.getProperty("consTime"));
		int minProd = Integer.parseInt(properties.getProperty("minProd"));
		int maxProd = Integer.parseInt(properties.getProperty("maxProd"));
		
		ProdConsBuffer management = new ProdConsBuffer(bifSz);
		
		Producteur prods[] = new Producteur[nProd];
		Consommateur cons[] = new Consommateur[nCons];
		//Terminaison terminaison = new Terminaison(nProd);
		
		for(int i=0;i<nProd;i++)
			prods[i]=new Producteur(management,i,prodTime,minProd,maxProd);
		
		for(int i=0;i<nCons;i++)
			cons[i]=new Consommateur(management,i,consTime);
		
		for(int i = 0; i < nProd; i++) {
			try {
				prods[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		while(management.nmsg() > 0) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		boolean test = management.nmsg() == 0;
		System.out.println("Est ce que les messages sont tous consommés ? "+ test);
		System.out.println("nmobre total de message produit ? "+ management.totmsg());
	}

}
