package bug;

import common.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BugServlet extends HttpServlet {

    public static final String ASSIGN = "assign";
    public static final String REASSIGN = "reassign";
    public static final String FIX = "fix";
    public static final String CLOSE = "close";
    public static final String REOPEN = "reopen";

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
        data.put("user", user.getName());
        //header data. number of opened tasks
        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT ID, TEXT, DESCRIPTION, AUTHOR_ID, CREATE_TIME, STATUS, ASSIGNED_ID, PRIORITY FROM BUGS WHERE ID = ?")) {
                ps.setInt(1, bugId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()){
                        String text = rs.getString("TEXT");
                        String description = rs.getString("DESCRIPTION");
                        Status status = Status.valueOf(rs.getString("STATUS"));
                        int currentAssignedUser = rs.getInt("ASSIGNED_ID");
                        data.put("fullBug", new BugDetails(bugId, text, description, status));
                        ArrayList<Button> buttons = new ArrayList<>();
                        switch (user.getRole()) {
                            case DEVELOPER:
                                switch (status) {
                                    case OPENED:
                                    case REOPENED:
                                        buttons.add(new Button(ASSIGN, "Взять в работу"));
                                        break;
                                    case ASSIGNED:
                                        if (currentAssignedUser != user.getId()) {
                                            buttons.add(new Button(REASSIGN, "Взять в работу"));
                                        }
                                        buttons.add(new Button(FIX, "Разработано"));
                                        break;
                                }
                                break;
                            case TESTER:
                                switch (status) {
                                    case RESOLVED:
                                        buttons.add(new Button(CLOSE, "Закрыть"));
                                        buttons.add(new Button(REOPEN, "Переоткрыть"));
                                        break;
                                }
                                break;
                        }
                        data.put("buttons", buttons);
                        TemplateUtil.render("bug.html", data, resp.getWriter());
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendError(404);
        //end of header data
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = LoginUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect("login");
            return;
        }
        String idStr = req.getParameter("id");
        int bugId = Integer.parseInt(idStr);
        String answer = req.getParameter("answer");
        if (req.getParameter(ASSIGN) != null) {
            try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO BUG_HISTORY" +
                        " (BUG_ID, USER_ID, CHANGE_TIME, STATUS_OLD, STATUS_NEW, ASSIGNED_OLD, ASSIGNED_NEW, COMMENT_TEXT)" +
                        " SELECT ID, ?, CURRENT_TIMESTAMP, STATUS, ?, ASSIGNED_ID, ?, ? FROM BUGS WHERE ID = ?")) {
                    ps.setInt(1, user.getId());
                    ps.setString(2, Status.ASSIGNED.name());
                    ps.setInt(3, user.getId());
                    ps.setString(4, answer);
                    ps.setInt(5, bugId);
                    ps.execute();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE BUGS SET STATUS = ?, ASSIGNED_ID = ? WHERE ID = ?")) {
                    ps.setString(1, Status.ASSIGNED.name());
                    ps.setInt(2, user.getId());
                    ps.setInt(3, bugId);
                    ps.execute();
                }
                resp.sendRedirect("bug?id=" + bugId);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } else if (req.getParameter(FIX) != null) {
            // todo
        } else if (req.getParameter(REASSIGN) != null) {
            // todo
        } else if (req.getParameter(CLOSE) != null) {
            // todo
        } else if (req.getParameter(REOPEN) != null) {
            // todo
        }
    }
}
