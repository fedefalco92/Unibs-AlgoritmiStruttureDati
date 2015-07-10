/**
 * 
 */
package it.unibs.asd;

import java.util.Iterator;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * @author Manutenzione
 *
 */
public class Stato {
Multiset<String> value;
	
	public Stato(){
		this.value = HashMultiset.create();
	}
	
	/*
	 * Serve per il metodo costruisciAutoma
	 */
	public Stato(String value){
		this.value =  HashMultiset.create();
		this.value.add(value);
	}
	
	public Multiset<String> getSetStatiSemplici(){
		return value;
	}
	
	public void add(String statoSemplice){
		value.add(statoSemplice);
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		Iterator<String> valore = value.iterator();
		while(valore.hasNext()){
			output.append(valore.next());
		}
		
		return output.toString();
	}
	
	public int hashCode(){
		return (this.toString()+"ASDASDASD").hashCode();
	}
	
	/**
	 * confronta gli stati confrontando i loro Set
	 * @param s
	 * @return
	 */
	public boolean equals(Object s){
		if (s instanceof Stato){
			Stato st = (Stato) s;
			return (value.containsAll(st.getSetStatiSemplici()) && st.getSetStatiSemplici().containsAll(value));			
		}
		return false;
	}
}
