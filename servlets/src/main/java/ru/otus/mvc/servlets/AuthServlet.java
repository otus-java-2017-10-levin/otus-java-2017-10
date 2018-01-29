package ru.otus.mvc.servlets;

import ru.otus.mvc.model.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthServlet extends AbstractBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Result res = authorise(req, resp);

        res.setMessage("stat.html");
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().append(res.getView().toJson());
    }
}