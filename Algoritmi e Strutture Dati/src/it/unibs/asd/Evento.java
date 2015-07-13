package it.unibs.asd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Evento {
	Multiset<String> value;
	
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
		if(value.containsAll(e.getSetEventiSemplici()) && e.getSetEventiSemplici().containsAll(value) && value.size()==e.cardinalita()){
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

