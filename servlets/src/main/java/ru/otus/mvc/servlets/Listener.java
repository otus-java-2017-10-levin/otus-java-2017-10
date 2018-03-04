package ru.otus.mvc.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.DbWorker;
import ru.otus.DbWorkerConfig;
import ru.otus.RpcServer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

class Listener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(Listener.class);
    private final ApplicationContext context = new AnnotationConfigApplicationContext(DbWorkerConfig.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Starting rpc server");

       context.getBean(RpcServer.class).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.getBean(DbWorker.class).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}