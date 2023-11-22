package com.bidnamu.bidnamubackend.replication.service;

import com.bidnamu.bidnamubackend.testClass.TestEntity;
import com.bidnamu.bidnamubackend.testClass.TestEntityRepository;
import com.bidnamu.bidnamubackend.testClass.TestService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplicationServiceTest {

    @Resource
    TestService testService;

    @Resource
    private TestEntityRepository testEntityRepository;

    @DisplayName("@Transaction(readOnly = false)일 때 한 건 저장")
    @Test
    void testInsertEntityReadOnlyFalse() {
        assertDoesNotThrow(() -> testService.insertEntityReadOnlyFalse());
    }

    @DisplayName("@Transaction(readOnly = true)일 때 한 건 저장할 경우 예외를 던져야 한다")
    @Test
    void testInsertEntityReadOnlyTrue() {
        assertThrows(Exception.class, () -> testService.insertEntityReadOnlyTrue());
    }

    @DisplayName("Master 데이터베이스에 저장한 엔티티와 Slave 에서 해당 아이디로 조회한 엔티티는 같아야 한다")
    @Test
    void testMasterSlaveReplication() {
        String testData = "Test Data";

        TestEntity newEntity = new TestEntity();
        newEntity.setData(testData);
        testEntityRepository.save(newEntity);

        TestEntity foundEntity = testEntityRepository.findById(newEntity.getId()).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(testData, foundEntity.getData());
    }
}
