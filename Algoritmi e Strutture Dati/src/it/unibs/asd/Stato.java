/**
 * 
 */
package it.unibs.asd;


/**
 * Classe Stato
 *
 */
public class Stato {
String value;
	
	/*
	 * Serve per il metodo costruisciAutoma
	 */

	public Stato(){
		this.value = "";
	}
	public Stato(String value){
		this.value = value;
	}
	
	public void add(String stato){
		value+=stato;
	}
	
	public String toString() {
		return value;
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
			return (value.equalsIgnoreCase(st.toString()));			
		}
		return false;
	}
}
