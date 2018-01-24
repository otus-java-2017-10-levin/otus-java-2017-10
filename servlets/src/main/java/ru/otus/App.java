package ru.otus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.mvc.controller.StatisticsController;
import ru.otus.mvc.model.Statistics;
import ru.otus.servlets.AuthServlet;
import ru.otus.servlets.LogoutServlet;
import ru.otus.servlets.StatServlet;
import ru.otus.utils.H2Util;
import ru.otus.mvc.view.StatisticView;

import javax.management.ObjectName;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class App
{
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";
    private final static Statistics stat = new Statistics();
    private final static StatisticView view = new StatisticView(stat);

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new DbWorker());

        StatisticsController controller = new StatisticsController(new ObjectName("javax.cache:type=CacheStatistics," +
                "CacheManager=my-cache-jsr107,Cache=L1-Cache-0"),stat);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new StatServlet(controller, view)), "/stat");
        context.addServlet(AuthServlet.class, "/auth");
        context.addServlet(LogoutServlet.class, "/logout");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }

    private void run() throws SQLException {
        H2Util.start("8082");

    }
}
