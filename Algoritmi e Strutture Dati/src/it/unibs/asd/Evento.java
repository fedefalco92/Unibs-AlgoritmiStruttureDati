package it.unibs.asd;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * Classe Evento
 *
 */
public class Evento {
	private Multiset<String> value;
	
	public Evento(){
		this.value = HashMultiset.create();
	}
	
	/*
	 * Serve per il metodo costruisciAutoma
	 */
	public Evento(String value){
		this.value =  HashMultiset.create();
		this.value.add(value);
	}
	
	public Multiset<String> getSetEventiSemplici(){
		return value;
	}
	
	public Set<Multiset.Entry<String>> getEntrySetEventiSemplici(){
		return value.entrySet();
	}
	
	public void add(String eventoSemplice){
		value.add(eventoSemplice);
	}
	
	public void add(Multiset<String> eventoComposto){
		value.addAll(eventoComposto);
	}
	
	public int cardinalita(){
		return value.size();
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		Iterator<String> valore = value.iterator();
		while(valore.hasNext()){
			output.append(valore.next());
			if(valore.hasNext()){
				output.append("//");
			}
		}
		
		return output.toString();
	}
	
	public int hashCode(){
		return (this.toString()+"*").hashCode();
	}
	
	/**
	 * confronta gli eventi confrontando i loro Set
	 * @param e
	 * @return
	 */
	public boolean equals(Evento e){
		/*
		if(value.containsAll(e.getSetEventiSemplici()) && e.getSetEventiSemplici().containsAll(value) && value.size()==e.cardinalita()){
			return true;
		} else {
			return false;
		}*//*
		if(value.size()==e.cardinalita()){
			if(value.containsAll(e.getSetEventiSemplici()) && e.getSetEventiSemplici().containsAll(value)){
				return true;
			}
		}
		return false;*/
		
		Set<Multiset.Entry<String>> entrylocale = value.entrySet();
		Set<Multiset.Entry<String>> entrye = e.getEntrySetEventiSemplici();
		
		if(entrylocale.containsAll(entrye) && entrye.containsAll(entrylocale)){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Restituisce vero se l'evento non &grave; composto dalla ripetizione di i eventi semplici tutti uguali
	 * @param evento
	 * @param i TODO
	 * @return
	 */
	public boolean eventoOk(int i) {
		boolean out = true;
		if(!value.isEmpty() && value.size()==i){
			String primo = value.iterator().next();
			if(value.count(primo)==i){
				out = false;
			}
		}
		return out;
	}
}

