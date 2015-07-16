/**
 * 
 */
package it.unibs.algoritmi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import it.unibs.asd.Automa;
import it.unibs.asd.Stato;
import it.unibs.asd.Transizione;

/**
 * @author Manutenzione
 *
 */
public class Metodi {
	
	
	public static boolean diagnosticabilitaMetodo1debug(Automa a, int livello, String nomeDir){
		String nomeFile = nomeDir + "metodo1.txt";
		File file = new File(nomeFile);
		PrintWriter writer = null;
		try {
			file.createNewFile();
			writer = new PrintWriter(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println("############################################");
		writer.println("## Analisi di Diagnosticabilita - Metodo 1##");
		writer.println("############################################");
		int i = 1;
		Automa btiprev = a;
		while (i<=livello){
			writer.println("*********************************************************************************************************\n");
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			btiprev =  bti;
			writer.println("Il bad twin di livello "+i+" e':\n" + bti);
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			writer.println("Il good twin di livello "+i+" e':\n" + gti);
			Automa sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
			writer.println("*******************************************************************\n");
			writer.println("L'automa sincronizzato livello "+i+" e':\n" + sincronizzato);
			if(controllaCicli(sincronizzato)){
				System.out.println("Il livello di diagnosticabilita' massimo e' " + (i-1));
				writer.println("Il livello di diagnosticabilita' massimo e' " + (i-1));
				writer.close();
				return false;
			}
			
			writer.println("Livello "+i+" diagnosticabile\n");
			writer.flush();
			i++;
		}
		writer.close();
		return true;		
	}
	public static boolean diagnosticabilitaMetodo1(Automa a, int livello){
		
		int i = 1;
		Automa btiprev = a;
		while (i<=livello){
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			btiprev =  bti;
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			Automa sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
			if(controllaCicli(sincronizzato)){
				System.out.println("Il livello di diagnosticabilita' massimo e' " + (i-1));
				return false;
			}
			i++;
		}
		return true;		
	}
	
	public static boolean diagnosticabilitaMetodo2debug(Automa a, int livello, String nomeDir){
		String nomeFile = nomeDir + "metodo2.txt";
		File file = new File(nomeFile);			
		PrintWriter writer = null;
		try {
			file.createNewFile();
			writer = new PrintWriter(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println("############################################");
		writer.println("## Analisi di Diagnosticabilita - Metodo 2##");
		writer.println("############################################");
		int i = 1;
		Automa btiprev = a;
		while (i<=livello){
			writer.println("*********************************************************************************************************\n");
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			btiprev =  bti;
			writer.println("Il bad twin di livello "+i+" e':\n" + bti);
			if(bti.diagnosticabilitaC2()||bti.diagnosticabilitaC3()){
				writer.println("Livello "+i+ " diagnosticabile\n");
				writer.flush();
				i++;
				continue;	
			}
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			writer.println("Il good twin di livello "+i+" e':\n" + gti);
			Automa sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
			writer.println("*******************************************************************\n");
			writer.println("L'automa sincronizzato livello "+i+" e':\n" +  sincronizzato);
			if(sincronizzato.diagnosticabilitaC1()){
				writer.println("Livello "+i+" diagnosticabile\n");
				writer.flush();
				i++;
				continue;
			}
			
			if (controllaCicli(sincronizzato)) {
				System.out.println("Il livello di diagnosticabilita' massimo e' " + (i - 1));
				writer.println("Il livello di diagnosticabilita' massimo e' " + (i - 1));
				writer.close();
				return false;
			}
			writer.flush();
			i++;
		}
		writer.close();
		return true;		
	}
	
	public static boolean diagnosticabilitaMetodo2(Automa a, int livello){
		int i = 1;
		Automa btiprev = a;
		while (i<=livello){
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			btiprev =  bti;
			if(bti.diagnosticabilitaC2()||bti.diagnosticabilitaC3()){
				i++;
				continue;	
			}
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			Automa sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
			if(sincronizzato.diagnosticabilitaC1()){
				i++;
				continue;
			}
			
			if (controllaCicli(sincronizzato)) {
				System.out.println("Il livello di diagnosticabilita' massimo e' " + (i - 1));
				return false;
			}
			i++;
		}
		return true;		
	}
	/*
	public static boolean diagnosticabilitaMetodo3(Automa a, int livello){
		int i = 1;
		Automa btiprev = a;
		
		Automa badtwin1 = Trasformazioni.badtwin0to1(a);
		if((badtwin1.diagnosticabilitaC2()||badtwin1.diagnosticabilitaC3())&&(livello==1)){
			return true;
		}
		Automa goodtwin1 = Trasformazioni.badToGoodTwin(badtwin1);
		Automa sincronizzato1 = Trasformazioni.sincronizzazione(goodtwin1, badtwin1);
		
		Automa sincronizzatoPrev = sincronizzato1;
		while (i<=livello){
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			Set<Transizione> transizioniAggiunte = transizioniAggiunte(bti, btiprev);
			btiprev =  bti;
			writer.println("Il bad twin di livello "+i+" e':\n" + bti);
			writer.println("L'insieme delle transizioni e': \n"+ bti.getTransizioni());
			if(bti.diagnosticabilitaC2()||bti.diagnosticabilitaC3()){
				writer.println("Livello "+i+ " diagnosticabile\n");
				i++;
				continue;	
			}
			Automa sincronizzato;
			if(i>1){
				sincronizzato = Trasformazioni.sincronizzazioneV2(sincronizzatoPrev, bti, transizioniAggiunte, i);
				
			} else {
				Automa gti = Trasformazioni.badToGoodTwin(bti);
				sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
			}
			sincronizzatoPrev = sincronizzato;
			if(sincronizzato.diagnosticabilitaC1()){
				writer.println("Livello "+i+" diagnosticabile\n");
				i++;
				continue;
			}
			if (controllaCicli(sincronizzato)) {
				writer.println("Il livello di diagnosticabilita' massimo e' " + (i - 1));
				return false;
			}
			
			i++;
			
		
		}
		return true;
	}*/
	
	public static boolean diagnosticabilitaMetodo3debug(Automa a, int livello, String nomeDir){
		String nomeFile = nomeDir + "metodo3.txt";
		File file = new File(nomeFile);			
		PrintWriter writer = null;
		try {
			file.createNewFile();
			writer = new PrintWriter(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println("############################################");
		writer.println("## Analisi di Diagnosticabilita - Metodo 3##");
		writer.println("############################################");
		
		Automa badtwin1 = Trasformazioni.badtwin0to1(a);
		writer.println("Il bad twin di livello 1 e':\n" + badtwin1);
		if((badtwin1.diagnosticabilitaC2()||badtwin1.diagnosticabilitaC3())&&(livello==1)){
			writer.close();
			return true;
		}
		Automa goodtwin1 = Trasformazioni.badToGoodTwin(badtwin1);
		writer.println("Il good twin di livello 1 e':\n" + goodtwin1);
		Automa sincronizzato = Trasformazioni.sincronizzazione(goodtwin1, badtwin1);
		writer.println("L'automa sincronizzato livello 1 e':\n" +  sincronizzato);
		if(sincronizzato.diagnosticabilitaC1()&&(livello==1)){
			writer.close();
			return true;			
		}
		
		if(controllaCicli(sincronizzato)){
			System.out.println("Il livello di diagnosticabilita' massimo e' " + 0);
			writer.println("Il livello di diagnosticabilita' massimo e' " + 0);
			writer.close();
			return false;
		} else if(livello==1) {
			writer.close();
			return true;
		}
		
		//livello dal 2 in poi
		
		int i = 2;
		Automa btiprev = badtwin1;
		Automa sincronizzatoPrev = sincronizzato;
		while (i<=livello){
			writer.println("*********************************************************************************************************\n");
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			if((bti.diagnosticabilitaC2()||bti.diagnosticabilitaC3())){
				writer.println("Livello "+i+" diagnosticabile\n");
				i++;
				writer.flush();
				continue;
			}
			Set<Transizione> transizioniAggiunte = transizioniAggiunte(bti, btiprev);
			btiprev =  bti;
			writer.println("Il bad twin di livello "+i+" e':\n" + bti);
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			writer.println("Il good twin di livello "+i+" e':\n" + gti);
			Automa sincronizzatov2 = Trasformazioni.sincronizzazioneV2(sincronizzatoPrev, bti, transizioniAggiunte, i);
			sincronizzatoPrev = sincronizzatov2;
			writer.println("*******************************************************************\n");
			writer.println("L'automa sincronizzato livello "+i+" e':\n"+sincronizzatov2);
			if(sincronizzatov2.diagnosticabilitaC1()){
				writer.println("Livello "+i+" diagnosticabile\n");
				i++;
				writer.flush();
				continue;
			}
			
			if (controllaCicli(sincronizzatov2)) {
				writer.println("Il livello di diagnosticabilita' massimo e' " + (i - 1));
				writer.close();
				return false;
			}
			writer.flush();
			i++;
		}
		writer.println("Livello "+(i-1)+" diagnosticabile\n");
		writer.close();
		return true;		
	}
	public static boolean diagnosticabilitaMetodo3(Automa a, int livello){
		Automa badtwin1 = Trasformazioni.badtwin0to1(a);
		if((badtwin1.diagnosticabilitaC2()||badtwin1.diagnosticabilitaC3())&&(livello==1)){
			return true;
		}
		Automa goodtwin1 = Trasformazioni.badToGoodTwin(badtwin1);
		Automa sincronizzato = Trasformazioni.sincronizzazione(goodtwin1, badtwin1);
		if(sincronizzato.diagnosticabilitaC1()&&(livello==1)){
			return true;			
		}
		
		if(controllaCicli(sincronizzato)){
			System.out.println("Il livello di diagnosticabilita' massimo e' " + 0);
			return false;
		} else if(livello==1) {
			return true;
		}
		
		//livello dal 2 in poi
		
		int i = 2;
		Automa btiprev = badtwin1;
		Automa sincronizzatoPrev = sincronizzato;
		while (i<=livello){
			Automa bti = Trasformazioni.badtwinLevelUp(btiprev, i);
			if((bti.diagnosticabilitaC2()||bti.diagnosticabilitaC3())){
				i++;
				continue;
			}
			Set<Transizione> transizioniAggiunte = transizioniAggiunte(bti, btiprev);
			btiprev =  bti;
			Automa gti = Trasformazioni.badToGoodTwin(bti);
			Automa sincronizzatov2 = Trasformazioni.sincronizzazioneV2(sincronizzatoPrev, bti, transizioniAggiunte, i);
			sincronizzatoPrev = sincronizzatov2;
			if(sincronizzatov2.diagnosticabilitaC1()){
				i++;
				continue;
			}
			
			if (controllaCicli(sincronizzatov2)) {
				return false;
			}
			i++;
		}
		return true;		
	}
	
	private static Set<Transizione> transizioniAggiunte(Automa bti, Automa btiprev) {
		Set<Transizione> aggiunte = new HashSet<Transizione>();
		aggiunte.addAll(bti.getTransizioni());
		aggiunte.removeAll(btiprev.getTransizioni());
		return aggiunte;
	}

	/**
	 * Controlla se esiste almeno un cammino in cui la prima transizione ambigua &egrave; seguita (anche non immediatamente) da un ciclo (infinito).
	 * (Per definizione la negazione di questa condizione &egrave; la condizione di diagnosticabilit&agrave;)
	 * @return true se la condizione descritta &egrave; vera
	 */
	private static boolean controllaCicli(Automa sincronizzato) {
		Set<Transizione> tambigue = sincronizzato.getTransizioniAmbigue();
		if(!tambigue.isEmpty()){
			for(Transizione ta: tambigue){
				Set<Stato> visitati = new HashSet<Stato>();
				Stato successivo = ta.getStatoDestinazione();
				visitati.add(successivo);
				
				//aggiungo agli stati visitati anche lo stato di partenza di ta (da controllare)
				//intendendo che � possibile che la transizione faccia parte del ciclo.
				Stato precedente = ta.getStatoSorgente();
				visitati.add(precedente);
				
				return visitaRicorsiva(sincronizzato, visitati,successivo);
			}
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
