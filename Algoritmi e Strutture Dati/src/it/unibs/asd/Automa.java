/**
 * 
 */
package it.unibs.asd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * La classe Automa rappresenta un automa a stati finiti mediante liste di adiacenza.
 * In particolare si e' voluto dare un'implementazione che utilizzasse classi
 * standard di java.util.
 * Di conseguenza:
 * 1. la lista degli stati e' rappresentata da una HashMap per poter accedere agli stati
 *  in tempo costante
 * 2. la lista degli stati adiacenti e' rappresentata da un HashSet di transizioni, in
 * modo tale da poter verificare/accedere allo stato adiacente in tempo costante.
 *
 */

public class Automa {
	
	private HashMap<Stato,Set<Transizione>> stati;
	private int numeroTransizioni;
	private Stato statoIniziale;
	 
	public Automa(){
		this.stati =  new HashMap<Stato,Set<Transizione>>();
		numeroTransizioni = 0;
		statoIniziale=null;
	}

	public Stato getStatoIniziale() {
		return statoIniziale;
	}

	public void setStatoIniziale(Stato statoIniziale) {
		this.statoIniziale = statoIniziale;
	}

	/**
	 * restituisce il numero di stati
	 * 
	 * @return il numero di stati
	 */
	public int numeroStati() {
		return stati.size();
	}
	/**
	 * Restituisce il numero di archi
	 * 
	 * @return il numero di archi
	 */
	public int numeroTransizioni() {
	    return numeroTransizioni;
	}
	
	/**
	   * aggiunge uno stato all'automa con valore x se x non e' presente nell'automa, nulla altrimenti
	   * L'aggiunta di uno stato significa aggiungere la coppia (x, lista) nella HashMap
	   * dove lista e' una HashSet vuota.
	   * 
	   * @param x lo stato da aggiungere
	   */
	  public void add(Stato x) {
	    if (!stati.containsKey(x)) {
	      Set<Transizione> lista = new HashSet<Transizione>();
	      stati.put(x,lista);
	    }
	  }
	  
	  /**
	   * rimuove lo stato x dall'automa se x e' presente nell'automa, 
	   * altrimenti non modifica l'automa.
	   * 
	   * @param x lo stato da rimuovere
	   */
	  public void remove(Stato x) {
	    if (stati.containsKey(x)) {
	      Iterator transizioni = ( (HashSet) stati.get(x) ).iterator();
	      Transizione t;
	      Stato y;
	      while (transizioni.hasNext()) {
	        t = (Transizione) transizioni.next();
	        y = ( t.getStatoSorgente().equals(x) ) ? t.getStatoDestinazione() : t.getStatoSorgente();
	        if ( ((HashSet) stati.get(y)).remove(t) )
	          numeroTransizioni--;
	      }
	      stati.remove(x);
	    }
	  }
	  
	  /**
	   * aggiunge una transizione tra gli stati x e y se tale transizione non &egrave; gi&agrave; presente e restituisce true, 
	   * altrimenti non modifica l'automa e restituisce false. 
	   * 
	   * @param x stato di partenza
	   * @param y stato di arrivo
	   * @param Evento contenente value transizione
	   * @param guasto boolean che indica se la transizione &egrave; di guasto
	   * @return vero se la transizione &egrave; stata aggiunta false altrimenti
	   */
	  public boolean add(Stato x, Stato y, Evento evento, boolean guasto) {
	    boolean flag = false, flag1 = false;
	    /*
	     * se lo stato di partenza non c'� lo aggiungo automaticamente
	     */
	    if (!stati.containsKey(x))
	      add(x);
	    /*
	     * se lo stato di arrivo non c'� lo aggiungo automaticamente
	     */
	    if (!stati.containsKey(y))
	      add(y);
	    Transizione t = new Transizione(x,y,evento,guasto);
	    flag = (stati.get(x) ).add(t);
	    flag1 =(stati.get(y) ).add(t);
	    flag = flag && flag1;
	    if (flag)
	      numeroTransizioni++;
	    return flag;
	  }
	  
