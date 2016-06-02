import org.h2.util.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class StaticServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(404);
            return;
        }
        InputStream res = getClass().getResourceAsStream("/static" + path);
        if (res == null) {
            resp.sendError(404);
            return;
        }
        if (path.toLowerCase().endsWith(".css")) {
            resp.setContentType("text/css");
        } else {
            resp.setContentType("application/binary");
        }
        IOUtils.copy(res, resp.getOutputStream());
    }
}
