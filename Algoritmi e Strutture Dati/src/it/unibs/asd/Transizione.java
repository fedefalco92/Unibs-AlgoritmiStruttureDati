/**
 * 
 */
package it.unibs.asd;


/**
 * la classe Transizione serve per implementare una transizione dell'automa
 */
public class Transizione {
	
	private Stato statoPartenza, statoArrivo;
	private Evento evento;
	private boolean guasto;
	private boolean ambigua;
	
	public boolean isAmbigua() {
		return ambigua;
	}

	public void setAmbigua(boolean ambigua) {
		this.ambigua = ambigua;
	}

	/**
	 * Costruttore con argomenti (compresa ambiguit&agrave;)
	 * @param _statoPartenza
	 * @param _statoArrivo
	 * @param _value
	 * @param _guasto
	 */
	
	public Transizione(Stato _statoPartenza, Stato _statoArrivo, Evento _value, boolean _guasto, boolean _ambigua) {
		this.statoPartenza = _statoPartenza;
		this.statoArrivo = _statoArrivo;
		this.evento = _value;
		this.guasto = _guasto;
		this.ambigua = _ambigua;
	}

	/**
	 * Costruttore con argomenti
	 * 
	 * @param _statoPartenza stato di partenza della transizione
	 * @param _statoArrivo stato di arrivo della transizione
	 * @param _value nome della transizione
	 * @param _guasto boolean che indica se la transizione &egrave; di guasto
	 */
	public Transizione(Stato _statoPartenza, Stato _statoArrivo, Evento _value, boolean _guasto) {
		this.statoPartenza = _statoPartenza;
		this.statoArrivo = _statoArrivo;
		this.evento = _value;
		this.guasto = _guasto;
		this.ambigua = false;
	}
	
	/**
	 *  costruttore senza argomenti
	 */
	public Transizione() {
		this.statoPartenza =  null;
		this.statoArrivo = null;
		this.evento = null;
		this.guasto = false;
		this.ambigua = false;
	}

	public Stato getStatoPartenza() {
		return statoPartenza;
	}

	public Stato getStatoArrivo() {
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
			return (statoPartenza.equals(trans.statoPartenza) && statoArrivo.equals(trans.statoArrivo) && evento.equals(trans.evento) && guasto == trans.isGuasto() && ambigua == trans.isAmbigua());		
		  }
		  return false;
	}

	public boolean nonOsservabile(){
		if(evento.getSetEventiSemplici().isEmpty()) return true; else return false;
	}
	
	public int hashCode() {
	    return (statoPartenza+"statoPartenza").hashCode()+(statoArrivo+"statoArrivo").hashCode()+evento.hashCode()+(guasto ? 1:0) +(ambigua ? 1:0);
	}

	public String toString() {
		return "\n<"+statoPartenza.toString()+", "+statoArrivo.toString()+"; "+evento.toString()+"; "+((guasto)?"guasto; ":"") + ((ambigua)?"ambigua; ": "")+hashCode()+">";
		//return "\n<"+statoPartenza.toString()+statoPartenza.hashCode()+", "+statoArrivo.toString()+statoArrivo.hashCode()+"; "+value.toString()+"; "+guasto+"; "+hashCode()+">";

	}
	
	public boolean equals(Transizione t){
		return this.hashCode() == t.hashCode();
	}

	
}
