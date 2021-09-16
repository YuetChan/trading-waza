package com.tycorp.eb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InviteCodeService {

    private final String SUFFIX = ":INVITE_CODE";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    @PostConstruct
    private void initValueOperations() {
        valueOperations = redisTemplate.opsForValue();
    }

    public boolean isInviteCodeValid(String useremail, String inviteCode) {
        String expectedCode = valueOperations.get(useremail + SUFFIX);
        return expectedCode != null ? (inviteCode.equals(expectedCode) ? true : false) : false;
    }

}
