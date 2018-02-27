package info.kuee.hinelet;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class LoginResource extends ServerResource {
	public static void main(String[] args) throws Exception {
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 10080);
		component.getDefaultHost().attach("/hello", LoginResource.class);
		component.start();
	}

	@Get
	public String hello() {
		return "Hello, World!!";
	}
}
