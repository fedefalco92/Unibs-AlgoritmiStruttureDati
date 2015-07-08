/**
 * 
 */
package it.unibs.algoritmi;

import java.util.HashSet;
import java.util.Set;

import it.unibs.asd.Automa;
import it.unibs.asd.Evento;
import it.unibs.asd.Transizione;

/**
 * @author Manutenzione
 *
 */
public class Trasformazioni {
	
	/**
	 * Trasforma il bad twin di livello 0 (l'automa di partenza) 
	 * nel bad twin di livello 1 
	 * che ha solo transizioni osservabili.
	 */
	public static Automa badtwin0to1(Automa a){
		Automa badtwin1 = inizializzaBadTwin1(a);
		Set<Transizione> tNonOsservabili = a.getTransizioniNonOsservabili();
		for(Transizione t : tNonOsservabili){
			Set<Transizione> triplette = find(a,t.getStatoArrivo(),1,t.isGuasto(),new Evento());
			if(!triplette.isEmpty()){
				for (Transizione tripletta:triplette){
					badtwin1.add(t.getStatoPartenza(), tripletta.getStatoArrivo(), tripletta.getEvento(), tripletta.isGuasto());
				}					
			}
		}
		
		return badtwin1;
	}

	public static Automa inizializzaBadTwin1(Automa a) {
		Automa bt = new Automa();
		
		Set<Transizione> transizioni = a.getTransizioni();
		for(Transizione t:transizioni){
			if(!t.isGuasto()&&!t.nonOsservabile()){
				bt.add(t);				
			}
		}		
		return bt;
	}
	
	/**
	 * restituisce un insieme di "triplette" contenenti:
	 * - un evento osservabile
	 * - uno stato di arrivo
	 * - il valore di guasto
	 * 
	 * queste triplette non sono altro che transizioni in cui lo stato di partenza
	 * &egrave; impostato a null
	 * @param a
	 * @param s
	 * @param n
	 * @param guasto
	 * @param o evento osservabile
	 * @return
	 */
	public static Set<Transizione> find(Automa a, Object s, int n, boolean guasto, Evento ot) {
		System.out.println(n);
		Set<Transizione> triplette = new HashSet<Transizione>();
		//Set<Transizione> transizioni=a.getTransizioni();
		Set<Transizione> transizioni=a.getTransizioniUscenti(s);
		if(!transizioni.isEmpty()){
			for (Transizione t: transizioni){
				boolean _guasto;
				Evento o = t.getEvento();
				Object _s = t.getStatoArrivo();
				
				if(t.isGuasto()){
					_guasto = true;
				} else {
					_guasto = guasto;
				}
				
				int cardinalita = o.cardinalita();
				if(!t.nonOsservabile() && cardinalita <= n){
					Transizione tripletta;
					Evento eventoComposto = new Evento();
					eventoComposto.add(o.getSetEventiSemplici());
					eventoComposto.add(ot.getSetEventiSemplici());
					if (cardinalita == n){
						tripletta = new Transizione (null, _s, eventoComposto, _guasto);
						triplette.add(tripletta);
					} else {
						triplette.addAll((Set<Transizione>) find(a, _s, n-cardinalita, _guasto, eventoComposto));
					}
				}
				if(t.nonOsservabile()){
					triplette.addAll((Set<Transizione>) find(a, _s, n, _guasto, ot));
				}
				
			}
		}
		
		
		
		return triplette;
	}

}
