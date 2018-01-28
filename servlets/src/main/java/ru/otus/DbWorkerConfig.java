package ru.otus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.utils.CacheUtil;
import ru.otus.utils.JpaUtil;

@Configuration
public class DbWorkerConfig {

    @Bean
    @Scope("singleton")
    public CacheUtil cacheUtil() {
        return new CacheUtil();
    }

    @Bean
    @Scope("singleton")
    public JpaUtil jpaUtil() { return new JpaUtil(); }

    @Bean
    @Scope("singleton")
    public DbWorker dbWorker() {
        return new DbWorker(jpaUtil());
    }

}
