/**
 * 
 */
package it.unibs.algoritmi;

import java.util.Arrays;
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
	
	private static final double PROBABILITA_GUASTO = 0.1;
	private static final double PROBABILITA_NON_OSSERVABILE = 0.3;
	private static final String [] EVENTI_SEMPLICI = new String []{"a","b","c","d","e","f","g"};
	private static final String[] STATI = new String[]{"A","B","C","D","E","F","G","H","I","J"};
	private static final int NUMERO_MAX_EVENTI = EVENTI_SEMPLICI.length;
	private static final int NUMERO_MAX_STATI = STATI.length;
	
	public static Automa generaAutoma(int numeroStati, int numeroEventiSemplici, double lambda){
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
			int numeroTransizioniUscenti = StdRandom.poisson(lambda);
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
				
				Transizione t = new Transizione(s1,s2,evento,guasto);
				if(!aggiunte.contains(t)){
					a.add(t);
					aggiunte.add(t);
				}
				
				
			}
		}
		
		
		return a;
	}

	public static int random(int min, int max){
		Random r = new Random();
		int out = r.nextInt(max-min);
		out+=min;
		return out;		
	}

}
