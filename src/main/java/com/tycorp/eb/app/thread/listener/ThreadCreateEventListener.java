package com.tycorp.eb.app.thread.listener;

import com.tycorp.eb.app.thread.AbstractThreadService;
import com.tycorp.eb.app.thread.dto.non_exposable.ThreadCreateDto;
import com.tycorp.eb.app.thread.event.ThreadCreateEvent;
import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import com.tycorp.eb.domain.thread.model.Thread;
import com.tycorp.eb.domain.user.model.EbUser;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Set;

@Component
public class ThreadCreateEventListener extends AbstractThreadService {

    @Async("asyncExecutor")
    @EventListener
    @Transactional
    public void handleEvent(ThreadCreateEvent event) {
        ThreadCreateDto createDto = event.getCreateDto();

        requestService.runRequest(event.getUUID(), () -> {
            SubscriptionSlave slave = slaveRepo.findById(createDto.getSlaveId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found"));
            EbUser user = userRepo.findById(createDto.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            Thread.Builder builder = Thread.getBuilder();

            builder.setOperator(event.getLoginedEbUserDetail());
            builder.setSlave(slave)
                    .next()
                    .runIfSuccess(() -> {
                        Set<Ticker> tickers = findTickersByNameOrCreate(createDto.getTickerNames());
                        Set<Tag> tags = findTagsByNameOrCreate(createDto.getTagNames());

                        masterRepo.save(
                                slave.getMaster()
                                        .addTags(tags).addTickers(tickers));
                        threadRepo.save(
                                builder.setProcessedAt(createDto.getProcessedAt())
                                        .setEbUser(user)
                                        .setTitle(createDto.getTitle()).setDescription(createDto.getDescription()).setContents(createDto.getContents())
                                        .setTickers(tickers).setTags(tags)
                                        .build());
                    })
                    .consumeError(errs -> {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, errs.toString());
                    });
        });
    }

}
