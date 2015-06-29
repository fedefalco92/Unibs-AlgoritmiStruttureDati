import it.unibs.algoritmi.CostruisciAutoma;
import it.unibs.asd.Automa;

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
	}

}
