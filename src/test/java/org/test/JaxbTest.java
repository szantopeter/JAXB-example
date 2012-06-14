/**
 * 
 */
package org.test;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.contact.Contact;
import org.test.request.Request;
import org.test.response.Response;

/**
 * @author Peter Szanto
 */
public class JaxbTest {

	private JAXBContext jaxbContext = null;
	private Unmarshaller unmarshaller = null;
	private Marshaller marshaller = null;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public JaxbTest() {
		try {
			jaxbContext = JAXBContext
					.newInstance("org.test.response:org.test.contact:org.test.request");
			unmarshaller = jaxbContext.createUnmarshaller();
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testResponse() throws JAXBException {
		JaxbTest xmgr = new JaxbTest();

		InputStream fis = this.getClass().getClassLoader().getResourceAsStream("response.xml");

		Object o = xmgr.loadXML(fis);

		Response response = (Response) o;

		for (Contact contact : response.getContacts().getContact()) {
			logger.debug("=== contact ===");
			logger.debug(contact.getFirstname());
			logger.debug(contact.getLastname());
			logger.debug(contact.getEmail());
		}
		
//		Contact c = new Contact();
//		c.setEmail("sdf");
//
//		Contacts contacts = new Contacts();
//		contacts.getContact().add(c);
//		
//		response.setContacts(contacts);
		
		xmgr.genXML(response, System.out);

	}	

	@Test
	public void testRequest() throws JAXBException {
		JaxbTest xmgr = new JaxbTest();

		InputStream fis = this.getClass().getClassLoader().getResourceAsStream("request.xml");

		Object o = xmgr.loadXML(fis);

		Request request= (Request) o;

		for (Contact contact : request.getContacts().getContact()) {
			logger.debug("=== contact ===");
			logger.debug(contact.getFirstname());
			logger.debug(contact.getLastname());
			logger.debug(contact.getEmail());
			
			contact.setEmail(contact.getEmail() + " UPDATED ");
		}
		
		xmgr.genXML(request, System.out);

	}	
	
	public Object loadXML(InputStream istrm) {
		try {
			Object o = unmarshaller.unmarshal(istrm);

			// return ((JAXBElement)o).getValue();
			return o;

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void genXML(Object o, OutputStream os) throws JAXBException {

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		// marshaller.marshal(objectFactory.createItems(items), os);
		marshaller.marshal(o, os);

	}

}
