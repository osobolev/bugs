package bug;

import common.TemplateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BugServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bugIdStr = req.getParameter("id");
        int bugId = Integer.parseInt(bugIdStr);
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> data = new HashMap<>();
        data.put("fullBug", new BugDetails(bugId, "???"));
        TemplateUtil.render("bug.html", data, resp.getWriter());
    }
}
