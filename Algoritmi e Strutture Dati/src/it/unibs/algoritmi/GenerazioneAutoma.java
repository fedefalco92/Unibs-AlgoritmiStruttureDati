/**
 * 
 */
package it.unibs.algoritmi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import it.unibs.asd.Automa;
import it.unibs.asd.Evento;
import it.unibs.asd.Stato;
import it.unibs.asd.Transizione;

/**
 * Contiene metodi per generare automi in modo casuale per il testing.
 *
 */
public class GenerazioneAutoma {
	
	private static final double PROBABILITA_GUASTO = 0.01;
	private static final double PROBABILITA_NON_OSSERVABILE = 0.01;
	private static final String [] EVENTI_SEMPLICI = new String []{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
	private static final String[] STATI = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O"};
	private static final int NUMERO_MAX_EVENTI = EVENTI_SEMPLICI.length;
	private static final int NUMERO_MAX_STATI = STATI.length;
	
	/**
	 * Genera casualmente un automa in cui:<br>
	 * - il numero di stati &egrave; pari a numeroStati <br>
	 * - il numero di transizioni &egrave; dato da una funzione casuale con distribuzione di poisson <br>
	 * - il numero di eventi semplici &egrave; pari a numeroEventiSemplici
	 * 
	 * CONDIZIONE NECESSARIA: non deve essere presente alcun ciclo nell'automa che non contenga
	 * alcuna transizione osservabile.
	 * 
	 * @param numeroStati
	 * @param numeroEventiSemplici
	 * @param lambda
	 * @return
	 */
	public static Automa generaAutoma_OLD(int numeroStati, int numeroEventiSemplici, double lambda){
		Automa a = new Automa();
		
		if(numeroStati>NUMERO_MAX_STATI) return null;
		//String[] stati = Arrays.copyOfRange(STATI,0,numeroStati);
		/*for(int i = 0; i<stati.length;i++){
			a.add(new Stato(stati[i]));
		}*/
		
		Vector<Stato> stati = new Vector<Stato>();
		for(int i = 0; i<numeroStati;i++){
			stati.add(new Stato(STATI[i]));
		}
		a.setStatoIniziale(stati.firstElement());
		/*
		Vector<Evento> eventiSemplici = new Vector<Evento>();
		for(int i = 0; i<numeroEventiSemplici;i++){
			eventiSemplici.add(new Evento(EVENTI_SEMPLICI[i]));
		}
		*/
		for(Stato s1: stati){
			//ci deve essere almeno una transizione uscente per ogni stato.
			int numeroTransizioniUscenti = 1 + StdRandom.poisson(lambda-1);
			Set<Transizione> aggiunte = new HashSet<Transizione>();
			for(int i = 0; i < numeroTransizioniUscenti; i++){
				int statoRandom = random(0, stati.size());
				Stato s2 = stati.elementAt(statoRandom);
				Evento evento = new Evento();
				double probNonOsservabile = Math.random();
				if(!(probNonOsservabile< PROBABILITA_NON_OSSERVABILE)||s1.equals(s2)){
					int eventoRandom = random(0,numeroEventiSemplici);
					evento.add(EVENTI_SEMPLICI[eventoRandom]);
				}
				double probGuasto = Math.random();
				boolean guasto = (probGuasto<PROBABILITA_GUASTO)? true:false;
				if(guasto){
					evento = new Evento();
				}
				
				Transizione t = new Transizione(s1,s2,evento,guasto);
				if(!aggiunte.contains(t)){
					a.add(t);
					aggiunte.add(t);
				} else i--;
				
				
			}
		}
		/*
		Set<Transizione> tguasto = a.getTransizioniDiGuasto();
		if(a.getTransizioniDiGuasto().isEmpty()){
			Set<Transizione> transizioni = a.getTransizioni();
			Transizione [] tarray = new Transizione [transizioni.size()]; 
			transizioni.toArray(tarray);
			int acaso = random(0,transizioni.size());
			
		}*/
		
		
		if(a.getTransizioniDiGuasto().isEmpty()){
			
			Stato s1 = statoRandom(stati);
			Stato s2 = statoRandom(stati);
			while(s1.equals(s2)){
				s2 = statoRandom(stati);
			}
			Transizione t = new Transizione(s1, s2, new Evento(), true);
			a.add(t);
			
		}
		
		return a;
	}
	
	/**
	 * Genera casualmente un automa in cui:<br>
	 * - il numero di stati &egrave; pari a numeroStati <br>
	 * - il numero di transizioni &egrave; dato da una funzione casuale con distribuzione di poisson <br>
	 * - il numero di eventi semplici &egrave; pari a numeroEventiSemplici
	 * 
	 * CONDIZIONE NECESSARIA: non deve essere presente alcun ciclo nell'automa che non contenga
	 * alcuna transizione osservabile.
	 * 
	 * @param numeroStati
	 * @param numeroEventiSemplici
	 * @param lambda
	 * @return
	 */
	public static Automa generaAutoma(int numeroStati, int numeroEventiSemplici, double lambda){
		if(numeroStati > NUMERO_MAX_STATI ||
				numeroEventiSemplici > NUMERO_MAX_EVENTI) return null;
		Automa a = new Automa();
		HashMap<Stato,Integer> transizioniUscenti = new HashMap<Stato, Integer>();
		//viene deciso il numero di transizioni uscenti da ogni stato
		Vector<Stato> stati = new Vector<Stato>();
		for(int i = 0; i<numeroStati;i++){
			Stato nuovo = new Stato(STATI[i]);
			stati.add(nuovo);
			if (!transizioniUscenti.containsKey(nuovo)) {
				int numeroTransizioniUscenti = 1 + StdRandom.poisson(lambda-1);
			    transizioniUscenti.put(nuovo, numeroTransizioniUscenti);
			}
		}
		a.setStatoIniziale(transizioniUscenti.keySet().iterator().next());
		Stato iniziale = a.getStatoIniziale();
		//transizioni aggiunte in modo ricorsivo
		aggiungiTransizione(a, iniziale, transizioniUscenti, stati, numeroEventiSemplici, PROBABILITA_NON_OSSERVABILE);
		if(a.numeroStati() != numeroStati){
			Set<Stato> statiNonAggiunti = new HashSet<Stato>();
			Vector<Stato> statiAggiunti = new Vector<Stato>();
			for(Stato s: transizioniUscenti.keySet()){
				if(transizioniUscenti.get(s)>0){
					statiNonAggiunti.add(s);
				} else {
					statiAggiunti.addElement(s);
				}
			}
			for(Stato s: statiNonAggiunti){ //sicuramente non è vuoto
				Stato inizialeRandom = statoRandom(statiAggiunti);
				aggiungiTransizioneSingola(a,inizialeRandom, s, numeroEventiSemplici);
				aggiungiTransizione(a,s,transizioniUscenti,stati,numeroEventiSemplici, PROBABILITA_NON_OSSERVABILE);	
			}
		}
		return a;
	}
	
	private static Stato statoRandom(Vector<Stato> stati) {
		int statoRandom = random(0, stati.size());
		return  stati.elementAt(statoRandom);
	}

	private static void aggiungiTransizione(Automa a, Stato s, HashMap<Stato, Integer> transizioniUscenti, Vector<Stato> stati, int numeroEventiSemplici, double pNonOsservabile){
		Set<Transizione> taggiunte = new HashSet<Transizione>();
		
		for(int i = 0; i < transizioniUscenti.get(s);i++){
			boolean guasto = (Math.random()< PROBABILITA_GUASTO)? true:false;
			Evento evento = new Evento();
			Stato dest = statoRandom(stati);
			if(guasto){
				while(s.equals(dest)){
					dest = statoRandom(stati);
				}
			} else {
				boolean nonOsservabile = (Math.random() < pNonOsservabile)? true: false;
				if(!nonOsservabile||s.equals(dest)){
					evento = eventoRandom(numeroEventiSemplici);
				}
				if(nonOsservabile){
						pNonOsservabile = pNonOsservabile/4;						
				} else {
					pNonOsservabile = PROBABILITA_NON_OSSERVABILE;
				}
			}
			Transizione t = new Transizione(s,dest,evento,guasto);
			if(!a.getTransizioni().contains(t)){
				a.add(t);
				taggiunte.add(t);
			} else i--;
		}
		transizioniUscenti.put(s, 0);
		for(Transizione t: taggiunte){
			aggiungiTransizione(a,t.getStatoDestinazione(),transizioniUscenti, stati, numeroEventiSemplici, pNonOsservabile);
		}
		
	}
	
	/**
	 * Aggiunge una transizione da s a dest
	 * PRECONDIZIONE: s diverso da dest
	 * @param a
	 * @param s
	 * @param dest
	 * @param numeroEventiSemplici
	 */
	private static void aggiungiTransizioneSingola(Automa a, Stato s, Stato dest, int numeroEventiSemplici){
		
		double probGuasto = Math.random();
		boolean guasto = (probGuasto<PROBABILITA_GUASTO)? true:false;
		Evento evento = new Evento();
		if(!guasto){
			double probNonOsservabile = Math.random();
			if(!(probNonOsservabile< PROBABILITA_NON_OSSERVABILE)){
				evento = eventoRandom(numeroEventiSemplici);
			}
		}

		Transizione t = new Transizione(s,dest,evento,guasto);
		//if(!a.getTransizioni().contains(t)){
			a.add(t);
		//}
	}

	private static Evento eventoRandom(int numeroEventiSemplici) {
		int eventoRandom = random(0,numeroEventiSemplici);
		Evento evento = new Evento();
		evento.add(EVENTI_SEMPLICI[eventoRandom]);
		return evento;
	}

	public static int random(int min, int max){
		Random r = new Random();
		int out = r.nextInt(max-min);
		out+=min;
		return out;		
	}
	
	/**
	 * Controlla se esiste un ciclo in cui non compaiono transizioni ambigue. In questo caso l'automa non &egrave; valido 
	 * ed &egrave da scartare. 
	 * @return true se l'automa &grave; da scartare.
	 */
	private static boolean controllaCicli(Automa a) {
		Set<Transizione> tnonoss = a.getTransizioniNonOsservabili();
		if(!tnonoss.isEmpty()){
			/*
			for(Transizione t: tnonoss){
				Set<Stato> visitati = new HashSet<Stato>();
				Stato successivo = t.getStatoDestinazione();
				visitati.add(successivo);
				
				//aggiungo agli stati visitati anche lo stato di partenza di ta (da controllare)
				//intendendo che e' possibile che la transizione faccia parte del ciclo.
				Stato precedente = t.getStatoSorgente();
				visitati.add(precedente);
				
				return visitaRicorsiva(a, visitati,successivo);
			}*/
		}
		return false;
	}
	
	
	/**
	 * Visita progressivamente l'automa a partire dallo stato partenza.
	 * Se dallo stato partenza non esce alcuna transizione si &egrave; arrivati in un punto morto.
	 * Se dallo stato partenza esce una transizione che ha come stato di arrivo uno stato gi&agrave; visitato
	 * in precendenza, allora si &egrave; incappati in un ciclo.
	 * @param sincronizzato
	 * @param visitati
	 * @param partenza
	 * @return true se c'&egrave; un ciclo.
	 */
	private static boolean visitaRicorsiva(Automa sincronizzato, Set<Stato> visitati, Stato partenza){
		//writer.println(partenza+" ");
		Set<Transizione> uscenti = sincronizzato.getTransizioniUscenti(partenza);
		if(uscenti.isEmpty()){	
			return false;
		} else {
			for(Transizione tuscente: uscenti){
				Stato statoArrivo = tuscente.getStatoDestinazione();
				if(visitati.contains(statoArrivo)){
					return true;
				}
				visitati.add(statoArrivo);
				return visitaRicorsiva(sincronizzato, visitati, statoArrivo);
						
			}			
		}
		return false;
		
	}

}
