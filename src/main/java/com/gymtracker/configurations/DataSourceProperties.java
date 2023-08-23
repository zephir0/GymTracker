package com.gymtracker.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secret.properties")
public class DataSourceProperties {

    @Value("${DB_PASSWORD}")
    private String dbPassword;

    @Value("${DB_USERNAME}")
    private String dbUsername;

    @Value("${DB_TEST_URL}")
    private String dbUrl;


}
