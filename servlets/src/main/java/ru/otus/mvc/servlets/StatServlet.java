package ru.otus.mvc.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.DbWorkerConfig;
import ru.otus.mvc.model.Result;
import ru.otus.mvc.model.Statistics;
import ru.otus.mvc.view.StatisticView;
import ru.otus.utils.AuthUtil;
import ru.otus.utils.CacheUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatServlet extends AbstractBaseServlet {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(DbWorkerConfig.class);
    public StatServlet() {}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Result authorise = authorise(req, resp);
        Statistics stats;

        if (authorise.getResult() == AuthUtil.FORBIDDEN_403) {
            authorise.setMessage("index.html");
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().append(authorise.getView().toJson());
            return;
        }

        stats = context.getBean(CacheUtil.class).getCacheStatistic();

        StatisticView view = stats.getView();
        view.setStatus(AuthUtil.OK_200);
        resp.getWriter().print(view.toJson());
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}