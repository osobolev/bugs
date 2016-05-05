package common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginUtil {

    public static User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (User) session.getAttribute("user");
    }

    public static void setUser(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
    }
}
