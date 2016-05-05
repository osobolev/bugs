package login;

import common.LoginUtil;
import common.Role;
import common.TemplateUtil;
import common.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> data = new HashMap<>();
        TemplateUtil.render("login.html", data, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        Map<String, Object> data = new HashMap<>();
        // todo: проверяем, что пользователь с введенным логином и паролем
        // сущесутвует в БД
        // todo: если да, то вызываем код:
        User user = new User(-1, Role.MANAGER, "Пример");
        LoginUtil.setUser(req, user);
        TemplateUtil.render("login.html", data, resp.getWriter());
    }
}