	  /**
	   * aggiunge una transizione tra gli stati x e y se tale transizione non &egrave; gi&agrave; presente e restituisce true, 
	   * altrimenti non modifica l'automa e restituisce false. 
	   * 
	   * @param x stato di partenza
	   * @param y stato di arrivo
	   * @param Evento contenente value transizione
	   * @param guasto boolean che indica se la transizione &egrave; di guasto
	   * @return vero se la transizione &egrave; stata aggiunta false altrimenti
	   */
	  public boolean add(Stato x, Stato y, Evento evento, boolean guasto, boolean ambigua) {
	    boolean flag = false, flag1 = false;
	    /*
	     * se lo stato di partenza non c'� lo aggiungo automaticamente
	     */
	    if (!stati.containsKey(x))
	      add(x);
	    /*
	     * se lo stato di arrivo non c'� lo aggiungo automaticamente
	     */
	    if (!stati.containsKey(y))
	      add(y);
	    Transizione t = new Transizione(x,y,evento,guasto, ambigua);
	    flag = (stati.get(x) ).add(t);
	    flag1 =(stati.get(y) ).add(t);
	    flag = flag && flag1;
	    if (flag)
	      numeroTransizioni++;
	    return flag;
	  }

	  /**
	   * Aggiunge la transizione a all'automa se la transizione non e' presente e restituisce true,
	   * altrimenti non modifica l'automa e restituisce false
	   * 
	   * @param t la transizione da aggiungere
	   * @return true se la transizione &egrave; stata aggiunta, false altrimenti
	   */
	  public boolean add(Transizione t) {
	    return add(t.getStatoSorgente(), t.getStatoDestinazione(), t.getEvento(), t.isGuasto(), t.isAmbigua());
	    
	  }

	  /**
	   * Rimuove la transizione tra gli stati  x e y se tale transizione &egrave; presente e restituisce true, 
	   * altrimenti non modifica l'automa e restituisce false. 
	   * 
	   * @param x stato di partenza
	   * @param y stato di arrivo
	   * @param value nome della transizione
	   * @param guasto boolean che indica se la transizione &egrave; di guasto
	   * @return vero se l'arco e' stato rimosso false altrimenti
	   */
	  public boolean remove(Stato x, Stato y, Evento value, boolean guasto) {
	    Transizione t = new Transizione(x,y,value, guasto);
	    return remove(t);
	  }

	  /**
	   * Rimuove la transizione dall'automa se la transizione &egrave; presente e restituisce true,
	   * altrimenti non modifica l'automa e restituisce false
	   * 
	   * @param t la transizione da rimuovere
	   * @return true se la transizione &egrave; stata rimossa, false altrimenti
	   */
	  public boolean remove(Transizione t) {
	    boolean flag = false,  flag1 = false;
	    if (stati.containsKey(t.getStatoSorgente()) && stati.containsKey(t.getStatoDestinazione())) {
	      flag = ( (HashSet) stati.get(t.getStatoSorgente()) ).remove(t);
	      flag1 = ( (HashSet) stati.get(t.getStatoDestinazione()) ).remove(t);
	    }
	    return flag || flag1;
	  }

	  /**
	   * Restituisce l'insieme delle transizioni presenti nell'automa
	   * 
	   * @return l'insieme delle transizioni presenti nell'automa
	   */
	  public Set<Transizione> getTransizioni() {
	    Set<Transizione> setTransizioni= new HashSet<Transizione>();
	    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
	    while (hashSetI.hasNext())
	      setTransizioni.addAll((Set<Transizione>) hashSetI.next());

	    return setTransizioni;
	  }

