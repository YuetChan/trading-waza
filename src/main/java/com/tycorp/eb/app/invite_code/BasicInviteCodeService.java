package com.tycorp.eb.app.invite_code;

import no.gorandalum.fluentresult.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class BasicInviteCodeService {

    private final String SUFFIX = ":CODE_CACHE";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    @PostConstruct
    private void initValueOperations() { valueOperations = redisTemplate.opsForValue(); }

    public Result<String, List<String>> validateInviteCode(String useremail, String inviteCode) {
        var expected = valueOperations.get(useremail + SUFFIX);
        return expected != null ?
                (inviteCode.equals(expected) ? Result.success(inviteCode) : Result.error(Arrays.asList("Invalid invite code")))
                : Result.error(Arrays.asList("Invalid invite code"));
    }

}
