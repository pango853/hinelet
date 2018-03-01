package info.kuee.hinelet;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.routing.Router;

public class NodeApp extends Application {
	public NodeApp() {
		setName("Login Server");
		setDescription("Repository>Login");
	}

	@Override
	public synchronized Restlet createInboundRoot() {
		// Create a root router
		Router router = new Router(getContext());

		// Create the handlers
		Restlet listLet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "Nodes \"...\"";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};

		Restlet nodeLet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				// Print the requested URI path
				String message = "Node \"" + request.getAttributes().get("FID") + "\"";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};

		Restlet propertiesLet = new Restlet(getContext()) {
			@Override
			public void handle(Request request, Response response) {
				// Print the user name of the requested orders
				String message = "Properties of node\"" + request.getAttributes().get("FID") + "\"";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};

		Restlet propertyLet = new Restlet(getContext()) {
			@Override
			public void handle(Request request, Response response) {
				// Print the user name of the requested orders
				String message = "Property \"" + request.getAttributes().get("PID") + "\" for node \""
						+ request.getAttributes().get("FID") + "\"";
				response.setEntity(message, MediaType.TEXT_PLAIN);
			}
		};

		// Attach the handlers to the root router
		router.attach("/node", listLet);
		router.attach("/node/{FID}", nodeLet);
		router.attach("/node/{FID}/properties", propertiesLet);
		router.attach("/node/{FID}/properties/{PID}", propertyLet);

		return router;
	}
}
