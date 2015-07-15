import java.util.Set;

import it.unibs.algoritmi.CostruisciAutoma;
import it.unibs.algoritmi.CostruisciFileXML;
import it.unibs.algoritmi.Metodi;
import it.unibs.algoritmi.Trasformazioni;
import it.unibs.asd.Automa;
import it.unibs.asd.Evento;

/**
 * 
 */

/**
 * @author federicofalcone
 *
 */
public class main {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("################");
		System.out.println("## Parser XML ##");
		System.out.println("################");
		
		/*
		Evento e1 = new Evento();
		e1.add("a");
		e1.add("b");
		e1.add("b");
		System.out.println(e1);
		Evento e2 = new Evento();
		e2.add("b");
		e2.add("b");
		e2.add("a");
		System.out.println(e2);
		System.out.println(e1.equals(e2));
		System.out.println(e1.cardinalita());*/
		//AnalisiDOM nuovaAnalisi = new AnalisiDOM("./automi/automa.xml");
		Automa a = CostruisciAutoma.costruisciAutoma("./automi/automa.xml");
		boolean diagnosticabile = Metodi.diagnosticabilitaMetodo3(a, 4);
		//System.out.println("L'automa a e':\n" + a);
	    //System.out.println("L'insieme delle transizioni e': \n" + a.getTransizioni());
		//Automa a = CostruisciAutoma.costruisciAutoma(args[1]);
	    
	  

		
		/*Automa a = new Automa();
		a.add("A","B","b", false);
		a.add("B","A","*",false);
		a.add("B","D","*",false);
		a.add("D","D","c",false);
		a.add("A","C","a",false);
		a.add("C","E","d",false);
		a.add("E","F","*",true);
		a.add("F","F","c",false);
		a.add("C","F","*",false);
		System.out.println(a.add("E","A","*",false));
		System.out.println(a.add("E","A","*",false));
		System.out.println(a.add("E","A","*",false));
		
	*/
	    /*System.out.println("L'automa a e':\n" + a);
	    System.out.println("L'insieme delle transizioni e': \n" + a.getTransizioni());
	    */
	    //CostruisciFileXML.automaToXML(a);
	    
	   /*
			System.out
					.println("L'insieme delle transizioni non osservabili e': \n"
							+ a.getTransizioniNonOsservabili());
			System.out.println("L'insieme delle transizioni di guasto e': \n"
					+ a.getTransizioniDiGuasto());
			Automa b = Trasformazioni.inizializzaBadTwin1(a);
			System.out.println("L'automa b e':\n" + b);
			System.out.println("L'insieme delle transizioni e': \n"
					+ b.getTransizioni());
			Automa badtwin1 = Trasformazioni.badtwin0to1(a);
			System.out.println("L'automa bad twin livello 1 e':\n" + badtwin1);
			System.out.println("L'insieme delle transizioni e': \n"
					+ badtwin1.getTransizioni());
			Evento eventoComposto = new Evento();
			Evento o = new Evento("a");
			Evento ot = new Evento("b");
			eventoComposto.add(o.getSetEventiSemplici());
			eventoComposto.add(ot.getSetEventiSemplici());
			System.out.println(eventoComposto);
		*/
		
	    //Automa badtwin1 = Trasformazioni.badtwin0to1(a);
	    //System.out.println("L'automa bad twin livello 1 e':\n" + badtwin1);
		//System.out.println("L'insieme delle transizioni e': \n"+ badtwin1.getTransizioni());
		
		/*Automa goodtwin1 = Trasformazioni.badToGoodTwin(badtwin1);
	    //System.out.println("L'automa good twin livello 1 e':\n" + goodtwin1);
		//System.out.println("L'insieme delle transizioni e': \n"+ goodtwin1.getTransizioni());
		//Set<Object> stati = goodtwin1.getStati();
		//System.out.println(stati);
		
		Automa sincronizzato = Trasformazioni.sincronizzazione(goodtwin1, badtwin1);
		System.out.println("L'automa sincronizzato livello 1 e':\n" + sincronizzato);
		System.out.println("L'insieme degli stati e':\n" + sincronizzato.getStati());
		System.out.println("L'insieme delle transizioni e': \n"+ sincronizzato.getTransizioni());
		System.out.println((sincronizzato.diagnosticabile()?"diagnosticabile\n":"non diagnosticabile\n"));
		if (sincronizzato.diagnosticabile()) {
			int i = 2;
			Automa btiprev = badtwin1;
			Automa gtiprev = goodtwin1;
			while ( sincronizzato.diagnosticabile()){
				System.out.println("*********************************************************************************************************\n");
				Automa bti = Trasformazioni.badtwinLevelUpGenerico(btiprev, i);
				System.out.println("Il bad twin di livello "+i+" e':\n" + bti);
				System.out.println("L'insieme delle transizioni e': \n"+ bti.getTransizioni());
				btiprev =  bti;
				Automa gti = Trasformazioni.badToGoodTwin(bti);
				System.out.println("Il good twin di livello "+i+" e':\n" + gti);
				System.out.println("L'insieme delle transizioni e': \n"+ gti.getTransizioni());
				sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
				System.out.println("*******************************************************************\n");
				System.out.println("L'automa sincronizzato livello "+i+" e':\n");
				System.out.println("L'insieme degli stati e':\n" + sincronizzato.getStati());
				System.out.println("\n"+ sincronizzato);
				System.out.println("L'insieme delle transizioni e': \n"+ sincronizzato.getTransizioni());
				System.out.println("Livello "+i+" "+(sincronizzato.diagnosticabile()?"diagnosticabile\n":"non diagnosticabile\n"));
				i++;
			}
		}*/
	}

}
