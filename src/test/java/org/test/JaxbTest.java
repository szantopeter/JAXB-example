/**
 * 
 */
package org.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.test.contact.Firstname;
import org.test.contact.Item;
import org.test.contact.Items;
import org.test.response.LegacyTemplateId;
import org.test.response.Response;
import org.test.response.Result;

/**
 * @author pszanto
 * 
 */
public class JaxbTest {

	private JAXBContext jaxbContext = null;
	private Unmarshaller unmarshaller = null;
	private Marshaller marshaller = null;

	public JaxbTest() {
		try {
			jaxbContext = JAXBContext
					.newInstance("org.test.response:org.test.contact");
			unmarshaller = jaxbContext.createUnmarshaller();
			marshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testContact() throws JAXBException {

		JaxbTest xmgr = new JaxbTest();

		InputStream fis = this.getClass().getClassLoader().getResourceAsStream("contact.xml");

		Object o = xmgr.loadXML(fis);

		Items items = (Items) o;

		Iterator<Item> rtItr = items.getItem().iterator();
		while (rtItr.hasNext()) {
			Item item = rtItr.next();

			System.out.println("First Name = "
					+ item.getFirstname().getContent().trim()
					+ "\t\tLast Name = "
					+ item.getLastname().getContent().trim() + "\t\tEmail = "
					+ item.getEmail().getContent().trim());

			Firstname firstName = new Firstname();
			firstName.setContent(item.getFirstname().getContent() + " Changed");
			item.setFirstname(firstName);

		}

		xmgr.genXML(items, System.out);

	}

	@Test
	public void testResponse() throws JAXBException {

		testResponse("response.xml");

	}
	
	@Test
	public void testResponseError() throws JAXBException {
		testResponse("response_error.xml");
	}
	
	public void testResponse(String fileName) throws JAXBException {
		JaxbTest xmgr = new JaxbTest();

		InputStream fis = this.getClass().getClassLoader().getResourceAsStream(fileName);

		Object o = xmgr.loadXML(fis);

		Response response = (Response) o;

		if (((Response) o).getExceptions() != null) {
			System.out.println("This is an error");

		}

		if (((Response) o).getResults() != null) {
			System.out.println("This is a result");

			List<Result> results = response.getResults().getResult();

			for (Result result : results) {
				if (result.getTemplate() != null) {
					LegacyTemplateId lt = new LegacyTemplateId();
					lt.setContent(result.getTemplate()
							.getLegacyTemplateId().getContent()
							+ " Changed");
					result.getTemplate().setLegacyTemplateId(lt);
				}
			}
			xmgr.genXML(o, System.out);

		}

		xmgr.genXML(response, System.out);

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
