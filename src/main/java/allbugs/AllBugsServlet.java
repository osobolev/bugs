package allbugs;

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

public class AllBugsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        ArrayList<BugInFullList> fbugs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT ID, TEXT, CREATE_TIME, STATUS, PRIORITY, AUTHOR_ID FROM BUGS ORDER BY ID DESC;")) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String text = rs.getString(2);
                        LocalDateTime createTime = rs.getTimestamp(3).toLocalDateTime();
                        Status status = Status.valueOf(rs.getString(4));
                        int priority = rs.getInt(5);
                        int uid = rs.getInt(6);

                        try (PreparedStatement pps = conn.prepareStatement(
                                "SELECT NAME, USER_ROLE FROM USERS WHERE ID=?;")) {
                            pps.setInt(1, uid);
                            try (ResultSet rrs = pps.executeQuery()) {
                                while (rrs.next()) {
                                    String name = rrs.getString(1);
                                    Role role = Role.valueOf(rrs.getString(2));
                                    BugInFullList fbug = new BugInFullList(
                                            id, text, createTime, status, priority, name, role
                                    );
                                    fbugs.add(fbug);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        data.put("fbugs", fbugs);
        TemplateUtil.render("allbugs.html", data, resp);
    }
}
