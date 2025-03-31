package com.dazo.hyperativa.card.batch.config.infrastructure.infrastructure;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BatchLiquibaseConfig {

    @Bean
    @ConfigurationProperties(prefix="spring.liquibase")
    public LiquibaseProperties primaryLiquibaseProperties(){
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase primaryLiquibase(@Qualifier("dataSource")  DataSource dataSource){
        return springLiquibase(dataSource, primaryLiquibaseProperties());
    }

    private static SpringLiquibase springLiquibase(DataSource dataSource,  LiquibaseProperties liquibaseProperties) {

        SpringLiquibase springLiquibase = new SpringLiquibase();

        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog(liquibaseProperties.getChangeLog());
        springLiquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        springLiquibase.setDropFirst(liquibaseProperties.isDropFirst());
        springLiquibase.setShouldRun(liquibaseProperties.isEnabled());
        springLiquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        springLiquibase.setRollbackFile(liquibaseProperties.getRollbackFile());

        return springLiquibase;
    }

}
