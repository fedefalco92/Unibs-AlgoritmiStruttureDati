/**
 * 
 */
package it.unibs.algoritmi;

import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

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
	 * Trasforma il bad twin di livello i-1 nel bad twin di livello i
	 * con i >= 2
	 * @param bt il bad twin considerato (livello i-1)
	 * @param i livello
	 * @return btup il bad twin di livello i
	 */
	public static Automa badtwinlevelup(Automa bt, int i){
		Automa btup = inizializzaBadTwin(bt); //bad twin di livello i
		Set<Object> stati = bt.getStati();
		for (Object s: stati){
			Set<Transizione> transizioni = bt.getTransizioni(s);
			for(Transizione t : transizioni){
				Set<Transizione> triplette = find(bt,t.getStatoArrivo(),i-t.getEvento().cardinalita(),t.isGuasto(),t.getEvento());
				if(!triplette.isEmpty()){
					for (Transizione tripletta:triplette){
						//controllo if (o non è composto dallaripetizione di i iventi semplici)
						btup.add(t.getStatoPartenza(), tripletta.getStatoArrivo(), tripletta.getEvento(), tripletta.isGuasto());
					}					
				}
			}
			
		}
		
		
		return btup;
	}
	
	/**
	 * Inizializza il bad twin clonando a mantenendo le sole transizioni non di guasto e osservabili
	 * @param a
	 * @return
	 */
	public static Automa inizializzaBadTwin1(Automa a) {
		Automa bt = new Automa();
		bt.setStatoIniziale(a.getStatoIniziale());
		Set<Transizione> transizioni = a.getTransizioni();
		for(Transizione t:transizioni){
			if(!t.isGuasto()&&!t.nonOsservabile()){
				bt.add(t);				
			}
		}		
		return bt;
	}
	
	/**
	 * inizializza il bad twin di livello generico i
	 * @param bt livello i-1
	 * @return btup livello i
	 */
	public static Automa inizializzaBadTwin(Automa bt) {
		Automa btup = new Automa();
		btup.setStatoIniziale(bt.getStatoIniziale());
		Set<Transizione> transizioni = bt.getTransizioni();
		for(Transizione t:transizioni){
			btup.add(t);				
		}		
		return btup;
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
		gt.setStatoIniziale(badtwin1.getStatoIniziale());
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
		sincronizzato.setStatoIniziale((String) gt.getStatoIniziale() + (String) gt.getStatoIniziale());
		
		//creazione S'' implicita in creazione T''
		Set<Transizione> daSincronizzare = gt.getTransizioni();
		for(Transizione tr : daSincronizzare){
			String _statoPartenza = (String) tr.getStatoPartenza() + (String) tr.getStatoPartenza();
			String _statoArrivo = (String) tr.getStatoArrivo() + (String) tr.getStatoArrivo();
			sincronizzato.add(_statoPartenza, _statoArrivo, tr.getEvento(), tr.isGuasto());
		} 
		
		//questa parte viene eseguita solo se l'automa è non deterministico
		
		//salvo S'' (stati dell'automa sincronizzato)
		Set<Object> sprev = sincronizzato.getStati();
		
		//seleziono gli stati del good twin
		Set<Object> stati = gt.getStati();
		//creo un set di transizioni ambigue (da riempire)
		//alternativa : attributo booleano che dice se una transizione è ambigua o no
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
							//da verificare quale stato di guasto mettere in questa transizione
							Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(), false);
							if(t1.isGuasto()){
								ta.setAmbigua(true);
							}
							sincronizzato.add(ta);
							
						}
					}
				}		
			}
		}
		
		Set<Object> ssecondo = sincronizzato.getStati();
		while(!setUguali(sprev, ssecondo)){
			Set<Object> sdiff = ssecondo;
			sdiff.removeAll(sprev);
			for (Object sasb : sdiff){
				String sasbstring = (String) sasb;
				String sa = sasbstring.substring(0,1);
				String sb = sasbstring.substring(1, 2);
				
				//seleziono le transizioni uscenti da s nel bad twin
				Set<Transizione> transizioni1 = bt.getTransizioniUscenti(sa);
				//seleziono le transizioni uscenti da s nel goodtwin
				Set<Transizione> transizioni2 = gt.getTransizioniUscenti(sb);
				for(Transizione t1: transizioni1){
					for(Transizione t2: transizioni2){
						if(t1.getEvento().equals(t2.getEvento())){
							//se arrivo qui la transizione t è ambigua
							Object statoPartenza = sasb;
							String statoArrivo = (String) t1.getStatoArrivo()+ (String) t2.getStatoArrivo();
							//da verificare quale stato di guasto mettere in questa transizione
							Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(), false);
							if(t1.isGuasto()){
								ta.setAmbigua(true);
							}
							sincronizzato.add(ta);	
						}
					}		
				}
				
			}
			ssecondo = sincronizzato.getStati();
		}
		return sincronizzato;
	}
	
	/**
	 * Restituisce vero se il set s1 e il set s2 sono uguali (contengono gli stessi elementi)
	 * Usato nell'algoritmo di sincronizzazione per confrontare S" con Sprev
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean setUguali(Set<Object> s1, Set<Object> s2){
		return (s1.containsAll(s2) && s2.containsAll(s1));
	}

}
