package ru.otus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.mvc.servlets.AuthServlet;
import ru.otus.mvc.servlets.LogoutServlet;
import ru.otus.mvc.servlets.StatServlet;
import ru.otus.utils.H2Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class App
{
    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {

        H2Util.start("8082");
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new DbWorker());

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new StatServlet()), "/stat");
        context.addServlet(AuthServlet.class, "/auth");
        context.addServlet(LogoutServlet.class, "/logout");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
