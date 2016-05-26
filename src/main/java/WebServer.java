import allbugs.AllBugsServlet;
import bug.BugServlet;
import bugs.BugsServlet;
import common.MainServlet;
import login.LoginServlet;
import login.LogoutServlet;
import newbug.NewBugServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;

public class WebServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler ctx = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(ctx);
        ctx.addServlet(MainServlet.class, "/");
        ctx.addServlet(BugsServlet.class, "/bugs");
        ctx.addServlet(LoginServlet.class, "/login");
        ctx.addServlet(LogoutServlet.class, "/logout");
        ctx.addServlet(AllBugsServlet.class, "/allbugs");
        ctx.addServlet(BugServlet.class, "/bug");
        ctx.addServlet(NewBugServlet.class, "/newbug");
        server.start();
    }
}
