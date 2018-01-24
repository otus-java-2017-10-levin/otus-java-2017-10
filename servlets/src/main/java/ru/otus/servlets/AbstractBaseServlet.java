package ru.otus.servlets;

import org.jetbrains.annotations.NotNull;
import ru.otus.controller.AuthController;
import ru.otus.model.Result;
import ru.otus.utils.AuthUtil;
import ru.otus.view.ResultView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class AbstractBaseServlet extends HttpServlet {

    protected static final String USER_HASH = "user.hash";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private final Result logined = new Result();
    private final ResultView view = new ResultView(logined);

    @NotNull
    protected String getUserHash(HttpServletRequest req) {
        return Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals(USER_HASH)).findFirst().get().getValue();
    }

    ResultView authorise(HttpServletRequest req, HttpServletResponse resp) {
        final AuthController controller = new AuthController(logined);

        controller.update(getUserHash(req));

        if (logined.isSuccess()) {
            view.setMessage("already signed in");
            view.setStatus(AuthUtil.STATUS_OK);
            return view;
        }

        final String user = req.getParameter(LOGIN);
        final String pass = req.getParameter(PASSWORD);
        controller.update(user, pass);

        if (logined.isSuccess()) {
            view.setMessage("sign in successfully");
            view.setStatus(AuthUtil.STATUS_OK);
            resp.addCookie(new Cookie(USER_HASH, logined.getMessage()));
        } else {
            view.setStatus(AuthUtil.FORBIDDEN);
            view.setMessage("Authentication failed");
        }
        return view;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/index.html");
    }
}