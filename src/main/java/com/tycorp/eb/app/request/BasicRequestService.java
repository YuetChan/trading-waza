package com.tycorp.eb.app.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Service
public class BasicRequestService {

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
        startRequest(UUID);
        runnable.run();
        endRequest(UUID);
    }

    private void startRequest(String UUID) {
        hashOperations.put(UUID_CACHE, UUID, String.valueOf(Instant.now().toEpochMilli()));
    }
    private void endRequest(String UUID) {
        hashOperations.put(UUID_CACHE, UUID, REQUEST_END);
    }

    public String getRequestByUUID(String UUID) {
        return hashOperations.get(UUID_CACHE, UUID);
    }

}
