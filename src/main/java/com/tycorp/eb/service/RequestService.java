package com.tycorp.eb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Service
public class RequestService {

    private final String UUID_CACHE = "UUID_CACHE";
    private final String REQUEST_END = "REQUEST_END";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    private void initHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void runRequest(String UUID, Runnable runnable) {
        markStart(UUID);
        runnable.run();
        markEnd(UUID);
    }

    private void markStart(String UUID) {
        hashOperations.put(UUID_CACHE, UUID, String.valueOf(Instant.now().toEpochMilli()));
    }

    private void markEnd(String UUID) {
        hashOperations.put(UUID_CACHE, UUID, REQUEST_END);
    }

    public String getRequestByUUID(String UUID) {
        return hashOperations.get(UUID_CACHE, UUID);
    }

}
