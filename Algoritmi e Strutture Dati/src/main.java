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
		/*System.out.println("################");
		System.out.println("## Parser XML ##");
		System.out.println("################");
		AnalisiDOM nuovaAnalisi = new AnalisiDOM("./automi/automa.xml");*/
		
		Automa a = new Automa();
		a.add("A","B","a", false);
		a.add("B","A","b",true);
		
		

	    System.out.println("L'automa a e':\n" + a);
	    System.out.println("L'insieme delle transizioni e': " + a.getTransizioni());
	}

}
