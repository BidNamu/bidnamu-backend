package com.bidnamu.bidnamubackend.global.config;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;


@Configuration
public class DataSourceConfig {

    private static final String MASTER_DATASOURCE = "masterDataSource";
    private static final String SLAVE_DATASOURCE = "slaveDataSource";

    @Bean(name = MASTER_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(name = SLAVE_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE})
    public DataSource routingDataSource(
        @Qualifier(MASTER_DATASOURCE) final DataSource masterDataSource,
        @Qualifier(SLAVE_DATASOURCE) final DataSource slaveDataSource
    ) {
        final RoutingDataSource routingDataSource = new RoutingDataSource();
        final Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DataSourceType.MASTER, masterDataSource);
        dataSources.put(DataSourceType.SLAVE, slaveDataSource);
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        return routingDataSource;
    }

    @Primary
    @Bean
    @DependsOn("routingDataSource")
    public LazyConnectionDataSourceProxy dataSource(final DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }
}
