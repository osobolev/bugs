package common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Map;

public class HeaderUtil {

    public static User prepareHeader(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> data) throws ServletException, IOException {
        //header data. logged in user
        User user = LoginUtil.getUser(req);
        if (user == null) {
            resp.sendRedirect("login");
            return user;
        }
        data.put("user", user.getName());
        data.put("userRole", user.getRole().translate());
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
        return user;
    }
}
