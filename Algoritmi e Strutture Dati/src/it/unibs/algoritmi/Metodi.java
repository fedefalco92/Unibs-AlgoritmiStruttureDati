/**
 * 
 */
package it.unibs.algoritmi;

import it.unibs.asd.Automa;

/**
 * @author Manutenzione
 *
 */
public class Metodi {
	
	public static boolean diagnosticabilitaMetodo1(Automa a, int livello){
		int i = 1;
		Automa btiprev = a;
		while (i<=livello){
			System.out.println("*********************************************************************************************************\n");
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			System.out.println("Il bad twin di livello "+i+" e':\n" + bti);
			System.out.println("L'insieme delle transizioni e': \n"+ bti.getTransizioni());
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			System.out.println("Il good twin di livello "+i+" e':\n" + gti);
			System.out.println("L'insieme delle transizioni e': \n"+ gti.getTransizioni());
			Automa sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
			System.out.println("*******************************************************************\n");
			System.out.println("L'automa sincronizzato livello "+i+" e':\n");
			System.out.println("L'insieme degli stati e':\n" + sincronizzato.getStati());
			System.out.println("\n"+ sincronizzato);
			System.out.println("L'insieme delle transizioni e': \n"+ sincronizzato.getTransizioni());
			System.out.println("Livello "+i+" "+(sincronizzato.diagnosticabile()?"diagnosticabile\n":"non diagnosticabile\n"));
			if(!sincronizzato.diagnosticabile()){
				System.out.println("Il livello di diagnosticabilita' massimo e' " + (i-1));
				return false;
			}
			
			btiprev =  bti;
			i++;
		}

		
		
		
		return true;
		
		
	}

}
