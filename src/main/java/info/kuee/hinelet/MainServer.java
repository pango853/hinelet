package info.kuee.hinelet;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class MainServer extends Component {
	private static final int PORT = 10080;

	public MainServer() throws Exception {
		// Set basic properties
		setName("RESTful Hinemos Client");
		setDescription("RESTful Hinemos Client component");
		setOwner("@pango853");
		setAuthor("@pango853");
	}

	public static void main(String[] args) throws Exception {
		Component component = new MainServer();
		component.getServers().add(Protocol.HTTP, PORT);
		component.getContext().getParameters().set("tracing", "true");
		component.getDefaultHost().attachDefault(new LoginApp());
		component.start();
	}
}
