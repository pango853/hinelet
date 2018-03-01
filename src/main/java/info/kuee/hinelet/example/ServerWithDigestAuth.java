package info.kuee.hinelet.example;

import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.ext.crypto.DigestAuthenticator;
import org.restlet.representation.StringRepresentation;
import org.restlet.security.MapVerifier;

public class ServerWithDigestAuth{
	public static void main(String[] args) throws Exception {
		DigestAuthenticator guard = new DigestAuthenticator(null, "TestRealm", "mySecretServerKey");

		// Instantiates a Verifier of identifier/secret couples based on a simple Map.
		MapVerifier mapVerifier = new MapVerifier();
		// Load a single static login/secret pair.
		mapVerifier.getLocalSecrets().put("login", "secret".toCharArray());
		guard.setWrappedVerifier(mapVerifier);

		Restlet mylet = new Restlet(){
		    @Override
		    public void handle(Request request, Response response) {
		        response.setEntity(new StringRepresentation("hello, world", MediaType.TEXT_PLAIN));
		    }
	    };

		// Guard the restlet
		guard.setNext(mylet);

		Component component = new Component();  
		component.getServers().add(Protocol.HTTP, 10080);  
		component.getDefaultHost().attachDefault(guard);
		component.start();
	}
}
