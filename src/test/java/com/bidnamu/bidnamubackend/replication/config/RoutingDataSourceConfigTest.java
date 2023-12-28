package com.bidnamu.bidnamubackend.replication.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bidnamu.bidnamubackend.global.config.DataSourceType;
import com.bidnamu.bidnamubackend.global.config.RoutingDataSource;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
class RoutingDataSourceConfigTest {

    private static final String DETERMINE_CURRENT_LOOKUP_KEY = "determineCurrentLookupKey";

    @Transactional
    @DisplayName("MasterDataSource Replication 설정 테스트")
    @Test
    void testMasterDataSourceReplication() throws Exception {

        // Given
        final RoutingDataSource routingDataSource = new RoutingDataSource();

        // When
        final Method declaredMethod = RoutingDataSource.class.getDeclaredMethod(
            DETERMINE_CURRENT_LOOKUP_KEY);
        declaredMethod.setAccessible(true);

        final Object object = declaredMethod.invoke(routingDataSource);

        // Then
        log.info("object : [{}]", object);
        assertEquals(DataSourceType.MASTER.toString(), object.toString());
    }

    @Transactional(readOnly = true)
    @DisplayName("SlaveDataSource Replication 설정 테스트")
    @Test
    void testSlaveDataSourceReplication() throws Exception {

        // Given
        final RoutingDataSource routingDataSource = new RoutingDataSource();

        // When
        final Method declaredMethod = RoutingDataSource.class.getDeclaredMethod(
            DETERMINE_CURRENT_LOOKUP_KEY);
        declaredMethod.setAccessible(true);

        final Object object = declaredMethod.invoke(routingDataSource);

        // Then
        log.info("object : [{}]", object);
        assertEquals(DataSourceType.SLAVE.toString(), object.toString());
    }
}