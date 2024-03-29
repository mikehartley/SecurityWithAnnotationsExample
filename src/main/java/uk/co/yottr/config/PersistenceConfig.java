package uk.co.yottr.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("uk.co.yottr.repository")
@EnableTransactionManagement
public class PersistenceConfig {

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder.scanPackages("uk.co.yottr").addProperties(getHibernateProperties());
        return builder.buildSessionFactory();
    }

    private Properties getHibernateProperties() {
        Properties prop = new Properties();
        prop.put("hibernate.format_sql", "true");
        prop.put("hibernate.show_sql", "true");
        prop.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        prop.put("hibernate.hbm2ddl.auto", "update");
        return prop;
    }

    @Bean(name = "dataSource")
    public BasicDataSource dataSource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/postgres");
        ds.setUsername("postgres");
        ds.setPassword("postgres");
        ds.setInitialSize(1);
        ds.setMaxActive(20);
        ds.setMaxIdle(3);
        ds.setMinIdle(1);
        ds.setMaxWait(5000);
        ds.setValidationQuery("select 'OK'");
        ds.setTestWhileIdle(true);
        ds.setTestOnBorrow(true);
        ds.setNumTestsPerEvictionRun(5);
        ds.setTimeBetweenEvictionRunsMillis(30000);
        ds.setMinEvictableIdleTimeMillis(60000);
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan("uk.co.yottr.model");
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(getHibernateProperties());

        return em;
    }
}
