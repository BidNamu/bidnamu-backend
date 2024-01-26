package com.bidnamu.bidnamubackend.distributedlock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bidnamu.bidnamubackend.testClass.TestEntity;
import com.bidnamu.bidnamubackend.testClass.TestEntityRepository;
import com.bidnamu.bidnamubackend.testClass.TestService;
import jakarta.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class DistributedLockTest {

    @Resource
    private TestService testService;

    @Resource
    private TestEntityRepository testEntityRepository;

    private TestEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = testEntityRepository.save(new TestEntity());
    }

    @Test
    @Transactional
    void testDistributedLock() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    testService.addNumber(testEntity);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertEquals(testEntity.getNum(), numberOfThreads);
    }
}
