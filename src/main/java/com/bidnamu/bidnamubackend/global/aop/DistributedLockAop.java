package com.bidnamu.bidnamubackend.global.aop;

import com.bidnamu.bidnamubackend.global.annotation.DistributedLock;
import com.bidnamu.bidnamubackend.global.util.AopForTransaction;
import com.bidnamu.bidnamubackend.global.util.CustomSpringELParser;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.bidnamu.bidnamubackend.global.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        final String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        final RLock rLock = redissonClient.getLock(key);

        try {
            final boolean available = rLock.tryLock(distributedLock.waitTime(),
                distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!available) {
                return false;
            }

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            rLock.unlock();
        }
    }
}
