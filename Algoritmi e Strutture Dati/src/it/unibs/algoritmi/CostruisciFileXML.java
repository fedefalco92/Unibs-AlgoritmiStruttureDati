/**
 * 
 */
package it.unibs.algoritmi;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import it.unibs.asd.Automa;

/**
 * @author Manutenzione
 *
 */
public class CostruisciFileXML {
	
	public static void automaToXML(Automa a) {
		 
        try {
            JAXBContext context = JAXBContext.newInstance(Automa.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
 
            // Write to System.out for debugging
             m.marshal(a, System.out);
 
            // Write to File
            m.marshal(a, new File("./automi/prova_scrittura_automa.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
