package com.bidnamu.bidnamubackend.testClass;

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
    public Long insertEntityReadOnlyTrue() {
        return testEntityRepository.save(new TestEntity()).getId();
    }
}
