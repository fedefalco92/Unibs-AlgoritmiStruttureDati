/**
 * 
 */
package it.unibs.asd;


/**
 * la classe Transizione serve per implementare una transizione dell'automa
 */
public class Transizione {
	
	private Stato statoSorgente, statoDestinazione;
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
		this.statoSorgente = _statoPartenza;
		this.statoDestinazione = _statoArrivo;
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
		this.statoSorgente = _statoPartenza;
		this.statoDestinazione = _statoArrivo;
		this.evento = _value;
		this.guasto = _guasto;
		this.ambigua = false;
	}
	
	/**
	 *  costruttore senza argomenti
	 */
	public Transizione() {
		this.statoSorgente =  null;
		this.statoDestinazione = null;
		this.evento = null;
		this.guasto = false;
		this.ambigua = false;
	}

	public void setGuasto(boolean guasto) {
		this.guasto = guasto;
	}

	public Stato getStatoSorgente() {
		return statoSorgente;
	}

	public Stato getStatoDestinazione() {
		return statoDestinazione;
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
			return (statoSorgente.equals(trans.statoSorgente) && statoDestinazione.equals(trans.statoDestinazione) && evento.equals(trans.evento) && guasto == trans.isGuasto() && ambigua == trans.isAmbigua());		
		  }
		  return false;
	}

	public boolean nonOsservabile(){
		if(evento.getSetEventiSemplici().isEmpty()) return true; else return false;
	}
	
	public int hashCode() {
	    return (statoSorgente+"statoPartenza").hashCode()+(statoDestinazione+"statoArrivo").hashCode()+evento.hashCode()+(guasto ? 1:0) +(ambigua ? 1:0);
	}

	public String toString() {
		return "\n<"+statoSorgente.toString()+", "+statoDestinazione.toString()+"; "+evento.toString()+"; "+((guasto)?"guasto; ":"") + ((ambigua)?"ambigua; ": "")+hashCode()+">";
		//return "\n<"+statoPartenza.toString()+statoPartenza.hashCode()+", "+statoArrivo.toString()+statoArrivo.hashCode()+"; "+value.toString()+"; "+guasto+"; "+hashCode()+">";

	}
	
	public boolean equals(Transizione t){
		//return this.hashCode() == t.hashCode();
		return (statoSorgente.equals(t.statoSorgente) && statoDestinazione.equals(t.statoDestinazione) && evento.equals(t.evento) && guasto == t.isGuasto() && ambigua == t.isAmbigua());
	}

	
}
