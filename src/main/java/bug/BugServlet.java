package bug;

import bugs.BugInList;
import common.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
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
        data.put("user", user.getName());
        //header data. number of opened tasks
        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COUNT(ID) FROM BUGS WHERE AUTHOR_ID=? AND STATUS='OPENED'")) {
                ps.setInt(1, user.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()){
                        int num = rs.getInt(1);
                        data.put("num", num);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        //end of header data

        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT DESCRIPTION FROM BUGS WHERE ID=?")) {
                ps.setInt(1, bugId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String text = rs.getString(1);
                        BugDetails bug = new BugDetails(
                                bugId, text
                        );
                        data.put("fullBug", bug);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        TemplateUtil.render("bug.html", data, resp.getWriter());
    }
}
