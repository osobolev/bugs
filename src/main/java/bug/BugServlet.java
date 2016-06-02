package bug;

import common.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BugServlet extends HttpServlet {

    public static final String ASSIGN = "assign";
    public static final String REASSIGN = "reassign";
    public static final String FIX = "fix";
    public static final String CLOSE = "close";
    public static final String REOPEN = "reopen";

    private static ArrayList<BugHistoryItem> loadHistory(Connection conn, int bugId) throws SQLException {
        ArrayList<BugHistoryItem> history = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT BH.CHANGE_TIME, BH.STATUS_OLD, BH.STATUS_NEW, BH.COMMENT_TEXT, U.NAME" +
                "  FROM BUG_HISTORY BH LEFT JOIN USERS U ON BH.USER_ID = U.ID" +
                " WHERE BH.BUG_ID = ?" +
                " ORDER BY BH.ID DESC")) {
            ps.setInt(1, bugId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime date = rs.getTimestamp(1).toLocalDateTime();
                    Status oldStatus = Status.valueOf(rs.getString(2));
                    Status newStatus = Status.valueOf(rs.getString(3));
                    String comment = rs.getString(4);
                    String user = rs.getString(5);
                    BugHistoryItem item = new BugHistoryItem(date, oldStatus, newStatus, user, comment);
                    history.add(item);
                }
            }
        }
        return history;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        User user = HeaderUtil.prepareHeader(req, resp, data);
        if (user == null)
            return;

        String bugIdStr = req.getParameter("id");
        int bugId = Integer.parseInt(bugIdStr);
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
                                        } else {
                                            buttons.add(new Button(FIX, "Разработано"));
                                        }
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
                        ArrayList<BugHistoryItem> history = loadHistory(conn, bugId);
                        data.put("buttons", buttons);
                        data.put("history", history);
                        TemplateUtil.render("bug.html", data, resp);
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendError(404);
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
        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            if (req.getParameter(ASSIGN) != null || req.getParameter(REASSIGN) != null) {
                Status newStatus = Status.ASSIGNED;
                saveHistory(conn, user, bugId, newStatus, answer);
                assign(conn, user, bugId, newStatus);
                resp.sendRedirect("bug?id=" + bugId);
            } else if (req.getParameter(FIX) != null) {
                Status newStatus = Status.RESOLVED;
                saveHistory(conn, user, bugId, newStatus, answer);
                setStatus(conn, bugId, newStatus);
                resp.sendRedirect("bug?id=" + bugId);
            } else if (req.getParameter(CLOSE) != null) {
                Status newStatus = Status.FIXED;
                saveHistory(conn, user, bugId, newStatus, answer);
                setStatus(conn, bugId, newStatus);
                resp.sendRedirect("bug?id=" + bugId);
            } else if (req.getParameter(REOPEN) != null) {
                Status newStatus = Status.REOPENED;
                saveHistory(conn, user, bugId, newStatus, answer);
                setStatus(conn, bugId, newStatus);
                resp.sendRedirect("bug?id=" + bugId);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private static void assign(Connection conn, User user, int bugId, Status newStatus) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE BUGS SET STATUS = ?, ASSIGNED_ID = ? WHERE ID = ?")) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, user.getId());
            ps.setInt(3, bugId);
            ps.execute();
        }
    }

    private static void setStatus(Connection conn, int bugId, Status newStatus) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE BUGS SET STATUS = ?, ASSIGNED_ID = NULL WHERE ID = ?")) {
            ps.setString(1, newStatus.name());
            ps.setInt(2, bugId);
            ps.execute();
        }
    }

    private static void saveHistory(Connection conn, User user, int bugId, Status newStatus, String comment) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO BUG_HISTORY" +
                " (BUG_ID, USER_ID, CHANGE_TIME, STATUS_OLD, STATUS_NEW, ASSIGNED_OLD, ASSIGNED_NEW, COMMENT_TEXT)" +
                " SELECT ID, ?, CURRENT_TIMESTAMP, STATUS, ?, ASSIGNED_ID, ?, ? FROM BUGS WHERE ID = ?")) {
            ps.setInt(1, user.getId());
            ps.setString(2, newStatus.name());
            ps.setInt(3, user.getId());
            ps.setString(4, comment);
            ps.setInt(5, bugId);
            ps.execute();
        }
    }
}
