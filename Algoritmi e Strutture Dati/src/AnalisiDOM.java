/**
 * 
 */
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

/**
 * @author Massi
 *
 */
//Prova di costruzione classe per validazione modello di test

public class AnalisiDOM {
	private Document documento;
	private Element radice;
	
	public AnalisiDOM(String file){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			dbf.setValidating(true);
			DocumentBuilder domParser = dbf.newDocumentBuilder();
			domParser.setErrorHandler(new GestioneErrori());
			documento = domParser.parse(new File(file));
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
	}
	
	public void esaminaNodo(Node nodo){
		switch(nodo.getNodeType()){
			case Node.ELEMENT_NODE:
				System.out.println("Elemento: " + nodo.getNodeName());
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
		}
	}
	
	public void esaminaFigli(NodeList figli){
		if(figli.getLength()>0){
			for(int i=0;i<figli.getLength();i++){
				esaminaNodo(figli.item(i));
			}
		}
	}
}
