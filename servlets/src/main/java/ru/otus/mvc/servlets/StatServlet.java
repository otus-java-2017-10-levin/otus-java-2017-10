package ru.otus.mvc.servlets;

import org.eclipse.jetty.http.HttpStatus;
import ru.otus.mvc.model.Result;
import ru.otus.mvc.model.Statistics;
import ru.otus.mvc.view.StatisticView;
import ru.otus.utils.JpaUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatServlet extends AbstractBaseServlet {

    public StatServlet() {}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Result authorise = authorise(req, resp);
        final Statistics stats = new Statistics();

        if (authorise.getResult() == HttpStatus.FORBIDDEN_403) {
            authorise.setMessage("/index.html");
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().append(authorise.getView().toJson());
            return;
        }

        JpaUtil.updateCacheStatistic(stats);

        StatisticView view = stats.getView();
        view.setStatus(HttpStatus.OK_200);
        resp.getWriter().print(view.toJson());
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}