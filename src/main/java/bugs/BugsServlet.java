package bugs;

import common.Status;
import common.TemplateUtil;

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
        ArrayList<BugInList> bugs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:h2:~/bugs")) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT ID, TEXT, CREATE_TIME, STATUS, PRIORITY FROM BUGS ORDER BY ID DESC")) {
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
