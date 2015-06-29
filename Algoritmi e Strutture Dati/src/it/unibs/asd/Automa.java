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
	
	HashMap<Object,Set<Transizione>> stati;
	int numeroTransizioni;
	Object statoIniziale;
	 
	public Automa(){
		this.stati =  new HashMap<Object,Set<Transizione>>();
		numeroTransizioni = 0;
		statoIniziale=null;
	}

	public Object getStatoIniziale() {
		return statoIniziale;
	}

	public void setStatoIniziale(Object statoIniziale) {
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
	  public void add(Object x) {
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
	  public void remove(Object x) {
	    if (stati.containsKey(x)) {
	      Iterator transizioni = ( (HashSet) stati.get(x) ).iterator();
	      Transizione t;
	      Object y;
	      while (transizioni.hasNext()) {
	        t = (Transizione) transizioni.next();
	        y = ( t.getStatoPartenza().equals(x) ) ? t.getStatoArrivo() : t.getStatoPartenza();
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
	   * @param value nome transizione
	   * @param guasto boolean che indica se la transizione &egrave; di guasto
	   * @return vero se la transizione &egrave; stata aggiunta false altrimenti
	   */
	  public boolean add(Object x, Object y, Object value, boolean guasto) {
	    boolean flag = false, flag1 = false;
	    /*
	     * se lo stato di partenza non c'è lo aggiungo automaticamente
	     */
	    if (!stati.containsKey(x))
	      add(x);
	    /*
	     * se lo stato di arrivo non c'è lo aggiungo automaticamente
	     */
	    if (!stati.containsKey(y))
	      add(y);
	    Transizione t = new Transizione(x,y,value,guasto);
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
	    return add(t.getStatoPartenza(), t.getStatoArrivo(), t.getValue(), t.isGuasto());
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
	  public boolean remove(Object x, Object y, Object value, boolean guasto) {
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
	    if (stati.containsKey(t.getStatoPartenza()) && stati.containsKey(t.getStatoArrivo())) {
	      flag = ( (HashSet) stati.get(t.getStatoPartenza()) ).remove(t);
	      flag1 = ( (HashSet) stati.get(t.getStatoArrivo()) ).remove(t);
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
	  public Set<Transizione> getTransizioni(Object stato) {
	    if (stati.containsKey(stato)) //se lo stato è presente nell'automa
	      return stati.get(stato); //ritorno il value corrispondente alla chiave, cioè l'insieme (hashset) delle transizioni
	    else
	      return null;
	  }

	  /**
	   * Restituisce l'insieme degli stati dell'automa
	   * 
	   * @return l'insieme degli stati dell'automa
	   */
	  public Set<Object> getStati() {
	    return stati.keySet();
	  }

	  public String toString() {
	    StringBuffer out = new StringBuffer();
	    Object stato;
	    Transizione t;
	    Iterator transizioneI;
	    Iterator statoI = stati.keySet().iterator();
	    while (statoI.hasNext()) {
	      transizioneI = ((Set) stati.get( stato = statoI.next() )).iterator();
	      out.append("Stato " + stato.toString() + ": ");
	      while (transizioneI.hasNext()) {
	        t = (Transizione) transizioneI.next();
	        //out.append( ((a.x == nodo ) ? a.y.toString() : a.x.toString()) + "("+a.value.toString()+"), ");
	        out.append(t.toString()+ ", ");
	      }
	      out.append("\n");
	    }
	    return out.toString();
	  }


}
