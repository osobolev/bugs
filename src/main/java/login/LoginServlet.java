package login;

import common.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("defaultLogin", "");
        TemplateUtil.render("login.html", data, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        try (Connection conn = DriverManager.getConnection(DB.DB_NAME)) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT ID, NAME, USER_ROLE, PASS_HASH FROM USERS WHERE LOGIN = ?")) {
                ps.setString(1, login);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String roleStr = rs.getString(3);
                        String passHash = rs.getString(4);
                        if (Objects.equals(passHash, GetSHA256.crypt(password))) {
                            Role role = Role.valueOf(roleStr);
                            LoginUtil.setUser(req, new User(id, role, name));
                            resp.sendRedirect("bugs");
                            return;
                        }
                    }
                }
            }
            Map<String, Object> data = new HashMap<>();
            data.put("defaultLogin", login);
            data.put("error", "Неправильный логин или пароль");
            TemplateUtil.render("login.html", data, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
