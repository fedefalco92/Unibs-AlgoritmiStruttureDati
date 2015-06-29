/**
 * 
 */
package it.unibs.asd;


/**
 * la classe Transizione serve per implementare una transizione dell'automa
 */
public class Transizione {
	
	protected Object statoPartenza, statoArrivo;
	protected Object value;
	protected boolean guasto;

	/**
	 * costruttore con argomenti
	 * 
	 * @param _statoPartenza stato di partenza della transizione
	 * @param _statoArrivo stato di arrivo della transizione
	 * @param _value nome della transizione
	 * @param _guasto boolean che indica se la transizione &egrave; di guasto
	 */
	public Transizione(Object _statoPartenza, Object _statoArrivo, Object _value, boolean _guasto) {
		this.statoPartenza = _statoPartenza;
		this.statoArrivo = _statoArrivo;
		this.value = _value;
		this.guasto = _guasto;
	}
	
	/**
	 *  costruttore senza argomenti
	 */
	public Transizione() {
		this.statoPartenza =  null;
		this.statoArrivo = null;
		this.value = null;
		this.guasto = false;
	}

	public Object getStatoPartenza() {
		return statoPartenza;
	}

	public Object getStatoArrivo() {
		return statoArrivo;
	}

	public Object getValue() {
		return value;
	}
  
	public boolean isGuasto() {
		return guasto;
	}
	
	public boolean equals(Object t) {
		  if (t instanceof Transizione) {
			Transizione trans = (Transizione) t;
			return (statoPartenza.equals(trans.statoPartenza) && statoArrivo.equals(trans.statoArrivo) && value.equals(trans.value));		
		  }
		  return false;
	}

	public int hashCode() {
	    return statoPartenza.hashCode()+statoArrivo.hashCode()+value.hashCode() + Integer.parseInt(guasto + "");
	}

	public String toString() {
		return "<"+statoPartenza.toString()+", "+statoArrivo.toString()+"; "+value.toString()+"; "+guasto+">";
	}

	
}
