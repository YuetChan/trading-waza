package com.tycorp.tb.repository;

import com.tycorp.tb.domain.*;
import com.tycorp.tb.spring_security.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DisplayName("com.tycorp.tb.repository.PostRepositoryIntegrationTest")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class PostRepositoryIntegrationTest {

    @Autowired
    private AuthenticationFacade authFacade;

    @Autowired
    private PostRepository postRepo_ut;

    @Autowired
    private SubscriptionMasterRepository masterRepo;
    @Autowired
    private SubscriptionSlaveRepository slaveRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TickerRepository tickerRepo;
    @Autowired
    private TagRepository tagRepo;

    private Long processedAt = null;

    private Long masterId = null;

    private Set<String> tickers = null;
    private Set<String> tags = null;

    @BeforeEach
    public void setup() {
        processedAt = Instant.now().toEpochMilli();

        SubscriptionMaster master = new SubscriptionMaster("default_master");
        masterRepo.save(master);

        masterId = master.getMasterId();

        SubscriptionSlave defaultSlave = slaveRepo.getDefaultSubscriptionSlave(master);
        slaveRepo.save(defaultSlave);

        User user = new User(
                Collections.singleton(defaultSlave),
                "cchan@tradingboard.com", "",
                "cchan");
        userRepo.save(user);

        tickers = Stream.of("AAPL").collect(Collectors.toSet());
        tags = Stream.of("golden_cross").collect(Collectors.toSet());

        Post.Builder builder = Post.getBuilder();
        builder.setOperator(authFacade.getDefaultAuthenticatedUserDetail());
        Post post = builder
                .setProcessedAt(processedAt)
                .setSlave(defaultSlave)
                .setUser(user)
                .setTitle("").setDescription("")
                .setContents(new ArrayList())
                .setTickers(tickerRepo.findAllByNamesOrCreate(tickers)).setTags(tagRepo.findAllByNamesOrCreate(tags))
                .build();

        postRepo_ut.save(post);
    }

    @Test
    public void verifyThat_findPostByFilter_shouldReturnExpectedTotalPages() {
        int expectedTotalPages = 1;
        Page<Post> page = postRepo_ut.findByFilter(processedAt, masterId, tickers, tags,
                PageRequest.of(0, 20));

        assertTrue(page.getTotalPages() == expectedTotalPages);
    }

    @Test
    public void verifyThat_findPostByFilter_shouldReturnExpectedTotalElements() {
        int expectedTotalElement = 1;
        Page<Post> page = postRepo_ut.findByFilter(processedAt, masterId, tickers, tags,
                PageRequest.of(0, 20));

        assertTrue(page.getTotalElements() == expectedTotalElement);
    }

}
