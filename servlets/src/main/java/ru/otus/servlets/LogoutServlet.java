package ru.otus.servlets;

import ru.otus.controller.LogoutController;
import ru.otus.model.Result;
import ru.otus.utils.AuthUtil;
import ru.otus.view.ResultView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends AbstractBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final ResultView authorise = authorise(req, resp);
        if (authorise.getStatus() == AuthUtil.FORBIDDEN) {
            authorise.setMessage("/index.html");
            return;
        }

        final Result result = new Result();
        final ResultView view = new ResultView(result);
        final LogoutController logoutController = new LogoutController(result);

        logoutController.update(getUserHash(req));

        resp.getWriter().append(view.getView());
    }
}
