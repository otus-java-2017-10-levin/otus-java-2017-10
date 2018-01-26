package ru.otus.servlets;

import org.jetbrains.annotations.NotNull;
import ru.otus.mvc.controller.AuthController;
import ru.otus.mvc.model.Result;
import ru.otus.utils.AuthUtil;
import ru.otus.mvc.view.ResultView;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
public class AbstractBaseServlet extends HttpServlet {

    protected static final String USER_HASH = "user.hash";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private final Result logined = new Result();
    private final ResultView view = new ResultView(logined);

    protected HttpSession getSession(HttpServletRequest req) {
        final HttpSession session = req.getSession();
        return session;
    }

    protected String getUserHash(HttpSession session) {
        final String attribute = (String)session.getAttribute(USER_HASH);
        return attribute;
    }

    ResultView authorise(HttpServletRequest req, HttpServletResponse resp) {
        final AuthController controller = new AuthController(logined);

        final HttpSession session = getSession(req);

        System.out.println(session.getId());
        controller.update(getUserHash(session));

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
            session.setAttribute(USER_HASH, logined.getMessage());
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