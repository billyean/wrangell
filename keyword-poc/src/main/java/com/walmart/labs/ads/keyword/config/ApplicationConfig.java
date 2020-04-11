package com.walmart.labs.ads.keyword.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class ApplicationConfig {
    private @Value("${spring.datasource.url}") String dsURL;
    private @Value("${spring.datasource.user}") String dsUser;
    private @Value("${spring.datasource.pass}") String dsPass;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url(dsURL);
        dataSourceBuilder.username(dsUser);
        dataSourceBuilder.password(dsPass);
        return dataSourceBuilder.build();
    }
}
