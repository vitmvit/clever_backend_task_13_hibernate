package ru.clevertec.house.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
public class ConfigReader {

    @Autowired
    private Environment environment;

    @Bean
    public Map<String, String> getConfigMap() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("driver", environment.getProperty("driver"));
        configMap.put("url", environment.getProperty("url"));
        configMap.put("username", environment.getProperty("username"));
        configMap.put("password", environment.getProperty("password"));
        return configMap;
    }
}