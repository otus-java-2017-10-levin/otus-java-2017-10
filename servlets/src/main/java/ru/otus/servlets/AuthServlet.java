package ru.otus.servlets;

import ru.otus.mvc.view.ResultView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends AbstractBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final ResultView res = authorise(req, resp);

        res.setMessage("stat.html");
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().append(res.getView());
    }
}
