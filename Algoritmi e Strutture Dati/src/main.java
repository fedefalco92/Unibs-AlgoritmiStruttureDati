import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.management.*;
import it.unibs.algoritmi.CostruisciAutoma;
import it.unibs.algoritmi.CostruisciFileXML;
import it.unibs.algoritmi.GenerazioneAutoma;
import it.unibs.algoritmi.Metodi;
import it.unibs.algoritmi.Trasformazioni;
import it.unibs.asd.Automa;
import it.unibs.asd.Evento;

/**
 * 
 */

/**
 * @author federicofalcone
 *
 */
public class main {
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		if(true){
			simulazione(5);
			return;

		}

		if(args.length<2){
			System.out.println("ERRORE");
			System.out.println(args[0] + " " + args[1]);
			System.exit(1);
		}
		
		String percorsoFile = args[0];
		
		// Modalita debug
		boolean debug;
		try {
			debug = Boolean.parseBoolean(args[2]);
		} catch (Exception e1) {
			debug = false;
		}
		
		try {
			
			int livelloDiagnosticabilita = Integer.parseInt(args[1]);			
			
			String nomeDir = "./output/time-" + System.currentTimeMillis() + (debug?"-debug":"")+"/";
			File dir = new File(nomeDir);
			dir.mkdir();
			
			String nomeFile = nomeDir + "automa.txt";
			File file = new File(nomeFile);			
			try {
				file.createNewFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			PrintWriter writer = new PrintWriter(file);
			
			//Automa a = CostruisciAutoma.costruisciAutoma(percorsoFile);
			int numeroStati = 10;
			int numeroEventiSemplici = 8;
			double lambda = 3;
			Automa a = GenerazioneAutoma.generaAutoma(numeroStati, numeroEventiSemplici, lambda);
			System.out.println(a);
			writer.println("Numero stati:" + a.numeroStati());
			writer.println("Numero transizioni:" + a.numeroTransizioni());
			writer.println(a);
			writer.println("Parametri costruzione automa:");
			writer.println("\tNumero stati:" + numeroStati);
			writer.println("\tNumero eventi semplici: "+ numeroEventiSemplici);
			writer.println("\tNumero medio di transizioni uscenti da ogni stato: " +  lambda);
			writer.println("Livello di diagnosticabilita' di input: " + livelloDiagnosticabilita);
			writer.println("Prestazioni:");
			writer.flush();
			
			long start, end;
			int livelloMax;
			System.out.println("\n****************************************************************");
			
			/* METODO 1 *******************************************************************************/
			System.out.println("\n#############################");
			System.out.println("Sto eseguendo metodo 1...");
			// Inizio misura tempo
			start = getCpuTime();
			if(debug)
				livelloMax = Metodi.diagnosticabilitaMetodo1debug(a, livelloDiagnosticabilita, nomeDir);
			else 
				livelloMax = Metodi.diagnosticabilitaMetodo1(a, livelloDiagnosticabilita);
			end = getCpuTime();
			// Fine misura tempo
			long alg1 = (end-start);
			System.out.println("\tTempo: " + alg1 + " ns" );
			if(livelloMax == livelloDiagnosticabilita)
				System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				System.out.println("\tLivello max: " + livelloMax);
			writer.println("\tAlgoritmo 1: " + alg1 + " ns");
			if(livelloMax == livelloDiagnosticabilita)
				writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				writer.println("\tLivello max: " + livelloMax);
			writer.flush();
			System.gc();
			
			/* METODO 2 *******************************************************************************/
			System.out.println("\n#############################");
			System.out.println("Sto eseguendo metodo 2...");
			
			// Inizio misura tempo
			start = getCpuTime();
			if(debug)
				livelloMax = Metodi.diagnosticabilitaMetodo2debug(a, livelloDiagnosticabilita, nomeDir);
			else 
				livelloMax = Metodi.diagnosticabilitaMetodo2(a, livelloDiagnosticabilita);
			end = getCpuTime();
			// Fine misura tempo
			
			long alg2 = (end-start);
			System.out.println("\tTempo: " + alg2 + " ns");
			if(livelloMax == livelloDiagnosticabilita)
				System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				System.out.println("\tLivello max: " + livelloMax);
			writer.println("\tAlgoritmo 2: " + alg2 + " ns" );
			if(livelloMax == livelloDiagnosticabilita)
				writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				writer.println("\tLivello max: " + livelloMax);
			writer.flush();
			System.gc();
			
			
			/* METODO 3v1*******************************************************************************/
			System.out.println("\n#############################");
			System.out.println("Sto eseguendo metodo 3v1...");
			// Inizio misura tempo
			start = getCpuTime();
			if(debug)
				livelloMax = Metodi.diagnosticabilitaMetodo3v1debug(a, livelloDiagnosticabilita, nomeDir);
			else 
				livelloMax = Metodi.diagnosticabilitaMetodo3v1(a, livelloDiagnosticabilita);
			end = getCpuTime();
			// Fine misura tempo
			long alg3v1= (end-start);
			System.out.println("\tTempo: " + alg3v1+ " ns");
			if(livelloMax == livelloDiagnosticabilita)
				System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				System.out.println("\tLivello max: " + livelloMax);
			writer.println("\tAlgoritmo 3: " + alg3v1+ " ns");
			if(livelloMax == livelloDiagnosticabilita)
				writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				writer.println("\tLivello max: " + livelloMax);
			writer.flush();
			System.gc();
			
			
			/* METODO 3v2 *******************************************************************************/
			System.out.println("\n#############################");
			System.out.println("Sto eseguendo metodo 3v2...");
			// Inizio misura tempo
			start = getCpuTime();
			if(debug)
				livelloMax = Metodi.diagnosticabilitaMetodo3v2debug(a, livelloDiagnosticabilita, nomeDir);
			else 
				livelloMax = Metodi.diagnosticabilitaMetodo3v2(a, livelloDiagnosticabilita);
			end = getCpuTime();
			// Fine misura tempo
			long alg3v2 = (end-start);
			System.out.println("\tTempo: " + alg3v2 + " ns");
			if(livelloMax == livelloDiagnosticabilita)
				System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				System.out.println("\tLivello max: " + livelloMax);
			writer.println("\tAlgoritmo 3v2: " + alg3v2 + " ns");
			if(livelloMax == livelloDiagnosticabilita)
				writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
			else
				writer.println("\tLivello max: " + livelloMax);
			writer.flush();
			System.gc();
			
			/* FINE ******************/
			writer.close();
			System.out.println("Terminato.");
			
			// Confronto tempi
			if(alg1>alg2){ // 1 impiega di piu' di 2
				if(alg2>alg3v1){ // 2 impiega piu' di 3v1
					if(alg3v1<alg3v2){
						System.out.println("Il metodo 3v1e' il piu' veloce con " + alg3v1);
						writer.println("Il metodo 3v1e' il piu' veloce con " + alg3v1+ "\n");
					} else{
						System.out.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2);
						writer.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2 + "\n");
					}
					
				}else{ // 2 impiega meno di 3v1 e meno di 1
					if(alg2<alg3v2){
						System.out.println("Il metodo 2 e' il piu' veloce con " + alg2);
						writer.println("Il metodo 2 e' il piu' veloce con " + alg2 + "\n");
					} else{
						System.out.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2);
						writer.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2 + "\n");
					}
					
				}
			}else{ // 1 impiega meno di 2
				if(alg1<alg3v1){ // 1 impiega meno di 3v1
					if(alg1<alg3v2){
						System.out.println("Il metodo 1 e' il piu' veloce con " + alg1);
						writer.println("Il metodo 1 e' il piu' veloce con " + alg1 + "\n");
					}else{
						System.out.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2);
						writer.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2 + "\n");
					}
					
				}else{ // 3v1impiega meno di 1
					if (alg3v1<alg3v2) {
						System.out.println("Il metodo 3v1e' il piu' veloce con " + alg3v1);
						writer.println("Il metodo 3v1e' il piu' veloce con " + alg3v1+ "\n");
					} else{
						System.out.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2);
						writer.println("Il metodo 3v2 e' il piu' veloce con " + alg3v2 + "\n");
					}
				}
			}
			
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			System.out.println("Inserire un numero intero");
			System.exit(1);
		}
		
		/*
		Evento e1 = new Evento();
		e1.add("a");
		e1.add("b");
		e1.add("b");
		System.out.println(e1);
		Evento e2 = new Evento();
		e2.add("b");
		e2.add("b");
		e2.add("a");
		System.out.println(e2);
		System.out.println(e1.equals(e2));
		System.out.println(e1.cardinalita());*/
		//AnalisiDOM nuovaAnalisi = new AnalisiDOM("./automi/automa.xml");
		
		//Automa a = CostruisciAutoma.costruisciAutoma("./automi/automa.xml");
		
		
		//System.out.println("L'automa a e':\n" + a);
	    //System.out.println("L'insieme delle transizioni e': \n" + a.getTransizioni());
		//Automa a = CostruisciAutoma.costruisciAutoma(args[1]);
	    
	  

		
		/*Automa a = new Automa();
		a.add("A","B","b", false);
		a.add("B","A","*",false);
		a.add("B","D","*",false);
		a.add("D","D","c",false);
		a.add("A","C","a",false);
		a.add("C","E","d",false);
		a.add("E","F","*",true);
		a.add("F","F","c",false);
		a.add("C","F","*",false);
		System.out.println(a.add("E","A","*",false));
		System.out.println(a.add("E","A","*",false));
		System.out.println(a.add("E","A","*",false));
		
	*/
	    /*System.out.println("L'automa a e':\n" + a);
	    System.out.println("L'insieme delle transizioni e': \n" + a.getTransizioni());
	    */
	    //CostruisciFileXML.automaToXML(a);
	    
	   /*
			System.out
					.println("L'insieme delle transizioni non osservabili e': \n"
							+ a.getTransizioniNonOsservabili());
			System.out.println("L'insieme delle transizioni di guasto e': \n"
					+ a.getTransizioniDiGuasto());
			Automa b = Trasformazioni.inizializzaBadTwin1(a);
			System.out.println("L'automa b e':\n" + b);
			System.out.println("L'insieme delle transizioni e': \n"
					+ b.getTransizioni());
			Automa badtwin1 = Trasformazioni.badtwin0to1(a);
			System.out.println("L'automa bad twin livello 1 e':\n" + badtwin1);
			System.out.println("L'insieme delle transizioni e': \n"
					+ badtwin1.getTransizioni());
			Evento eventoComposto = new Evento();
			Evento o = new Evento("a");
			Evento ot = new Evento("b");
			eventoComposto.add(o.getSetEventiSemplici());
			eventoComposto.add(ot.getSetEventiSemplici());
			System.out.println(eventoComposto);
		*/
		
	    //Automa badtwin1 = Trasformazioni.badtwin0to1(a);
	    //System.out.println("L'automa bad twin livello 1 e':\n" + badtwin1);
		//System.out.println("L'insieme delle transizioni e': \n"+ badtwin1.getTransizioni());
		
		/*Automa goodtwin1 = Trasformazioni.badToGoodTwin(badtwin1);
	    //System.out.println("L'automa good twin livello 1 e':\n" + goodtwin1);
		//System.out.println("L'insieme delle transizioni e': \n"+ goodtwin1.getTransizioni());
		//Set<Object> stati = goodtwin1.getStati();
		//System.out.println(stati);
		
		Automa sincronizzato = Trasformazioni.sincronizzazione(goodtwin1, badtwin1);
		System.out.println("L'automa sincronizzato livello 1 e':\n" + sincronizzato);
		System.out.println("L'insieme degli stati e':\n" + sincronizzato.getStati());
		System.out.println("L'insieme delle transizioni e': \n"+ sincronizzato.getTransizioni());
		System.out.println((sincronizzato.diagnosticabile()?"diagnosticabile\n":"non diagnosticabile\n"));
		if (sincronizzato.diagnosticabile()) {
			int i = 2;
			Automa btiprev = badtwin1;
			Automa gtiprev = goodtwin1;
			while ( sincronizzato.diagnosticabile()){
				System.out.println("*********************************************************************************************************\n");
				Automa bti = Trasformazioni.badtwinLevelUpGenerico(btiprev, i);
				System.out.println("Il bad twin di livello "+i+" e':\n" + bti);
				System.out.println("L'insieme delle transizioni e': \n"+ bti.getTransizioni());
				btiprev =  bti;
				Automa gti = Trasformazioni.badToGoodTwin(bti);
				System.out.println("Il good twin di livello "+i+" e':\n" + gti);
				System.out.println("L'insieme delle transizioni e': \n"+ gti.getTransizioni());
				sincronizzato = Trasformazioni.sincronizzazione(gti, bti);
				System.out.println("*******************************************************************\n");
				System.out.println("L'automa sincronizzato livello "+i+" e':\n");
				System.out.println("L'insieme degli stati e':\n" + sincronizzato.getStati());
				System.out.println("\n"+ sincronizzato);
				System.out.println("L'insieme delle transizioni e': \n"+ sincronizzato.getTransizioni());
				System.out.println("Livello "+i+" "+(sincronizzato.diagnosticabile()?"diagnosticabile\n":"non diagnosticabile\n"));
				i++;
			}
		}*/
	}
	
	/** Get CPU time in nanoseconds. */
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime() : 0L;
	}
	
	public static void simulazione(int livelloDiagnosticabilita) throws FileNotFoundException{
		int contatoreAutomiScartati = 0;
		
		int nstatimin = 5;
		int nstatimax = 15;
		
		int lambdamin = 2;
		int lambdamax = 2;
		
		int neventimin = 3;
		int neventimax = 3;
		
		int niterazionitripletta = 5;
		
		String nomeDir = "./simulazioni/simulazione - " + System.currentTimeMillis() + "/";
		File dir = new File(nomeDir);
		dir.mkdir();
		String nomef = "_tempi_s" + nstatimin + "-" + nstatimax + "_l" + lambdamin + "-" + lambdamax + "_e" + neventimin + "-" + neventimax ;
		File csvfile = new File(nomeDir + nomef);
		try {
			csvfile.createNewFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter writercsv = new PrintWriter(csvfile);
		
		for(int nstati = nstatimin; nstati <= nstatimax; nstati++){
			for(int lambda = lambdamin; lambda <= lambdamax; lambda++){
				for(int neventi = neventimin; neventi <= neventimax; neventi++){
					for(int i = 0; i < niterazionitripletta; i++){
						
						try {
							String nomeFile = nomeDir + "automa_" + "s" + (nstati > 10 ? nstati : "0" + nstati) + "_l"
									+ (lambda > 10 ? lambda : "0" + lambda) + "_e"
									+ (neventi > 10 ? neventi : "0" + neventi) + "_n" + (i > 10 ? i : "0" + i) + ".txt";
							File file = new File(nomeFile);
							try {
								file.createNewFile();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							PrintWriter writer = new PrintWriter(file);
							Automa a = GenerazioneAutoma.generaAutoma(nstati, neventi, lambda);
							//System.out.println(a);
							System.out.println(nomeFile);
							writer.println("Numero stati:" + a.numeroStati());
							writer.println("Numero transizioni:" + a.numeroTransizioni());
							writer.println(a);
							writer.println("Parametri costruzione automa:");
							writer.println("\tNumero stati:" + nstati);
							writer.println("\tNumero eventi semplici: " + neventi);
							writer.println("\tNumero medio di transizioni uscenti da ogni stato: " + lambda);
							writer.println("Livello di diagnosticabilita' di input: " + livelloDiagnosticabilita);
							writer.println("Prestazioni:");
							writer.flush();
							long start, end;
							int livelloMax = 0;
							System.out.println("\n****************************************************************");
							/* METODO 1 *******************************************************************************/
							System.out.println("\n#############################");
							System.out.println("Sto eseguendo metodo 1...");
							// Inizio misura tempo
							start = getCpuTime();
							livelloMax = Metodi.diagnosticabilitaMetodo1(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							long alg1 = (end - start);
							System.out.println("\tTempo: " + alg1 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								System.out.println("\tLivello max: " + livelloMax);
							writer.println("\tAlgoritmo 1: " + alg1 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								writer.println("\tLivello max: " + livelloMax);
							writer.flush();
							System.gc();
							/* METODO 2 *******************************************************************************/
							System.out.println("\n#############################");
							System.out.println("Sto eseguendo metodo 2...");
							// Inizio misura tempo
							start = getCpuTime();
							livelloMax = Metodi.diagnosticabilitaMetodo2(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							long alg2 = (end - start);
							System.out.println("\tTempo: " + alg2 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								System.out.println("\tLivello max: " + livelloMax);
							writer.println("\tAlgoritmo 2: " + alg2 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								writer.println("\tLivello max: " + livelloMax);
							writer.flush();
							System.gc();
							/* METODO 3v1*******************************************************************************/
							System.out.println("\n#############################");
							System.out.println("Sto eseguendo metodo 3v1...");
							// Inizio misura tempo
							start = getCpuTime();
							livelloMax = Metodi.diagnosticabilitaMetodo3v1(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							long alg3v1 = (end - start);
							System.out.println("\tTempo: " + alg3v1 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								System.out.println("\tLivello max: " + livelloMax);
							writer.println("\tAlgoritmo 3: " + alg3v1 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								writer.println("\tLivello max: " + livelloMax);
							writer.flush();
							System.gc();
							/* METODO 3v2 *******************************************************************************/
							System.out.println("\n#############################");
							System.out.println("Sto eseguendo metodo 3v2...");
							// Inizio misura tempo
							start = getCpuTime();
							livelloMax = Metodi.diagnosticabilitaMetodo3v2(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							long alg3v2 = (end - start);
							System.out.println("\tTempo: " + alg3v2 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								System.out.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								System.out.println("\tLivello max: " + livelloMax);
							writer.println("\tAlgoritmo 3v2: " + alg3v2 + " ns");
							if (livelloMax == livelloDiagnosticabilita)
								writer.println("\tLivello " + livelloDiagnosticabilita + " diagnosticabile.");
							else
								writer.println("\tLivello max: " + livelloMax);
							writer.flush();
							System.gc();
							/* FINE ******************/
							writer.close();
							System.out.println("Terminato.");
							
							
							//////////////////////////////////////////////////////////////////////////////////////////////////
							// SCRIVO I TEMPI NEL writercsw 
							///////////////////////////////////////////////////////////////////////////////////////////////////
							
							String riga = nstati + "," + lambda + "," + neventi + "," + livelloMax + "," + alg1 + "," + alg2 + "," + alg3v1 + "," + alg3v2;
							writercsv.println(riga);
							writercsv.flush();
						} catch (StackOverflowError e) {
							System.out.println("Automa non valido ha generato eccezione");
							contatoreAutomiScartati++;
							i--; continue;
						}
						
						
					}
				}
			}
		}
		
		writercsv.close();
		System.out.println("Automi scartati: " + contatoreAutomiScartati);
	}
	

}
