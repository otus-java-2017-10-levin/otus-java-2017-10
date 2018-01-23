package ru.otus.servlets;

import ru.otus.controller.AuthController;
import ru.otus.model.Result;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class AbstractBaseServlet extends HttpServlet {

    private static final String USER_HASH = "user.hash";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    protected final Result logined = new Result();

    public Result authorise(HttpServletRequest req) {
        final AuthController controller = new AuthController(logined);
        controller.update(USER_HASH);
        if (logined.isSuccess()) {
            return logined;
        }

        final String user = req.getParameter(LOGIN);
        final String pass = req.getParameter(PASSWORD);
        controller.update(user, pass);
        return logined;
    }
}