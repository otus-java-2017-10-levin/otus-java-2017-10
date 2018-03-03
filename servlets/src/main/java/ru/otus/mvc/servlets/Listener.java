package ru.otus.mvc.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.DbWorker;
import ru.otus.DbWorkerConfig;
import ru.otus.RpcServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Listener implements ServletContextListener {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(DbWorkerConfig.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            try {
                context.getBean(RpcServer.class).init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(context.getBean(DbWorker.class));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}