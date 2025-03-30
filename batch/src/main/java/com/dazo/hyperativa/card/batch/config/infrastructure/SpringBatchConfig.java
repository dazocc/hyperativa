package com.dazo.hyperativa.card.batch.config.infrastructure;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class SpringBatchConfig extends DefaultBatchConfiguration {

    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;

    public SpringBatchConfig(@Qualifier("dataSource") DataSource dataSource,
                             @Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
        super();
        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }
    @Bean
    public JobRepository jobRepository() throws BatchConfigurationException {
        try{
            JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
            jobRepositoryFactoryBean.setDataSource(dataSource);
            jobRepositoryFactoryBean.setTransactionManager(transactionManager);
            jobRepositoryFactoryBean.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
            jobRepositoryFactoryBean.setDatabaseType("mysql");
            jobRepositoryFactoryBean.afterPropertiesSet();
            return jobRepositoryFactoryBean.getObject();
        }catch (Exception e){
            throw new BatchConfigurationException("Error while trying to create job repository", e);
        }
    }
}
