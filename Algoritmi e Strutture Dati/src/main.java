import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

import it.unibs.algoritmi.CostruisciAutoma;
import it.unibs.algoritmi.GenerazioneAutoma;
import it.unibs.algoritmi.Metodi;
import it.unibs.asd.Automa;
/**
 * Main Class
 *
 */
public class main {
	
	private static final long KBytes = 1024;

	/**
	 * Metodo main
	 * 
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("########################################################");
		System.out.println("##             Analisi di Diagnosticabilita           ##");
		System.out.println("########################################################");
		
		// create the parser
	    CommandLineParser parser = new DefaultParser();
	    
	    // create the Options
	    Options options = new Options();
	    
	    // Option with argument
	    Option automa = Option.builder("a").longOpt("automa").hasArg().argName("file").desc("Inserire il percorso del file xml dell'automa").build();
	    options.addOption(automa);
	    
	    Option livelloDiagnosticabilita = Option.builder("l").hasArg().argName("numeroLivello").required().longOpt("livello").desc("Livello di diagnosticabilita' da verificare").build();
	    options.addOption(livelloDiagnosticabilita);
	    
	    Option simulazione = Option.builder("s").hasArg().argName("Stringa Parametri").longOpt("simulazione").desc("Esegue la simulazione. E' necessario passare la stringa tra apici. \nUn esempio di stringa e' la seguente \"s=5-15 l=3-4 e=4-6 g=0.1 no=0.3 i=10\"").build();
	    options.addOption(simulazione);
	    
	    // Boolean option 
	    options.addOption("v","verbose", false, "Salva files dettagliati dei metodi risolventi");
	    
	 	// automatically generate the help statement
	    HelpFormatter formatter = new HelpFormatter();
	    
	    try {        
			// parse the command line arguments
	        CommandLine cmd = parser.parse( options, args );
	        //formatter.printHelp( "Analisi di Diagnosticabilita", options);
	        
        	int livello = Integer.parseInt(cmd.getOptionValue("l"));
        	
        	if( cmd.hasOption("a") && !cmd.hasOption("s")) {
	            String path = cmd.getOptionValue("a");
	            diagnosticaAutoma(path, livello, cmd.hasOption("v"));
	        } else if (cmd.hasOption("s") && !cmd.hasOption("a")){
	        	boolean controlloParametriOk = true;
	        	
	        	//Parsing parametri Simulazione
	        	int nstatimin=0, nstatimax=0, lambdamin=0, lambdamax=0, neventimin=0, neventimax=0, niterazionitripletta=0;
	        	double pguasto=0.2, pnonosservabile=0.4;
	        	boolean debug;
	        	String stringaParametri = cmd.getOptionValue("s");
	        	StringTokenizer st = new StringTokenizer(stringaParametri," "); // Spazio per i parametri
	        	if(st.countTokens() != 6){
	        		System.out.println("ERRORE! Hai inserito " + st.countTokens() +" parametri!");
	        		System.out.println("Devi inserire una stringa con ESATTAMENTE 6 Parametri.");
	        		controlloParametriOk = false;
	        	}else{
	        		while (st.hasMoreTokens()) {
	        			String [] splittingArray = st.nextToken().split("=");
		                switch (splittingArray[0]) {
						case "s":
							String [] splitStato = splittingArray[1].split("-");
							nstatimin = Integer.parseInt(splitStato[0]);
							nstatimax = Integer.parseInt(splitStato[1]);
							break;
						
						case "l":
							String [] splitLambda = splittingArray[1].split("-");
							lambdamin = Integer.parseInt(splitLambda[0]);
							lambdamax = Integer.parseInt(splitLambda[1]);
							break;
						
						case "e":
							String [] splitEventi = splittingArray[1].split("-");
							neventimin = Integer.parseInt(splitEventi[0]);
							neventimax = Integer.parseInt(splitEventi[1]);
							break;
						
						case "g":
							pguasto = Double.parseDouble(splittingArray[1]);
							break;
							
						case "no":
							pnonosservabile = Double.parseDouble(splittingArray[1]);
							break;
						
						case "i":
							niterazionitripletta = Integer.parseInt(splittingArray[1]);
							break;
							
						default:
							System.out.println("ERRORE! Parametro non riconosciuto.");
							controlloParametriOk = false;
						}
		            }
	        	}
	        	if(controlloParametriOk){
		        	simulazione(livello,nstatimin, nstatimax, lambdamin, lambdamax, neventimin, neventimax, niterazionitripletta, pguasto, pnonosservabile, cmd.hasOption("v"));
	        	}else{
	        		System.out.println();
	        		System.out.println("Riesegui il programma!");
	        		System.out.println("Ti ricordo che per la simulazione devi inserire una stringa con esattamente 6 parametri.");
	        		System.out.println("Esempio stringa: s=5-15 l=3-4 e=4-6 g=0.1 no=0.3 i=10");
	        		System.out.println("Con s=stati, l=lambda, e=eventi_semplici, g=probabilita'_guasto, no=probabilita'_non_osservabile, i=iterazioni.");
	        	}
	        }else{
	        	formatter.printHelp( "Analisi di Diagnosticabilita", options);
	        	System.out.println("Le opzioni -a e -s sono mutuamente esclusive.");
	        }
		}
		catch( ParseException exp ) {
		    System.err.println( "Parsing fallito. Spiegazione: " + exp.getMessage() );
		    formatter.printHelp( "Analisi di Diagnosticabilita", options);
		} catch(NumberFormatException exp){
			System.err.println("Errore ParseInt. Quando e' richiesto un numero devi inserire un numero.");
			formatter.printHelp( "Analisi di Diagnosticabilita", options);
		}

	}
	
	/** Get CPU time in nanoseconds. */
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime() : 0L;
	}
	
	public static long getMemory(){
		MemoryMXBean memoryMXBean=ManagementFactory.getMemoryMXBean();
		MemoryUsage memHeap = memoryMXBean.getHeapMemoryUsage();
		return memHeap.getUsed();
	}
	
	public static void dumpMemoryStats(){
		MemoryMXBean memoryMXBean=ManagementFactory.getMemoryMXBean();
		MemoryUsage memHeap = memoryMXBean.getHeapMemoryUsage();
		MemoryUsage memNonHeap = memoryMXBean.getNonHeapMemoryUsage();
		float utilizationRatio = ((float) memHeap.getUsed()) / ((float) memHeap.getMax());
		System.out.println("Heap Size: " + memHeap.getUsed() / KBytes + " (KB)" + " - " + "Non Heap Size: " + memNonHeap.getUsed() / KBytes  + " (KB)");
	}
	
	public static void diagnosticaAutoma(String percorsoFile, int livelloDiagnosticabilita, boolean debug){	
		System.out.println("**************************************");
		System.out.println("**     Diagnosticabilita Automa     **");
		System.out.println("**************************************");
		System.out.println("Percorso file xml automa: " + percorsoFile);
		System.out.println("Livello di Diagnosticabilita' da verificare: " + livelloDiagnosticabilita);
		System.out.println("Modalita' debug (verbose): " + debug);
		System.out.println("**************************************");
		
		File root = new File("output");
		if(!root.exists()) root.mkdir();
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
		
		PrintWriter writer = null;
		try {
			file.createNewFile();
			writer = new PrintWriter(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Automa a = CostruisciAutoma.costruisciAutoma(percorsoFile);
		System.out.println(a);
		writer.println("Numero stati:" + a.numeroStati());
		writer.println("Numero transizioni:" + a.numeroTransizioni());
		writer.println(a);
		writer.println("Livello di diagnosticabilita' di input: " + livelloDiagnosticabilita);
		writer.println("Prestazioni:");
		writer.flush();
		
		int [] livelliMax = new int [4];
		long start, end;
		int livelloMax = 0;
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
		livelliMax[0] = livelloMax;
		
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
		livelliMax[1] = livelloMax;
		
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
		
		livelliMax[2] = livelloMax;
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
		livelliMax[3] = livelloMax;
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
		
		System.out.println("\n#############################");
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
		System.out.println("Terminato.");
	}
	
	
	public static void simulazione(int livelloDiagnosticabilita, 
			int nstatimin, int nstatimax, 
			int lambdamin, int lambdamax, 
			int neventimin, int neventimax, 
			int niterazionitripletta, 
			double pguasto, double pnonosservabile,
			boolean debug) throws FileNotFoundException{
		System.out.println("**************************************");
		System.out.println("**          Simulazione             **");
		System.out.println("**************************************");
		System.out.println("Parametri Simulazione");
		System.out.println("Livello di Diagnosticabilita': " + livelloDiagnosticabilita);
    	System.out.println("Stati = min: " + nstatimin + " - max: " + nstatimax);
    	System.out.println("Lambda = min: " + lambdamin + " - max: " + lambdamax);
    	System.out.println("Eventi Semplici = min: " + neventimin + " - max " + neventimax);
    	System.out.println("Iterazioni = " + niterazionitripletta);
    	System.out.println("Probabilita' di Guasto = " + pguasto);
    	System.out.println("Probabilita' evento Non osservabile = " + pnonosservabile);
		System.out.println("Modalita' debug (verbose): " + debug);
    	System.out.println("**************************************");
		
		int contatoreAutomiScartati = 0;
		
		/*
		int nstatimin = 5;
		int nstatimax = 15;
		
		int lambdamin = 2;
		int lambdamax = 2;
		
		int neventimin = 3;
		int neventimax = 3;
		
		int niterazionitripletta = 10;
		*/
		
		File root = new File("simulazioni");
		if(!root.exists()) root.mkdir();
		String nomeDir = "./simulazioni/simulazione - " + System.currentTimeMillis() + "/";
		File dir = new File(nomeDir);
		dir.mkdir();

		/*
		String nomeDirErrori = nomeDir + "AutomiErrati/";
		File dirErrori = new File(nomeDirErrori);
		dirErrori.mkdir();
		*/
		
		String nomef = "_tempi_s" + nstatimin + "-" + nstatimax + 
				"_l" + lambdamin + "-" + lambdamax + 
				"_e" + neventimin + "-" + neventimax + 
				"_g" + Double.toString(pguasto).substring(Double.toString(pguasto).indexOf('.')+1) + 
				"_no" + Double.toString(pnonosservabile).substring(Double.toString(pnonosservabile).indexOf('.')+1) + 
				".csv" ;
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
							/*String nomeDirFile =  "automa_" + "s" + (nstati > 10 ? nstati : "0" + nstati) + "_l"
									+ (lambda > 10 ? lambda : "0" + lambda) + "_e"
									+ (neventi > 10 ? neventi : "0" + neventi) + "_n" + (i > 10 ? i : "0" + i) +
									"_g" + Double.toString(pguasto).substring(Double.toString(pguasto).indexOf('.')+1) + 
									"_no" + Double.toString(pnonosservabile).substring(Double.toString(pnonosservabile).indexOf('.')+1) + 
									".txt";*/
							
							String nomeDirFile =  "automa_" 
									+ "s" + (nstati >= 10 ? nstati : "0" + nstati) 
									+ "_l" + (lambda >= 10 ? lambda : "0" + lambda) 
									+ "_e" + (neventi >= 10 ? neventi : "0" + neventi)
									+ "_g" + Double.toString(pguasto).substring(Double.toString(pguasto).indexOf('.')+1) 
									+ "_no" + Double.toString(pnonosservabile).substring(Double.toString(pnonosservabile).indexOf('.')+1) 
									+ "_i" + (i >= 10 ? i : "0" + i)
									+ "/";
							
							File fileDirFile = new File(nomeDir + nomeDirFile);
							File file = new File(nomeDir + nomeDirFile + "automa.txt");
							try {
								fileDirFile.mkdir();
								file.createNewFile();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							PrintWriter writer = new PrintWriter(file);
							Automa a = GenerazioneAutoma.generaAutoma(nstati, neventi, lambda, pguasto, pnonosservabile);
							//System.out.println(a);
							writer.println("Numero stati:" + a.numeroStati());
							writer.println("Numero transizioni:" + a.numeroTransizioni());
							writer.println(a);
							writer.println("Parametri costruzione automa:");
							writer.println("\tNumero stati:" + nstati);
							writer.println("\tNumero eventi semplici: " + neventi);
							writer.println("\tNumero medio di transizioni uscenti da ogni stato: " + lambda);
							writer.println("Probabilita' di guasto" + pguasto);
							writer.println("Probabilita' di non osservabilita'" + pnonosservabile);
							writer.println("Livello di diagnosticabilita' di input: " + livelloDiagnosticabilita);
							writer.println("Prestazioni:");
							writer.flush();
							long start, end;
							Set<Integer> livelliMax = new HashSet<Integer>();
							int livelloMax = 0;
							System.out.println("\n****************************************************************");
							System.out.println(nomeDirFile);
							
							/* METODO 1 *******************************************************************************/
							System.out.println("\n#############################");
							System.out.println("Sto eseguendo metodo 1...");
							// Inizio misura tempo
							start = getCpuTime();
							//livelloMax = Metodi.diagnosticabilitaMetodo1(a, livelloDiagnosticabilita);
							if(debug)
								livelloMax = Metodi.diagnosticabilitaMetodo1debug(a, livelloDiagnosticabilita, nomeDir + nomeDirFile);
							else 
								livelloMax = Metodi.diagnosticabilitaMetodo1(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							livelliMax.add(livelloMax);
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
							//livelloMax = Metodi.diagnosticabilitaMetodo2(a, livelloDiagnosticabilita);
							if(debug)
								livelloMax = Metodi.diagnosticabilitaMetodo2debug(a, livelloDiagnosticabilita, nomeDir + nomeDirFile);
							else 
								livelloMax = Metodi.diagnosticabilitaMetodo2(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							livelliMax.add(livelloMax);
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
							//livelloMax = Metodi.diagnosticabilitaMetodo3v1(a, livelloDiagnosticabilita);
							if(debug)
								livelloMax = Metodi.diagnosticabilitaMetodo3v1debug(a, livelloDiagnosticabilita, nomeDir + nomeDirFile);
							else 
								livelloMax = Metodi.diagnosticabilitaMetodo3v1(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							livelliMax.add(livelloMax);
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
							//livelloMax = Metodi.diagnosticabilitaMetodo3v2(a, livelloDiagnosticabilita);
							if(debug)
								livelloMax = Metodi.diagnosticabilitaMetodo3v2debug(a, livelloDiagnosticabilita, nomeDir + nomeDirFile);
							else 
								livelloMax = Metodi.diagnosticabilitaMetodo3v2(a, livelloDiagnosticabilita);
							end = getCpuTime();
							// Fine misura tempo
							livelliMax.add(livelloMax);
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
							
							/*
							if(livelliMax.size()>1){
								//i risultati dei tre metodi non coincidono D:
								File automaErrato = new File(nomeDirErrori + nomeFile);
								try {
									automaErrato.createNewFile();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								PrintWriter writerErrore = new PrintWriter(automaErrato);
								writerErrore.println("L'automa ha dato risultati diversi per i metodi risolventi. Controlla il file omonimo nella cartella " + nomeDir);
								writerErrore.close();
							} */
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
