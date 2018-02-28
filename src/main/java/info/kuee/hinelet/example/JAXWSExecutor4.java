package info.kuee.hinelet.example;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.xml.sax.SAXException;

/**
 * In this example we will use Using Service.Mode.MESSAGE instead.
 * 
 * @author pango
 *
 */
public class JAXWSExecutor4 {

	private static final String WSDL_URI = System.getenv("HINEMOS_MANGER_URL") + "RepositoryEndpoint?wsdl";
	private static final String HINEMOS_USER = System.getenv("HINEMOS_USER");
	private static final String HINEMOS_PASS = System.getenv("HINEMOS_PASS");

	private static final String NAMESPACE = "http://repository.ws.clustercontrol.com";
	private static final QName SERVICE_NAME = new QName(NAMESPACE, "RepositoryEndpointService");
	private static final QName PORT_NAME = new QName(NAMESPACE, "RepositoryEndpointPort");
	private static final String OPERATION_NAME = "getPlatformList";

	public String getPlatformList() throws ParserConfigurationException, SAXException, IOException,
			XMLStreamException, FactoryConfigurationError, TransformerException, SOAPException {
		URL wsdlURL = new URL(WSDL_URI);
		Service service = Service.create(wsdlURL, SERVICE_NAME);
		Dispatch<SOAPMessage> disp = service.createDispatch(PORT_NAME, SOAPMessage.class, Service.Mode.MESSAGE);

		//Map<String, Object> map = ((BindingProvider)service).getRequestContext();
		Map<String, Object> map = disp.getRequestContext();

		map.put(Dispatch.SOAPACTION_URI_PROPERTY, NAMESPACE + OPERATION_NAME);
		// set credentials
		//client.header("Authorization", "Basic " + (new BASE64Encoder().encode((name + ":" + password).getBytes())));		map.put(BindingProvider.USERNAME_PROPERTY, HINEMOS_USER);
		map.put(BindingProvider.USERNAME_PROPERTY, HINEMOS_USER);
		map.put(BindingProvider.PASSWORD_PROPERTY, HINEMOS_PASS);

		// Create SOAPMessage request
		SOAPMessage request = createRequestSource();

		// Invoke the service endpoint
		SOAPMessage response = disp.invoke(request);

		return response.getSOAPBody().getTextContent();
	}

	private SOAPMessage createRequestSource() throws IOException, SOAPException {
		// compose a request message
		// Create a message.  This example works with the SOAPPART.
		SOAPMessage request = MessageFactory.newInstance().createMessage();
		SOAPBody body = request.getSOAPBody();

		// Construct the message payload.
		//SOAPBodyElement bodyElement = body.addBodyElement(new QName(NAMESPACE, OPERATION_NAME));
		//SOAPElement textElem = bodyElement.addChildElement(new QName("arg0"));
		//textElem.setTextContent(text);
		//SOAPElement numElem = bodyElement.addChildElement(new QName("arg1"));
		//numElem.setTextContent(Integer.toString(num));
		body.addBodyElement(new QName(NAMESPACE, OPERATION_NAME));

		//request.saveChanges();
		return request;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new JAXWSExecutor4().getPlatformList());
	}
}