package com.bidnamu.bidnamubackend.testClass;

import com.bidnamu.bidnamubackend.global.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestEntityRepository testEntityRepository;

    @Transactional
    public Long insertEntityReadOnlyFalse() {
        return testEntityRepository.save(new TestEntity()).getId();
    }

    @Transactional(readOnly = true)
    public void insertEntityReadOnlyTrue() {
        testEntityRepository.save(new TestEntity());
    }

    @DistributedLock(key = "'testEntity'")
    public void addNumber(final TestEntity entity) {
        entity.setNum(entity.getNum() + 1);
    }
}
