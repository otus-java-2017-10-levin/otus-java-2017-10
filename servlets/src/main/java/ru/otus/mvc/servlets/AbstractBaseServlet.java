package ru.otus.mvc.servlets;

import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.mvc.model.Result;
import ru.otus.utils.AuthUtil;

import javax.servlet.http.*;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
class AbstractBaseServlet extends HttpServlet {

    protected static final String USER_HASH = "user.hash";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";

    protected HttpSession getSession(HttpServletRequest req) {
        return req.getSession();
    }

    @NotNull
    protected String getUserHash(HttpSession session) {
        if (session == null)
            return "";

        final String attribute = (String)session.getAttribute(USER_HASH);
        return attribute == null ? "" : attribute;
    }

    protected Result authorise(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = getSession(req);

        final Result logined = login(getUserHash(session));
        if (logined.getResult() == HttpStatus.OK_200)
            return logined;

        final String user = req.getParameter(LOGIN);
        final String pass = req.getParameter(PASSWORD);
        Result result = login(user, pass);

        if (result.getResult() == HttpStatus.OK_200) {
            session.setAttribute(USER_HASH, result.getMessage());
        }

        return result;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/index.html");
    }

    @NotNull
    private Result login(@Nullable String name, @Nullable String pass) {
        return AuthUtil.isValidUser(name, pass)
            ? new Result(HttpStatus.OK_200, AuthUtil.generateHash(name, pass))
            : new Result(HttpStatus.FORBIDDEN_403, "Authentication failed");

    }

    @NotNull
    private Result login(@Nullable String hash) {
        return AuthUtil.validateHash(hash)
                ? new Result(HttpStatus.OK_200, "already signed in")
                : new Result(HttpStatus.UNAUTHORIZED_401, "already signed in");
    }

    @NotNull
    protected Result logout(HttpServletRequest req) {
        AuthUtil.logout(getUserHash(getSession(req)));

        return new Result(HttpStatus.OK_200, "");
    }
}