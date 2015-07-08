import it.unibs.algoritmi.CostruisciAutoma;
import it.unibs.algoritmi.CostruisciFileXML;
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
		//AnalisiDOM nuovaAnalisi = new AnalisiDOM("./automi/automa.xml");
		Automa a = CostruisciAutoma.costruisciAutoma("./automi/automa.xml");
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
	    System.out.println("L'automa a e':\n" + a);
	    System.out.println("L'insieme delle transizioni e': \n" + a.getTransizioni());
	    
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
	    Automa badtwin1 = Trasformazioni.badtwin0to1(a);
	    System.out.println("L'automa bad twin livello 1 e':\n" + badtwin1);
		System.out.println("L'insieme delle transizioni e': \n"+ badtwin1.getTransizioni());
		
		Automa goodtwin1 = Trasformazioni.goodtwin1(badtwin1);
	    System.out.println("L'automa good twin livello 1 e':\n" + goodtwin1);
		System.out.println("L'insieme delle transizioni e': \n"+ goodtwin1.getTransizioni());
		
	}

}
