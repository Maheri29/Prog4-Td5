package com.example.prog4.repository.db2.config;


import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableJpaRepositories(
  transactionManagerRef = "employeeCnapsTransactionManager",
  entityManagerFactoryRef = "employeeCnapsEntityManagerFactory",
  basePackages = {"com.example.prog4.repository.db2"}
)
public class Db2Config {

  private final Environment env;


  @Bean(name = "db2DataSource")
  @ConfigurationProperties(prefix = "spring.db2.datasource")
  public DataSource db2DataSource() {
    return DataSourceBuilder.create().build();
  }



  @Bean(name = "db2EntityManagerFactoryBuilder")
  public EntityManagerFactoryBuilder db2EntityManagerFactoryBuilder(
    @Qualifier("db2DataSource") DataSource dataSource
  ) {
    Map<String, Object> properties = new HashMap<>();
    properties.put("javax.persistence.jdbc.url", env.getRequiredProperty("spring.db2.datasource.url"));
    properties.put("javax.persistence.jdbc.user", env.getRequiredProperty("spring.db2.datasource.username"));
    properties.put("javax.persistence.jdbc.password", env.getRequiredProperty("spring.db2.datasource.password"));

    return new EntityManagerFactoryBuilder(
      new HibernateJpaVendorAdapter(),
      properties,
      null
    );
  }


  @Bean(name = "employeeCnapsEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean employeeCnapsEntityManagerFactory(
    @Qualifier("db2EntityManagerFactoryBuilder") final EntityManagerFactoryBuilder builder,
    @Qualifier("db2DataSource") final DataSource dataSource
  ) {
    return  builder
      .dataSource(dataSource)
      .packages("com.example.prog4.repository.db2.entity")
      .build();
  }



  @Bean(name = "employeeCnapsTransactionManager")
  public PlatformTransactionManager employeeCnapsTransactionManager(
    @Qualifier("employeeCnapsEntityManagerFactory") final EntityManagerFactory employeeCnapsEntityManagerFactory
  ) {
    return new JpaTransactionManager(employeeCnapsEntityManagerFactory);
  }



  // Configuration Flyway pour DB2
  @Bean(initMethod = "migrate")
  @ConfigurationProperties(prefix = "spring.flyway.db2")
  public Flyway flywayDb2() {
    return new Flyway(
      Flyway.configure()
        .baselineOnMigrate(true)
        .locations("classpath:/db/migration/db2")
        .dataSource(
          env.getRequiredProperty("spring.db2.datasource.url"),
          env.getRequiredProperty("spring.db2.datasource.username"),
          env.getRequiredProperty("spring.db2.datasource.password")
        )
    );
  }

}
