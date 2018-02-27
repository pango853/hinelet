package info.kuee.hinelet.example;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class HelloResource extends ServerResource {
	@Get
	public String hello() {
		return "Hello, World!!";
	}

	public static void main(String[] args) throws Exception {
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 8080);
		component.getDefaultHost().attach("/hello", HelloResource.class);
		component.start();
	}
}
