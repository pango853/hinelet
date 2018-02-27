package info.kuee.hinelet.example;

import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.clustercontrol.ws.access.ManagerInfo;

/**
 * Set the following environment variables before running this program
 * 
 * 	SET HINEMOS_MANGER_URL=http://xxx.xxx.xxx.xxx:8080/HinemosWS/
 * 	SET HINEMOS_USER=hinemos
 * 	SET HINEMOS_PASS=hinemos
 * 
 * @author pango
 */
public class CXFExecutor {

	private static final String WSDL_URI = System.getenv("HINEMOS_MANGER_URL")+"AccessEndpoint?wsdl";
	private static final String HINEMOS_USER = System.getenv("HINEMOS_USER");
	private static final String HINEMOS_PASS = System.getenv("HINEMOS_PASS");
	private static final String OPERATION_NAME = "checkLogin";

	public ManagerInfo checkLogin() throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();

		System.out.println("createClient: " + WSDL_URI);
		Client client = dcf.createClient(WSDL_URI);

		//String authorizationHeader = "Basic " + org.apache.cxf.common.util.Base64Utility.encode((HINEMOS_USER+":"+HINEMOS_PASS).getBytes());
		//client.header("Authorization", authorizationHeader);
		AuthorizationPolicy authorization = new AuthorizationPolicy();
		authorization.setUserName(HINEMOS_USER);
		authorization.setPassword(HINEMOS_PASS);

		HTTPConduit http = (HTTPConduit) client.getConduit();
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		httpClientPolicy.setConnectionTimeout(6000);
		httpClientPolicy.setAllowChunking(false);
		http.setClient(httpClientPolicy);
		http.setAuthorization(authorization);
		
		Object[] result = client.invoke(OPERATION_NAME);
		if (result.length > 0 && result[0] instanceof ManagerInfo) {
			return (ManagerInfo) result[0];
		}
		throw new IllegalStateException("No response!");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new CXFExecutor().checkLogin());
	}
}
