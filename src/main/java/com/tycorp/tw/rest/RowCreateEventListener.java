package com.tycorp.tw.rest;

import com.tycorp.tw.exception.DomainEntityNotFoundException;
import com.tycorp.tw.rest.event.RowsBatchCreateEvent;
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
import java.util.*;
import java.util.stream.Collectors;

@Component
public class RowCreateEventListener {

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
                RowCreateDto dto = event.getCreateDto();

                User user = userRepo.findById(dto.getUserId()).orElseThrow(() ->
                        new DomainEntityNotFoundException("User not found"));

                Set<Ticker> tickers = tickerRepo.findAllByNamesOrCreate(new HashSet<>(Arrays.asList(dto.getTicker())));
                Set<Indicator> indicators = indicatorRepo.findAllByNamesOrCreate(dto.getIndicators());

                Row.Builder builder = Row.getBuilder();
                builder.setOperator(event.getSignedInUserDetail());

                Row row = builder
                        .setProcessedAt(dto.getProcessedAt())
                        .setEndTimeAt(dto.getEndTimeAt())
                        .setUser(user)
                        .setTicker(tickers.stream().collect(Collectors.toList()).get(0))
                        .setPriceDetail(dto.getPriceDetail())
                        .setIndicators(indicators)
                        .build();

                rowRepo.save(row);
            }catch (Exception ex) {
                throw ex;
            }
        });
    }

    @Async("asyncExecutor")
    @EventListener({RowsBatchCreateEvent.class})
    @Transactional
    public void handleEvent(RowsBatchCreateEvent event) {
        requestSvc.runRequest(event.getUUID(), () -> {
            try {
                RowCreateDto[] createDtos = event.getCreateDtos();
                List<Row> rowsBatch = new ArrayList();

                for(RowCreateDto dto : createDtos) {
                    User user = userRepo.findById(dto.getUserId()).orElseThrow(() ->
                            new DomainEntityNotFoundException("User not found"));

                    Set<Ticker> tickers = tickerRepo.findAllByNamesOrCreate(new HashSet<>(Arrays.asList(dto.getTicker())));
                    Set<Indicator> indicators = indicatorRepo.findAllByNamesOrCreate(dto.getIndicators());

                    Row.Builder builder = Row.getBuilder();
                    builder.setOperator(event.getSignedInUserDetail());

                    Row row = builder
                            .setProcessedAt(dto.getProcessedAt())
                            .setEndTimeAt(dto.getEndTimeAt())
                            .setUser(user)
                            .setTicker(tickers.stream().collect(Collectors.toList()).get(0))
                            .setPriceDetail(dto.getPriceDetail())
                            .setIndicators(indicators)
                            .build();

                    rowsBatch.add(row);
                }

                rowRepo.saveAll(rowsBatch);
            }catch (Exception ex) {
                throw ex;
            }
        });
    }

}