	  /**
	   * Restituisce l'insieme delle transizioni incidenti sullo stato stato
	   * se stato &egrave; presente nell'automa, null altrimenti 
	   * 
	   * @param stato stato di cui si vuole conoscere l'insieme delle transizioni incidenti
	   * @return l'insieme delle transizioni incidenti sullo stato stato
	   * se stato &egrave; presente nell'automa, null altrimenti
	   */
	  public Set<Transizione> getTransizioni(Stato stato) {
	    if (stati.containsKey(stato)) //se lo stato � presente nell'automa
	      return stati.get(stato); //ritorno il value corrispondente alla chiave, cio� l'insieme (hashset) delle transizioni
	    else
	      return null;
	  }
	  
	  /**
	   * Ritorna le transizioni uscenti dallo stato stato
	   * @param stato
	   * @return
	   */
	  public Set<Transizione> getTransizioniUscenti(Stato stato){
		  Set<Transizione> tstato = getTransizioni(stato);
		  Set<Transizione> tuscenti = new HashSet<Transizione>();
		  for(Transizione t : tstato){
			  if (t.getStatoSorgente().equals(stato)){
				  tuscenti.add(t);
			  }
		  }
		  return tuscenti;
	  }
	  
	  /**
	   * Ritorna le transizioni uscenti (non di guasto) dallo stato stato
	   * @param stato
	   * @return
	   */
	  public Set<Transizione> getTransizioniUscentiNonDiGuasto(Stato stato){
		  Set<Transizione> tstato = getTransizioni(stato);
		  Set<Transizione> tuscenti = new HashSet<Transizione>();
		  for(Transizione t : tstato){
			  if (t.getStatoSorgente().equals(stato)&&!t.isGuasto()){
				  tuscenti.add(t);
			  }
		  }
		  return tuscenti;
	  }

	  /**
	   * Restituisce l'insieme degli stati dell'automa
	   * 
	   * @return l'insieme degli stati dell'automa
	   */
	  public Set<Stato> getStati() {
	    return stati.keySet();
	  }
	  
