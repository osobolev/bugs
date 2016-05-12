package bugs;

import common.LoginUtil;
import common.Status;
import common.TemplateUtil;
import common.User;

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

public class BugsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> data = new HashMap<>();

        //header data. logged in user
        User user = LoginUtil.getUser(req);
        data.put("user", user.getName());
        //header data. number of opened tasks
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/bugs")) {
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

        ArrayList<BugInList> bugs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/bugs")) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT ID, TEXT, CREATE_TIME, STATUS, PRIORITY FROM BUGS WHERE AUTHOR_ID=? ORDER BY ID DESC")) {
                ps.setInt(1, user.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String text = rs.getString(2);
                        LocalDateTime createTime = rs.getTimestamp(3).toLocalDateTime();
                        Status status = Status.valueOf(rs.getString(4));
                        int priority = rs.getInt(5);
                        BugInList bug = new BugInList(
                            id, text, createTime, status, priority
                        );
                        bugs.add(bug);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        data.put("bugs", bugs);
        TemplateUtil.render("bugs.html", data, resp.getWriter());
    }
}
