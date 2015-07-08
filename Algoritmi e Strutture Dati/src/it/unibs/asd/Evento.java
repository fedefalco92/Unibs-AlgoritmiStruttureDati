package it.unibs.asd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

public class Evento {
	private Set<String> value;
	private double seed;	
	
	public Evento(){
		this.value = new HashSet<String>();
		seed = Math.random();
	}
	
	/*
	 * Serve per il metodo costruisciAutoma
	 */
	public Evento(String value){
		this.value = new HashSet<String>();
		this.value.add(value);
		seed = Math.random();
	}
	
	public Set<String> getSetEventiSemplici(){
		return value;
	}
	public void add(String eventoSemplice){
		value.add(eventoSemplice);
	}
	
	public void add(Set<String> eventoComposto){
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
		return (this.toString()+"*" + seed).hashCode();
	}
}

