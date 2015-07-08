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
	
	/**
	 * Inizializza il bad twin clonando a mantenendo le sole transizioni non di guasto e osservabili
	 * @param a
	 * @return
	 */
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
	
	/**
	 * Restituisce il good twin di livello 1 
	 * @param badtwin1 bad twin di livello 1
	 * @return
	 */
	public static Automa goodtwin1(Automa badtwin1){
		Automa gt = new Automa();
		Set<Transizione> transizioniNoGuasto = badtwin1.getTransizioniNonDiGuasto();
		for(Transizione t:transizioniNoGuasto){
				gt.add(t);				
		}		
		return gt;
	}
	
	
	/**
	 * Sincronizza il good twin di livello 1 e il bad twin di livello 1
	 * @param gt good twin
	 * @param bt baad twin
	 * @return
	 */
	public static Automa sincronizzazione1(Automa gt, Automa bt){
		Automa sincronizzato = new Automa();
		//seleziono gli stati del good twin
		Set<Object> stati = gt.getStati();
		for(Object s: stati){
			//seleziono le transizioni uscenti da s nel bad twin
			Set<Transizione> transizioni1 = bt.getTransizioniUscenti(s);
			//seleziono le transizioni uscenti da s nel goodtwin
			Set<Transizione> transizioni2 = gt.getTransizioniUscenti(s);
			
			//cerco tutte le transizioni distinte caratterizzate dallo stesso evento osservabile
			for(Transizione t1: transizioni1){
				for(Transizione t2: transizioni2){
					if(!t1.equals(t2)){
						if(t1.getEvento().equals(t2.getEvento())){
							//se arrivo qui la transizione t è ambigua
							String statoPartenza = (String) t1.getStatoPartenza()+ (String) t2.getStatoPartenza();
							String statoArrivo = (String) t1.getStatoArrivo()+ (String) t2.getStatoArrivo();
							Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(), false);
							
						}
					}
				}		
			}
		}
		return sincronizzato;
	}

}
