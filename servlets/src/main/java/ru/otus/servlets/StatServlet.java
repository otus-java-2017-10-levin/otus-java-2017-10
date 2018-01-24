package ru.otus.servlets;

import ru.otus.mvc.controller.StatisticsController;
import ru.otus.utils.AuthUtil;
import ru.otus.mvc.view.ResultView;
import ru.otus.mvc.view.StatisticView;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatServlet extends AbstractBaseServlet {

    private final StatisticView stat;
    private final StatisticsController controller;

    public StatServlet(StatisticsController controller, StatisticView stat) {
        this.stat = stat;
        this.controller = controller;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final ResultView authorise = authorise(req, resp);
        if (authorise.getStatus() == AuthUtil.FORBIDDEN) {
            authorise.setMessage("/index.html");
            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().append(authorise.getView());
            return;
        }

        try {
            controller.update();
        } catch (AttributeNotFoundException | MBeanException | InstanceNotFoundException | ReflectionException e) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            e.printStackTrace();
        }

        stat.setStatus(AuthUtil.STATUS_OK);
        resp.getWriter().print(stat.getView());
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}