package ru.otus.mvc.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.DbWorker;
import ru.otus.DbWorkerConfig;
import ru.otus.utils.JpaUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Listener implements ServletContextListener {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(DbWorkerConfig.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService.execute(context.getBean(DbWorker.class));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdown();
        context.getBean(JpaUtil.class).getFactory().close();
    }
}
