package com.tycorp.tw.rest;

import com.tycorp.tw.exception.DomainEntityNotFoundException;
import com.tycorp.tw.service.RequestService;
import com.tycorp.tw.rest.dto.non_exposable.RowCreateDto;
import com.tycorp.tw.rest.event.RowCreateEvent;
import com.tycorp.tw.domain.*;
import com.tycorp.tw.domain.User;
import com.tycorp.tw.domain.Row;
import com.tycorp.tw.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RowCreateEventListener {

    @Autowired
    private SubscriptionMasterRepository masterRepo;
    @Autowired
    private SubscriptionSlaveRepository slaveRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RowRepository rowRepo;

    @Autowired
    private TickerRepository tickerRepo;
    @Autowired
    private IndicatorRepository indicatorRepo;

    @Autowired
    private RequestService requestSvc;

    @Async("asyncExecutor")
    @EventListener({RowCreateEvent.class})
    @Transactional
    public void handleEvent(RowCreateEvent event) {
        requestSvc.runRequest(event.getUUID(), () -> {
            try {
                RowCreateDto createDto = event.getCreateDto();

                SubscriptionSlave slave = slaveRepo.findById(createDto.getSlaveId()).orElseThrow(
                        () -> new DomainEntityNotFoundException("Subscription not found"));
                User user = userRepo.findById(createDto.getUserId()).orElseThrow(
                        () -> new DomainEntityNotFoundException("User not found"));

                Set<Ticker> tickers = tickerRepo.findAllByNamesOrCreate(new HashSet<>(Arrays.asList(createDto.getTicker())));
                Set<Indicator> indicators = indicatorRepo.findAllByNamesOrCreate(createDto.getIndicators());

                Row.Builder builder = Row.getBuilder();
                builder.setOperator(event.getSignedInUserDetail());
                Row row = builder
                        .setProcessedAt(createDto.getProcessedAt())
                        .setSlave(slave).setUser(user)
                        .setTicker(tickers.stream().collect(Collectors.toList()).get(0)).setIndicators(indicators)
                        .build();

                rowRepo.save(row);

                SubscriptionMaster master = slave.getMaster();
                master.addTickers(tickers);
                master.addTags(indicators);
                master.addRow(row);

                masterRepo.save(master);
            }catch (Exception ex) {
                throw ex;
            }
        });
    }

}
