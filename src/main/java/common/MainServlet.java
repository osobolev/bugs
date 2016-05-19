package common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mode on 19.05.2016.
 */
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = LoginUtil.getUser(req);
        if (user != null) {
            resp.sendRedirect("bugs");
        } else {
            resp.sendRedirect("login");
        }
    }
}
