package ru.clevertec.house.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    private static final String ENTITY_FOLDER = "ru.clevertec.house";
    private static final String HIBERNATE_TRANSACTION_JTA_PLATFORM = "hibernate.transaction.jta.platform";
    private static final String HIBERNATE_OGM_DATASTORE_PROVIDER = "hibernate.ogm.datastore.provider";
    private static final String HIBERNATE_USE_SQL_COMMENTS = "hibernate.use_sql_comments";
    private static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String CHANGELOG_PATH = "classpath:changelog-master.xml";

    @Value("${" + HIBERNATE_TRANSACTION_JTA_PLATFORM + "}")
    private String transactionJtaPlatform;
    @Value("${" + HIBERNATE_OGM_DATASTORE_PROVIDER + "}")
    private String ogmDatasourceProvider;
    @Value("${" + HIBERNATE_USE_SQL_COMMENTS + "}")
    private String useSqlComments;
    @Value("${" + HIBERNATE_SHOW_SQL + "}")
    private String showSql;
    @Value("${" + HIBERNATE_FORMAT_SQL + "}")
    private String formatSql;
    @Value("${database.driver}")
    private String driver;
    @Value("${database.url}")
    private String url;
    @Value("${database.user}")
    private String user;
    @Value("${database.password}")
    private String password;

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager;
        }
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan(ENTITY_FOLDER);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        Properties properties = getHibernateProperties();
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(driver);
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(CHANGELOG_PATH);
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();

        properties.setProperty(HIBERNATE_TRANSACTION_JTA_PLATFORM, transactionJtaPlatform);
        properties.setProperty(HIBERNATE_OGM_DATASTORE_PROVIDER, ogmDatasourceProvider);
        properties.setProperty(HIBERNATE_USE_SQL_COMMENTS, useSqlComments);
        properties.setProperty(HIBERNATE_SHOW_SQL, showSql);
        properties.setProperty(HIBERNATE_FORMAT_SQL, formatSql);

        return properties;
    }
}
