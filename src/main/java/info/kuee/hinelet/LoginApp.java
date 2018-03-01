package info.kuee.hinelet;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.LocalVerifier;
import org.restlet.security.MapVerifier;

public class LoginApp extends Application {
	public LoginApp() {
		setName("Login Server");
		setDescription("Login");
	}

	@Override
	public synchronized Restlet createInboundRoot() {
		// Create a root router
		Router router = new Router(getContext());

		// Create a Guard
		// Attach a guard to secure access to the directory
		ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "Tutorial");
		guard.setVerifier(new TestVerifier());
		// router.attach("/login", guard).setMatchingMode(Template.MODE_STARTS_WITH);
		router.attach("/login", guard).setMatchingMode(Template.MODE_EQUALS);

		// Create the handlers
		Restlet loginLet = new Restlet(getContext()) {
			@Override
			public void handle(Request request, Response response) {
				// Print the user name of the requested orders
				String message = "Logged in!";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};
		Restlet loginUIDLet = new Restlet(getContext()) {
			@Override
			public void handle(Request request, Response response) {
				// Print the user name of the requested orders
				String message = "User \"" + request.getAttributes().get("UID") + "\"";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};
		Restlet loginUIDPASSLet = new Restlet(getContext()) {
			@Override
			public void handle(Request request, Response response) {
				// Print the user name of the requested orders
				String message = "User \"" + request.getAttributes().get("UID") + "\" with password \""
						+ request.getAttributes().get("PASS") + "\"";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};

		// // Create a directory able to expose a hierarchy of files
		// Directory directory = new Directory(getContext(), ROOT_URI);
		// guard.setNext(directory);
		guard.setNext(loginLet);

		// Attach the handlers to the root router
		router.attach("/login/{UID}", loginUIDLet);
		router.attach("/login/{UID}/{PASS}", loginUIDPASSLet);

		return router;
	}

	class TestVerifier extends LocalVerifier {

		@Override
		public char[] getLocalSecret(String identifier) {
			// Create a simple password verifier
			MapVerifier verifier = new MapVerifier();
			verifier.getLocalSecrets().put("hinemos", "hinemos".toCharArray());

			// Could have a look into a database, LDAP directory, etc.
			char[] secretChars = verifier.getLocalSecrets().get(identifier);
			return secretChars;
			//return null;
		}

	}
}
