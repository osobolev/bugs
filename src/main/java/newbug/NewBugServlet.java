package newbug;

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

public class NewBugServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        TemplateUtil.render("newbug.html", data, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        String submitreg = req.getParameter("Submitreg");
//        String submitcansel = req.getParameter("Submitcansel");

        if (submitreg != null) {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            String priorityStr = req.getParameter("priority");
            User user = LoginUtil.getUser(req);
            if (user == null) {
                resp.sendError(403);
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO BUGS (TEXT, DESCRIPTION, AUTHOR_ID, CREATE_TIME, STATUS, PRIORITY)" +
                                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, 'OPENED', ?)")) {
                    ps.setString(1, name);
                    ps.setString(2, description);
                    ps.setInt(3, user.getId());
                    ps.setInt(4, Integer.parseInt(priorityStr));
                    ps.execute();
                }
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        resp.sendRedirect("/bugs");
    }
}
