import allbugs.AllBugsServlet;
import bug.BugServlet;
import bugs.BugsServlet;
import login.LoginServlet;
import newbug.NewBugServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class WebServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler();
        server.setHandler(ctx);
        ctx.addServlet(BugsServlet.class, "/bugs");
        ctx.addServlet(LoginServlet.class, "/login");
        ctx.addServlet(AllBugsServlet.class, "/allbugs");
        ctx.addServlet(BugServlet.class, "/bug");
        ctx.addServlet(NewBugServlet.class, "/newbug");
        server.start();
    }
}
