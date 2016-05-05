import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class WebServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler();
        server.setHandler(ctx);
        ctx.addServlet(BugListServlet.class, "/list");
        server.start();
    }
}
