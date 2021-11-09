package com.tycorp.tw.repository;

import com.tycorp.tw.domain.*;
import com.tycorp.tw.spring_security.AuthenticationFacade;
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
@DisplayName("com.tycorp.tw.repository.RowRepositoryIntegrationTest")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class RowRepositoryIntegrationTest {

    @Autowired
    private AuthenticationFacade authFacade;

    @Autowired
    private RowRepository rowRepo_ut;

    @Autowired
    private SubscriptionMasterRepository masterRepo;
    @Autowired
    private SubscriptionSlaveRepository slaveRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TickerRepository tickerRepo;
    @Autowired
    private IndicatorRepository indicatorRepo;

    private Long processedAt = null;

    private Long masterId = null;

    private String ticker = null;
    private Set<String> indicators = null;

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
                "cchan@tradingwaza.com", "",
                "cchan");
        userRepo.save(user);

        ticker = "AAPL";
        indicators = Stream.of("golden_cross").collect(Collectors.toSet());

        Row.Builder builder = Row.getBuilder();
        builder.setOperator(authFacade.getDefaultAuthenticatedUserDetail());
        Row row = builder
                .setProcessedAt(processedAt)
                .setSlave(defaultSlave)
                .setUser(user)
                .setTicker(tickerRepo.findAllByNamesOrCreate(new HashSet<>(Arrays.asList(ticker)))
                        .stream().collect(Collectors.toList())
                        .get(0))
                .setIndicators(indicatorRepo.findAllByNamesOrCreate(indicators))
                .build();

        rowRepo_ut.save(row);
    }



}
