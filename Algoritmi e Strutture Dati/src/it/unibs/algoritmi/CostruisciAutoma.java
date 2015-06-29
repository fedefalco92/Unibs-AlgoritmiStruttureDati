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
	
/*
	private static Object ottieniStatoIniziale() {
		NodeList stati = radice.getChildNodes();
		for(int i=0;i<stati.getLength();i++){
			System.out.println(stati.item(i));
		}
		//Node statoIniziale = stati.getFirstChild();
		//String nome = statoIniziale.getNodeValue();
		System.out.println(stati);
		return null;
	}*/


	public static void esaminaNodo(Node nodo){
		switch(nodo.getNodeName()){
		case "automa":
		case "stati":
		case "transizioni":
			esaminaFigli(nodo.getChildNodes());
			break;
			
		case "stato": gestisciStato(nodo); break;
		case "transizione": gestisciTransizione(nodo); break;
		}
		/*switch(nodo.getNodeType()){
			case Node.ELEMENT_NODE:
				System.out.println("Elemento: " + nodo.getNodeName());
				//gestisciElementNode(nodo);
				
				NamedNodeMap attributi = nodo.getAttributes();
				if(attributi.getLength()>0){
					System.out.println("\tAttributi: ");
					for(int i=0;i<attributi.getLength();i++){
						Attr attributo = (Attr) attributi.item(i);
						System.out.println(attributo.getNodeName() + " = "+ attributo.getNodeValue()+" ");						
					}
					System.out.println();					
				}
				esaminaFigli(nodo.getChildNodes());
				System.out.println("Fine elemento: " + nodo.getNodeName());
				break;
			case Node.CDATA_SECTION_NODE:
			case Node.TEXT_NODE:
				Text testo = (Text)nodo;
				if(!testo.getNodeValue().trim().equals("")){
					System.out.println("\tTesto: "+testo.getNodeValue());
				}
				break;
		}*/
	}
	
	private static void gestisciTransizione(Node t) {
	// TODO Auto-generated method stub
	
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
				System.out.println(a.getStatoIniziale());
			}
		}	
	}

	/*
	private static void gestisciElementNode(Node nodo) {
		switch(nodo.getNodeName()){
		case "automa": break;
		case "stati": esaminaNodo(nodo); break;
		case "transizioni": esaminaNodo(nodo) break;
		case "stato":  break;
		case "transizione": break;
		
		}
	
	}*/

	public static void esaminaFigli(NodeList figli){
		if(figli.getLength()>0){
			for(int i=0;i<figli.getLength();i++){
				esaminaNodo(figli.item(i));
			}
		}
	}

}
