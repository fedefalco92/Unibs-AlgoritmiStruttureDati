/**
 * 
 */
package it.unibs.algoritmi;
import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXParseException;

import it.unibs.asd.Automa;

/**
 * 
 */
public class CostruisciAutoma {
	
	private static Document documento;
	private static Element radice;
	private static Automa a = new Automa();
	
	/**
	 * Legge il documento XML contenuto nel file xmlFile e ne inizia la lettura a partire dal nodo radice.
	 * L'avanzare della lettura comporta la costruzione dell'automa a.
	 * 
	 * @param xmlFile stringa contenente il nome del file
	 * @return a oggetto di tipo Automa costruito a partire dal file
	 */
	public static Automa costruisciAutoma(String xmlFile){
		
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			dbf.setValidating(true);
			DocumentBuilder domParser = dbf.newDocumentBuilder();
			//domParser.setErrorHandler(new GestioneErrori());
			documento = domParser.parse(new File(xmlFile));
			radice = documento.getDocumentElement();
			esaminaNodo(radice);

		}
		catch(SAXParseException e){
			System.out.println("Errore di parsing:" + e.getMessage());
			System.exit(1);
		}
		catch (FileNotFoundException e){
			System.out.println("File non trovato");
			System.exit(1);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		//si aggiungono le transizioni con a.add(....)
		
		return a;
	}
	
	/**
	 * Esamina il nodo nodo eseguendo metodi diversi a seconda del tipo di nodo, ottenuto con nodo.getNodeName().
	 * @param nodo
	 */
	private static void esaminaNodo(Node nodo){
		switch(nodo.getNodeName()){
		case "automa":
		case "stati":
		case "transizioni":
			esaminaFigli(nodo.getChildNodes());
			break;
			
		case "stato": 
			gestisciStato(nodo); 
			break;
			
		case "transizione": 
			gestisciTransizione(nodo); 
			break;
		}
	}
	
	/**
	 * Gestisce ogni singola Transizione letta nel file xml.
	 * Aggiunge la Transizione all'automa.
	 * 
	 * @param t
	 */
	private static void gestisciTransizione(Node t) {
		Object statoPartenza = "", statoArrivo = "";
		boolean guasto = false;
		String evento = "*";
		//System.out.println(t);
		
		//gestione attributi
		if(t.hasAttributes()){
			NamedNodeMap attributi =t.getAttributes();
			for(int i=0;i<attributi.getLength();i++){
				Attr attr = (Attr) attributi.item(i);
				switch(attr.getNodeName()){
				case "guasto": 
					if (attr.getNodeValue().equals("true"))
						guasto = true;
					break;
				case "evento":
					evento = attr.getNodeValue();
					break;
				}
			}
			//System.out.println(t);
			
		}
		
		//gestione start e end
		NodeList figli = t.getChildNodes();
		for(int i=0;i<figli.getLength();i++){
			Node figlio = figli.item(i);
			if(figlio.getNodeType() == Node.ELEMENT_NODE){
				switch(figlio.getNodeName()){
				case "start": statoPartenza = figlio.getFirstChild().getNodeValue(); break;
				case "end": statoArrivo = figlio.getFirstChild().getNodeValue(); break;
				}
			}
			
			//System.out.println(figli.item(i));
		}
		
		a.add(statoPartenza, statoArrivo, evento, guasto);
		
	
	}
	
	/**
	 * Gestisce ogni singolo stato letto nel file xml.
	 * Se lo stato &egrave; quello iniziale aggiorna l'automa settando lo stato iniziale.
	 * 
	 * Altrimenti non fa nulla perch&eacute; gli stati verranno inseriti successivamente quando verranno gestite le transizioni.
	 * @param s
	 */
	private static void gestisciStato(Node s) {
		NamedNodeMap attributi = s.getAttributes();
		if(attributi.getLength()>0){
			if(attributi.item(0).getNodeValue().equals("true")){
				a.setStatoIniziale(s.getFirstChild().getNodeValue());
				//System.out.println(a.getStatoIniziale());
			}
		}	
	}

	/**
	 * Richiama sulla NodeList figli il metodo esaminaNodo
	 * @param figli NodeList ottenuta con il metodo .getChildNodes()
	 */
	private static void esaminaFigli(NodeList figli){
		if(figli.getLength()>0){
			for(int i=0;i<figli.getLength();i++){
				esaminaNodo(figli.item(i));
			}
		}
	}

}
