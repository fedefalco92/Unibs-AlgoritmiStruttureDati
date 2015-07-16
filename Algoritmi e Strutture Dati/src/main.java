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
		
		if(args.length<2){
			System.out.println("ERRORE");
			System.out.println(args[0] + " " + args[1]);
			System.exit(1);
		}
		
		String percorsoFile = args[0];
		try {
			
			int livelloDiagnosticabilita = Integer.parseInt(args[1]);
			String nomeDir = "./output/time-" + System.currentTimeMillis() + "/";
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
			int numeroStati = 5;
			int numeroEventiSemplici = 3;
			double lambda = 2;
			Automa a = GenerazioneAutoma.generaAutoma(numeroStati, numeroEventiSemplici, lambda);
			System.out.println(a);
			writer.println("Numero stati:" + a.numeroStati());
			writer.println("Numero transizioni:" + a.numeroTransizioni());
			writer.println(a);
			writer.println("Parametri costruzione automa:");
			writer.println("\tNumero stati:" + numeroStati);
			writer.println("\tNumero eventi semplici: "+ numeroEventiSemplici);
			writer.println("\tNumero medio di transizioni uscenti da ogni stato: " +  lambda);
			writer.flush();
			
			long start, end;
			boolean diagnosticabile;
			System.out.println("Sto eseguento metodo 1...");
			start = getCpuTime();
			diagnosticabile = Metodi.diagnosticabilitaMetodo1(a, livelloDiagnosticabilita, nomeDir);
			end = getCpuTime();
			long alg1 = (end-start);
			diagnosticabile = Metodi.diagnosticabilitaMetodo1debug(a, livelloDiagnosticabilita, nomeDir);
			System.out.println("\tTempo: " + alg1 + " ns");
			
			System.out.println("Sto eseguento metodo 2...");
			start = getCpuTime();
			diagnosticabile = Metodi.diagnosticabilitaMetodo2(a, livelloDiagnosticabilita, nomeDir);
			end = getCpuTime();
			long alg2 = (end-start);
			System.out.println("\tTempo: " + alg2 + " ns");
			
			System.out.println("Sto eseguento metodo 3...");
			start = getCpuTime();
			diagnosticabile = Metodi.diagnosticabilitaMetodo3(a, livelloDiagnosticabilita, nomeDir);
			end = getCpuTime();
			long alg3 = (end-start);
			System.out.println("\tTempo: " + alg3 + " ns");
			
			writer.println("Prestazioni:");
			writer.println("\tAlgoritmo 1: " + alg1 + " ns");
			writer.println("\tAlgoritmo 2: " + alg2 + " ns");
			writer.println("\tAlgoritmo 3: " + alg3 + " ns");
			writer.close();
			
			
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

}
