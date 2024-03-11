package ru.clevertec.house.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Objects;
import java.util.Properties;

import static ru.clevertec.house.constant.Constant.CONFIG_FILE_NOT_FOUND_ERROR;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan("ru.clevertec.house")
@PropertySource("classpath:application.yml")
public class SpringConfig {

    private static final String APPLICATION_YML = "application.yml";

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        PropertySourcesPlaceholderConfigurer configure = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(APPLICATION_YML));
        Properties yamlObject = Objects.requireNonNull(yaml.getObject(), CONFIG_FILE_NOT_FOUND_ERROR);
        configure.setProperties(yamlObject);
        return configure;
    }

    @Bean
    public Validator validator() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            return validatorFactory.getValidator();
        }
    }
}
