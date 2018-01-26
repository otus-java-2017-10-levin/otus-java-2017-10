package ru.otus.mvc.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends AbstractBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        logout(req);
    }
}
