/**
 * 
 */
package it.unibs.algoritmi;

import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.Iterator;

import it.unibs.asd.Automa;
import it.unibs.asd.Evento;
import it.unibs.asd.Stato;
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
	 * @param i livello. Precondizione  i>=2
	 * @return btup il bad twin di livello i
	 */
	public static Automa badtwinLevelUp(Automa bt, int i){
		return (i==1?Trasformazioni.badtwin0to1(bt):Trasformazioni.badtwinLevelUpGenerico(bt, i));
	}
	
	/**
	 * Trasforma il bad twin di livello i-1 nel bad twin di livello i
	 * con i >= 2
	 * @param bt il bad twin considerato (livello i-1)
	 * @param i livello. Precondizione  i>=2
	 * @return btup il bad twin di livello i
	 */
	public static Automa badtwinLevelUpGenerico(Automa bt, int i){
		Automa btup = inizializzaBadTwin(bt); //bad twin di livello i
		Set<Stato> stati = bt.getStati();
		for (Stato s: stati){
			Set<Transizione> transizioni = bt.getTransizioni(s);
			for(Transizione t : transizioni){
				Set<Transizione> triplette = find(bt,t.getStatoArrivo(),i-t.getEvento().cardinalita(),t.isGuasto(),t.getEvento());
				if(!triplette.isEmpty()){
					for (Transizione tripletta:triplette){
						//controllo if (o non � composto dalla ripetizione di i eventi semplici)
						if (tripletta.getEvento().eventoOk(i)){
							Transizione nuova = new Transizione(t.getStatoPartenza(), tripletta.getStatoArrivo(), tripletta.getEvento(), tripletta.isGuasto());
							btup.add(nuova);
						}
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
	public static Set<Transizione> find(Automa a, Stato s, int n, boolean guasto, Evento ot) {
		Set<Transizione> triplette = new HashSet<Transizione>();
		//Set<Transizione> transizioni=a.getTransizioni();
		Set<Transizione> transizioni=a.getTransizioniUscenti(s);
		if(!transizioni.isEmpty()){
			for (Transizione t: transizioni){
				boolean _guasto;
				Evento o = t.getEvento();
				Stato _s = t.getStatoArrivo();
				
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
						tripletta = new Transizione (new Stato(""), _s, eventoComposto, _guasto);
						triplette.add(tripletta);
					} else {
						Set<Transizione> risultatoChiamataRicorsiva = find(a, _s, n-cardinalita, _guasto, eventoComposto);
						if(!risultatoChiamataRicorsiva.isEmpty())
						triplette.addAll(risultatoChiamataRicorsiva);
					}
				}
				if(t.nonOsservabile()){
					Set<Transizione> risultatoChiamataRicorsiva = find(a, _s, n, _guasto, ot);
					if(!risultatoChiamataRicorsiva.isEmpty())
						triplette.addAll(risultatoChiamataRicorsiva);
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
	public static Automa badToGoodTwin(Automa badtwin1){
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
	public static Automa sincronizzazione(Automa gt, Automa bt){
		Automa sincronizzato = new Automa();
		//Stato statoIniziale = new Stato(gt.getStatoIniziale().toString()+gt.getStatoIniziale().toString());
		Stato statoIniziale = new Stato();
		statoIniziale.add(gt.getStatoIniziale().toString());
		statoIniziale.add(gt.getStatoIniziale().toString());
		sincronizzato.setStatoIniziale(statoIniziale);
		
		//creazione S'' implicita in creazione T''
		Set<Transizione> daSincronizzare = gt.getTransizioni();
		for(Transizione tr : daSincronizzare){
			//Stato _statoPartenza = new Stato(tr.getStatoPartenza().toString() + tr.getStatoPartenza().toString());
			//Stato _statoArrivo = new Stato(tr.getStatoArrivo().toString() + tr.getStatoArrivo().toString());
			Stato _statoPartenza = new Stato();
			_statoPartenza.add(tr.getStatoPartenza().toString()); 
			_statoPartenza.add(tr.getStatoPartenza().toString());
			Stato _statoArrivo = new Stato();
			_statoArrivo.add(tr.getStatoArrivo().toString());
			_statoArrivo.add(tr.getStatoArrivo().toString());
			sincronizzato.add(_statoPartenza, _statoArrivo, tr.getEvento(), tr.isGuasto(), tr.isAmbigua());
		} 
		
		//questa parte viene eseguita solo se l'automa � non deterministico
		
		//salvo S'' (stati dell'automa sincronizzato)
		//Set<Stato> sprev = sincronizzato.getStati();
		Set<Stato> sprev = new HashSet<Stato>();
		sprev.addAll(sincronizzato.getStati());
		
		//seleziono gli stati del good twin
		Set<Stato> stati = gt.getStati();
		//creo un set di transizioni ambigue (da riempire)
		//alternativa : attributo booleano che dice se una transizione � ambigua o no
		for(Stato s: stati){
			//seleziono le transizioni uscenti da s nel bad twin
			Set<Transizione> transizioni1 = bt.getTransizioniUscenti(s);
			//seleziono le transizioni uscenti da s nel goodtwin
			Set<Transizione> transizioni2 = gt.getTransizioniUscenti(s);
			
			//cerco tutte le transizioni distinte caratterizzate dallo stesso evento osservabile
			for(Transizione t1: transizioni1){
				//transizioni2.remove(t1);
				for(Transizione t2: transizioni2){
					if(!t1.equals(t2)){		
						if(t1.getEvento().equals(t2.getEvento()) && t1.getStatoPartenza().equals(t2.getStatoPartenza())){
							//System.out.println("t1: " + t1 + " - t2:"+t2);
							//se arrivo qui la transizione t � ambigua
							/*
							//gli stati di partenza in realt� sono uguali
							Stato statoPartenza = new Stato(t1.getStatoPartenza().toString() + t2.getStatoPartenza().toString());
							//gli stati di partenza in realt� sono uguali
							
							Stato statoArrivo = new Stato(t1.getStatoArrivo().toString() + t2.getStatoArrivo().toString());
							
							*/
							//se arrivo qui la transizione t � ambigua
							Stato statoPartenza = new Stato();
							//gli stati di partenza in realt� sono uguali
							statoPartenza.add(t1.getStatoPartenza().toString());
							statoPartenza.add(t2.getStatoPartenza().toString());
							Stato statoArrivo = new Stato();
							statoArrivo.add(t1.getStatoArrivo().toString());
							statoArrivo.add(t2.getStatoArrivo().toString());
							//da verificare quale stato di guasto mettere in questa transizione
							//Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(), false);
							Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(), t1.isGuasto());
							if(t1.isGuasto()){
								ta.setAmbigua(true);
							}
							sincronizzato.add(ta);						
							
						}
					}
				}		
			}
		}
		
		Set<Stato> ssecondo = new HashSet<Stato>();
		ssecondo.addAll(sincronizzato.getStati());
		
		while(!setUguali(sprev, ssecondo)){
			//Set<Stato> sdiff = ssecondo;
			Set<Stato> sdiff = new HashSet<Stato>();
			sdiff.addAll(ssecondo);		
			sdiff.removeAll(sprev);
			sprev = new HashSet<Stato>();
			sprev.addAll(ssecondo);
			for (Stato sasb : sdiff){
				String sasbstring = sasb.toString();
				Stato sa = new Stato(sasbstring.substring(0,1));
				Stato sb = new Stato(sasbstring.substring(1, 2));
				
				//seleziono le transizioni uscenti da s nel bad twin
				Set<Transizione> transizioni1 = bt.getTransizioniUscenti(sa);
				//seleziono le transizioni uscenti da s nel goodtwin
				Set<Transizione> transizioni2 = gt.getTransizioniUscenti(sb);
				for(Transizione t1: transizioni1){
					//transizioni2.remove(t1);
					for(Transizione t2: transizioni2){
						if (!t1.equals(t2)) {
							if (t1.getEvento().equals(t2.getEvento())) {
								//se arrivo qui la transizione t � ambigua
								Stato statoPartenza = sasb;
								Stato statoArrivo = new Stato();
								statoArrivo.add(t1.getStatoArrivo().toString());
								statoArrivo.add(t2.getStatoArrivo().toString());
								//Stato statoArrivo = new Stato(t1.getStatoArrivo().toString()+t2.getStatoArrivo().toString());
								//da verificare quale stato di guasto mettere in questa transizione
								//Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(), false);
								Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(),
										t1.isGuasto());
								if (t1.isGuasto()) {
									ta.setAmbigua(true);
								}
								sincronizzato.add(ta);
							} 
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
	public static boolean setUguali(Set<Stato> s1, Set<Stato> s2){
		return (s1.containsAll(s2) && s2.containsAll(s1));
	}
	
	/**
	 * Sincronizza bad twin e good twin di livello i a partire non da essi ma 
	 * a partire dall'automa sincronizzato di livello i-1, da Ti (transizioni aggiunte) e dal bad twin di livello i
	 * @param sincronizzatoPrev Automa sincronizzato di livello i-1
	 * @param bti Bad Twin di livello i
	 * @param transizioniAggiunte TODO
	 * @param i TODO
	 * @return L'automa sincronizzato di livello i
	 */
	public static Automa sincronizzazioneV2(Automa sincronizzatoPrev, Automa bti, Set<Transizione> transizioniAggiunte, int i) {
		Automa sincronizzato = inizializzaSincronizzato(sincronizzatoPrev);
		
		Set<Stato> stemp = new HashSet<Stato>();
		stemp.addAll(sincronizzato.getStati());
		
		Set<Stato> sprimo = new HashSet<Stato>();
		sprimo.addAll(sincronizzatoPrev.getStati());
		
		Set<Transizione> transizioniAggiunteNonGuasto = new HashSet<Transizione>();
		for(Transizione t: transizioniAggiunte){
			if(!t.isGuasto()){
				transizioniAggiunteNonGuasto.add(t);
			}
		}
		
		for(Stato sasb: sprimo){ //sasb � uno stato "doppio"
			String sasbstring = sasb.toString();
			Stato sa = new Stato(sasbstring.substring(0,1));
			Stato sb = new Stato(sasbstring.substring(1, 2));
			Set<Transizione> tAggiunteSa = transizioniUscenti(transizioniAggiunte, sa);
			Set<Transizione> tAggiunteSb = transizioniUscenti(transizioniAggiunteNonGuasto, sb);
			for(Transizione t1: tAggiunteSa){
				for(Transizione t2: tAggiunteSb){
					if (!t1.equals(t2)) {
						if((t1.getEvento().equals(t2.getEvento()))&&(t1.getEvento().cardinalita()==i)){ //l'ultima condizione � necessariamente soddisfatta
							Stato _statoPartenza = sasb;
							Stato _statoArrivo = new Stato();
							_statoArrivo.add(t1.getStatoArrivo().toString());
							_statoArrivo.add(t2.getStatoArrivo().toString());
							Transizione ta = new Transizione(_statoPartenza, _statoArrivo, t1.getEvento(), t1.isGuasto());
							if (t1.isGuasto()) {
								ta.setAmbigua(true);
							}
							sincronizzato.add(ta);
						}
					}
				}
			}
		}
		
		Set<Stato> ssecondo = new HashSet<Stato>();
		ssecondo.addAll(sincronizzato.getStati());
		
		while(!setUguali(stemp, ssecondo)){
			Set<Stato> sdiff = new HashSet<Stato>();
			sdiff.addAll(ssecondo);
			sdiff.removeAll(stemp);
			
			stemp = new HashSet<Stato>();
			stemp.addAll(ssecondo);
			
			for (Stato sasb : sdiff){
				String sasbstring = sasb.toString();
				Stato sa = new Stato(sasbstring.substring(0,1));
				Stato sb = new Stato(sasbstring.substring(1, 2));
				
				//seleziono le transizioni uscenti da s nel bad twin
				Set<Transizione> transizioni1 = bti.getTransizioniUscenti(sa);
				//seleziono le transizioni uscenti da s nel goodtwin
				Set<Transizione> transizioni2 = bti.getTransizioniUscentiNonDiGuasto(sb);
				
				for(Transizione t1: transizioni1){
					for(Transizione t2: transizioni2){
						if(t1.getEvento().equals(t2.getEvento())){
							if (!t1.equals(t2)) {
								Stato statoPartenza = sasb;
								Stato statoArrivo = new Stato();
								statoArrivo.add(t1.getStatoArrivo().toString());
								statoArrivo.add(t2.getStatoArrivo().toString());
								Transizione ta = new Transizione(statoPartenza, statoArrivo, t1.getEvento(),t1.isGuasto());
								if (t1.isGuasto()) {
									ta.setAmbigua(true);
								}
								sincronizzato.add(ta);
							}
						}
					}		
				}
			}
			ssecondo = new HashSet<Stato>();
			ssecondo.addAll(sincronizzato.getStati());
		}
		
		return sincronizzato;
	}
	
	/**
	 * Inizializza l'automa sincronizzato, copiando tutte le transizioni (ci&ograve; implica
	 * anche la copia degli stati)
	 * @param sincronizzatoPrev
	 * @return
	 */
	private static Automa inizializzaSincronizzato(Automa sincronizzatoPrev) {
		Automa sincronizzato = new Automa();
		sincronizzato.setStatoIniziale(sincronizzatoPrev.getStatoIniziale());
		Set<Transizione> transizioni = sincronizzatoPrev.getTransizioni();
		for(Transizione t:transizioni){
			sincronizzato.add(t);				
		}		
		return sincronizzato;
	}
	
	/**
	 * Restituisce le transizioni del set transizioni uscenti da s
	 * @param transizioni
	 * @param s
	 * @return
	 */
	private static Set<Transizione> transizioniUscenti(Set<Transizione> transizioni, Stato s){
		Set<Transizione> uscenti = new HashSet<Transizione>();
		for(Transizione t: transizioni){
			if(t.getStatoPartenza().equals(s)){
				uscenti.add(t);
			}
		}
		return uscenti;
	}

}
