package bug;

import common.DB;
import common.LoginUtil;
import common.TemplateUtil;
import common.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BugServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bugIdStr = req.getParameter("id");
        int bugId = Integer.parseInt(bugIdStr);
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> data = new HashMap<>();

        //header data. logged in user
        User user = LoginUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }
        data.put("user", user == null ? "" : user.getName());
        //header data. number of opened tasks
        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT ID, TEXT, DESCRIPTION, AUTHOR_ID, CREATE_TIME, STATUS, ASSIGNED_ID, PRIORITY FROM BUGS WHERE ID = ?")) {
                ps.setInt(1, bugId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()){
                        String text = rs.getString("TEXT");
                        String description = rs.getString("DESCRIPTION");
                        data.put("fullBug", new BugDetails(bugId, text, description));
                        TemplateUtil.render("bug.html", data, resp.getWriter());
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendError(404);
        //end of header data
    }
}
