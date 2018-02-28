package info.kuee.hinelet.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Run web service without stub
 * 
 * In this example we are going to run a web service without a client stub.
 * 
 * @author pango
 * @see REF: <a href="https://www.zaneli.com/blog/20111228">ザネリは列車を見送った's 
 *      SOAP Webサービスクライアントを作ろう(JAX-WS, Apache CXF 編)</a>
 */
public class JAXWSExecutor {

	private static final String WSDL_URI = System.getenv("HINEMOS_MANGER_URL")+"AccessEndpoint?wsdl";
	private static final String HINEMOS_USER = System.getenv("HINEMOS_USER");
	private static final String HINEMOS_PASS = System.getenv("HINEMOS_PASS");

	private static final String NAMESPACE = "http://access.ws.clustercontrol.com";
	private static final QName SERVICE_NAME = new QName(NAMESPACE, "AccessEndpointService");
	private static final QName PORT_NAME = new QName(NAMESPACE, "AccessEndpointPort");
	private static final String OPERATION_NAME = "checkLogin";
	private static final String RESPONSE_BODY_NAME = OPERATION_NAME + "Response";

	public String checkLogin() throws ParserConfigurationException, SAXException, IOException,
			XMLStreamException, FactoryConfigurationError, TransformerException {
		URL wsdlURL = new URL(WSDL_URI);
		Service service = Service.create(wsdlURL, SERVICE_NAME);
		Dispatch<Source> disp = service.createDispatch(PORT_NAME, Source.class, Service.Mode.PAYLOAD);

		//Map<String, Object> map = ((BindingProvider)service).getRequestContext();
		Map<String, Object> map = disp.getRequestContext();

		map.put(Dispatch.SOAPACTION_URI_PROPERTY, NAMESPACE + OPERATION_NAME);
		// set credentials
		//client.header("Authorization", "Basic " + (new BASE64Encoder().encode((name + ":" + password).getBytes())));		map.put(BindingProvider.USERNAME_PROPERTY, HINEMOS_USER);
		map.put(BindingProvider.USERNAME_PROPERTY, HINEMOS_USER);
		map.put(BindingProvider.PASSWORD_PROPERTY, HINEMOS_PASS);

		Source request = createRequestSource();
		Source response = disp.invoke(request);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		ResponseHandler handler = new ResponseHandler();
		Result result = new SAXResult(handler);
		transformer.transform(response, result);
		return handler.getResponse();
	}

	private SAXSource createRequestSource() throws IOException {
		InputStream in = null;
		try {
			String reqBody = "<checkLogin xmlns=\"" + NAMESPACE + "\"></checkLogin>";
			in = new ByteArrayInputStream(reqBody.getBytes("UTF-8"));
			return new SAXSource(new InputSource(in));
		} finally {
			if (in != null)
				in.close();
		}
	}

	private static class ResponseHandler extends DefaultHandler {
		private boolean isGetMessageResultStarted;
		private String response;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			isGetMessageResultStarted = NAMESPACE.equals(uri) && RESPONSE_BODY_NAME.equals(localName);
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (isGetMessageResultStarted) {
				response = new String(ch, start, length);
			}
		}

		public String getResponse() {
			return response;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new JAXWSExecutor().checkLogin());
	}
}