/**
 * 
 */
package it.unibs.asd;


/**
 * la classe Transizione serve per implementare una transizione dell'automa
 */
public class Transizione {
	
	protected Object statoPartenza, statoArrivo;
	protected Evento evento;
	protected boolean guasto;

	/**
	 * costruttore con argomenti
	 * 
	 * @param _statoPartenza stato di partenza della transizione
	 * @param _statoArrivo stato di arrivo della transizione
	 * @param _value nome della transizione
	 * @param _guasto boolean che indica se la transizione &egrave; di guasto
	 */
	public Transizione(Object _statoPartenza, Object _statoArrivo, Evento _value, boolean _guasto) {
		this.statoPartenza = _statoPartenza;
		this.statoArrivo = _statoArrivo;
		this.evento = _value;
		this.guasto = _guasto;
	}
	
	/**
	 *  costruttore senza argomenti
	 */
	public Transizione() {
		this.statoPartenza =  null;
		this.statoArrivo = null;
		this.evento = null;
		this.guasto = false;
	}

	public Object getStatoPartenza() {
		return statoPartenza;
	}

	public Object getStatoArrivo() {
		return statoArrivo;
	}

	public Evento getEvento() {
		return evento;
	}
  
	public boolean isGuasto() {
		return guasto;
	}
	
	public boolean equals(Object t) {
		  if (t instanceof Transizione) {
			Transizione trans = (Transizione) t;
			return (statoPartenza.equals(trans.statoPartenza) && statoArrivo.equals(trans.statoArrivo) && evento.equals(trans.evento));		
		  }
		  return false;
	}

	public boolean nonOsservabile(){
		if(evento.getSetEventiSemplici().isEmpty()) return true; else return false;
	}
	
	public int hashCode() {
	    return (statoPartenza+"statoPartenza").hashCode()+(statoArrivo+"statoArrivo").hashCode()+evento.hashCode()+(guasto ? 1:0);
	}

	public String toString() {
		return "\n<"+statoPartenza.toString()+", "+statoArrivo.toString()+"; "+evento.toString()+"; "+guasto+"; "+hashCode()+">";
		//return "\n<"+statoPartenza.toString()+statoPartenza.hashCode()+", "+statoArrivo.toString()+statoArrivo.hashCode()+"; "+value.toString()+"; "+guasto+"; "+hashCode()+">";

	}

	
}
