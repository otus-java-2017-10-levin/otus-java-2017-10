package ru.otus.servlets;

import ru.otus.controller.StatisticsController;
import ru.otus.view.StatisticView;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            controller.update();
        } catch (AttributeNotFoundException | MBeanException | InstanceNotFoundException | ReflectionException e) {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            e.printStackTrace();
        }
        resp.getWriter().print(stat.getView());
        resp.setContentType("application/json;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
