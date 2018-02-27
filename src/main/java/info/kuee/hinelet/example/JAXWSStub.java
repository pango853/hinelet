package info.kuee.hinelet.example;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.BindingProvider;

import org.restlet.resource.ServerResource;

import com.clustercontrol.ws.access.AccessEndpoint;
import com.clustercontrol.ws.access.AccessEndpointService;
import com.clustercontrol.ws.access.HinemosUnknown_Exception;
import com.clustercontrol.ws.access.InvalidRole_Exception;
import com.clustercontrol.ws.access.InvalidUserPass_Exception;
import com.clustercontrol.ws.access.ManagerInfo;

/**
 * Run service using a client stub
 * 
 * In this example we are going to connect to a web service using a
 * JAX-WS(wsimport) generated client stub.
 * 
 * There are 2 ways to get the client stub.
 * 
 * 
 * 1. Generate yourself by wsimport
 * 
 * First you need start the Hinemos Manager. Here it assumes that you have your
 * Hinemos Manager running on localhost.
 * 
 * > wsimport -keep http://localhost:8080/HinemosWS/AccessEndpoint?wsdl -s
 * src\main\ws
 * 
 * If you do not have wsimport yet, you can,
 * 
 * a. Download a standalone JAX-WS package and use the
 * jaxws-ri/bin/wsimport.(bat|sh) in it.
 * 
 * b. Install a JDK and use the $JAVA_HOME/bin/wsimport(.exe)
 * 
 * In this case you may not need to set HINEMOS_MANGER_URL if you just connect
 * to the same server which you run wsimport against.
 * 
 * 
 * 2. Use the pre-generated in Hinemos Client
 * 
 * a. Download
 * https://github.com/hinemos/hinemos/releases/download/v6.1.0/hinemos-6.1-web-6.1.0-1.el7.x86_64.rpm
 * 
 * b. Extract the rpm package using 7zip or rpm2cpio & cpio c. Again extract the
 * ROOT.war inside d. The you will find ClientWS.jar inside. Place it into lib/
 * folder.
 * 
 * Note that in this case you have to set HINEMOS_MANGER_URL in order to connect
 * properly.
 * 
 * @author pango
 *
 */
public class JAXWSStub extends ServerResource {

	private static final String HINEMOS_MANGER_URL = System.getenv("HINEMOS_MANGER_URL");
	private static final String WSDL_URI = (null!=HINEMOS_MANGER_URL) ? HINEMOS_MANGER_URL+"AccessEndpoint?wsdl" : null;
	private static final String HINEMOS_USER = System.getenv("HINEMOS_USER");
	private static final String HINEMOS_PASS = System.getenv("HINEMOS_PASS");


	private void setBindingProvider(Object o, String url) {
		// Use the BindingProvider's context to set the endpoint
		BindingProvider bp = (BindingProvider)o;

		// Since we already set the endpoint while creating the service
		//if(null!=WSDL_URI)
		//	bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WSDL_URI);

		// By default, you must set the credentials
		if(null!=HINEMOS_USER)
			bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, HINEMOS_USER);
		if(null!=HINEMOS_PASS)
			bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, HINEMOS_PASS);

		// Optional
		//bp.getRequestContext().put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, 8192);
		//bp.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, 3000);
		//((SOAPBinding)bp.getBinding()).setMTOMEnabled(true);
	}

	public ManagerInfo checkLogin() {
		try {
			AccessEndpointService service;
			if(null!=WSDL_URI)
				service = new AccessEndpointService(new URL(WSDL_URI));
			else
				service = new AccessEndpointService();
			AccessEndpoint port = service.getAccessEndpointPort();
			
			setBindingProvider(port, service.getWSDLDocumentLocation().toString());
			return port.checkLogin();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (HinemosUnknown_Exception | InvalidRole_Exception | InvalidUserPass_Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(new JAXWSStub().checkLogin());
	}
}