	  public Set<Transizione> getTransizioniNonOsservabili(){
		  Set<Transizione> setTransizioniNonOsservabili= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (t.nonOsservabile()){
		    			setTransizioniNonOsservabili.add(t);
		    		}
		    	}
		    }

		    return setTransizioniNonOsservabili;
	  }
	  
	  public Set<Transizione> getTransizioniOsservabili(){
		  Set<Transizione> setTransizioniOsservabili= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (!t.nonOsservabile()){
		    			setTransizioniOsservabili.add(t);
		    		}
		    	}
		    }

		    return setTransizioniOsservabili;
	  }
	  
	  public Set<Transizione> getTransizioniDiGuasto(){
		  Set<Transizione> setTransizioniDiGuasto= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (t.isGuasto()){
		    			setTransizioniDiGuasto.add(t);
		    		}
		    	}
		    }

		    return setTransizioniDiGuasto;
	  }
	  
	  public Set<Transizione> getTransizioniDiGuastoOsservabili(){
		  Set<Transizione> setTransizioniDiGuastoOsservabili= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (t.isGuasto()&&!t.nonOsservabile()){
		    			setTransizioniDiGuastoOsservabili.add(t);
		    		}
		    	}
		    }

		    return setTransizioniDiGuastoOsservabili;
	  }
	  
	  public Set<Transizione> getTransizioniNonDiGuastoOsservabili(){
		  Set<Transizione> setTransizioniNonDiGuastoOsservabili= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (!t.isGuasto()&&!t.nonOsservabile()){
		    			setTransizioniNonDiGuastoOsservabili.add(t);
		    		}
		    	}
		    }

		    return setTransizioniNonDiGuastoOsservabili;
	  }
	  
	  public Set<Transizione> getTransizioniNonDiGuasto(){
		  Set<Transizione> setTransizioniNonDiGuasto= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (!t.isGuasto()){
		    			setTransizioniNonDiGuasto.add(t);
		    		}
		    	}
		    }

		    return setTransizioniNonDiGuasto;
	  }
	  
	  public Set<Transizione> getTransizioniAmbigue(){
		  Set<Transizione> setTransizioniAmbigue= new HashSet<Transizione>();
		    Iterator<Set<Transizione>> hashSetI = stati.values().iterator();
		    while (hashSetI.hasNext()){
		    	Set<Transizione> setCorrente = (Set<Transizione>) hashSetI.next();
		    	for(Transizione t : setCorrente){
		    		if (t.isAmbigua()){
		    			setTransizioniAmbigue.add(t);
		    		}
		    	}
		    }

		    return setTransizioniAmbigue;
	  }	  
	  
	  
	  /**
	   * Verifica la condizione di diagnosticabilit&agrave; C1, cio&egrave; se l'insieme della transizioni ambigue &egrave; vuoto.
	   * @return
	   */
	  public boolean diagnosticabilitaC1(){
		  return !(getTransizioniAmbigue().size() > 0);
	  }
	  
	  /**
	   * Verifica la condizione di diagnosticabilit&agrave; C2, cio&egrave; se il bad twin &egrave; deterministico.
	   * Se da uno stesso stato escono due transizioni caratterizzate dallo stesso evento osservabile l'automa &egrave; non deterministico.
	   * Infatti sussiste uno dei seguenti due casi:
	   * 1) le transizioni sono dirette verso stati diversi.
	   * 2) le transizioni sono dirette verso lo stesso stato ma una delle due &egrave; di guasto, l'altra no.
	   * (Dalle precondizioni sappiamo che non &egrave; possibile alcuna alternativa a queste configurazioni).
	   * 
	   * @return true se &egrave; deterministico (e quindi diagnosticabile), false altrimenti.
	   */
	  public boolean diagnosticabilitaC2(){
		  boolean out = true;
		  Set<Stato> stati = getStati();
		  for(Stato s: stati){
			  Set<Transizione> transizioni1 = getTransizioniUscenti(s);
			  Set<Transizione> transizioni2 = new HashSet<Transizione>();
			  transizioni2.addAll(transizioni1);
			  
			  for(Transizione t1: transizioni1){
				  transizioni2.remove(t1);
				  for(Transizione t2:transizioni2){
					  if(t1.getEvento().equals(t2.getEvento())){
						  return false;	  
					  }
				  }
			  }
		  }
		  return out;
	  }
	  
	  public boolean diagnosticabilitaC3(){
		  boolean out = true;
		  
		  Set<Transizione> tosservabiliguasto  = getTransizioniDiGuastoOsservabili();
		  Set<Transizione> tosservabilinonguasto = getTransizioniNonDiGuastoOsservabili();
		  for(Transizione tguasto: tosservabiliguasto){
			  for(Transizione toss: tosservabilinonguasto){
				  if(tguasto.getEvento().equals(toss.getEvento())){
					  return false;
				  }
			  }
		  }
		  return out;
	  }
	  
	  public String toString() {
	    StringBuffer out = new StringBuffer();
	    out.append("Stato iniziale: " + statoIniziale+"\n");
	    out.append("Stati: " + stati.keySet() + "\n");
	    Stato stato;
	    Transizione t;
	    Iterator transizioneI;
	    Iterator statoI = stati.keySet().iterator();
	    while (statoI.hasNext()) {
	      transizioneI = ((Set) stati.get( stato = (Stato) statoI.next() )).iterator();
	      out.append("Stato " + stato.toString() + ": ");
	      while (transizioneI.hasNext()) {
	        t = (Transizione) transizioneI.next();
	        //out.append( ((a.x == nodo ) ? a.y.toString() : a.x.toString()) + "("+a.value.toString()+"), ");
	        out.append(t.toString()+ ", ");
	      }
	      out.append("\n");
	    }
	    
	    out.append("Transizioni: \n"+ getTransizioni());/*
	    for(Transizione t1: getTransizioni()){
	    	out.append("\t" + t1 + "\n");
	    }*/
	    return out.toString();
	  }


}
