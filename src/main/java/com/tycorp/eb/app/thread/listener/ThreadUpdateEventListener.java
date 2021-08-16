package com.tycorp.eb.app.thread.listener;

import com.tycorp.eb.app.thread.AbstractThreadService;
import com.tycorp.eb.app.thread.dto.non_exposable.ThreadUpdateDto;
import com.tycorp.eb.app.thread.event.ThreadUpdateEvent;
import com.tycorp.eb.domain.thread.model.Thread;
import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Set;

@Component
public class ThreadUpdateEventListener extends AbstractThreadService {

    @Async("asyncExecutor")
    @EventListener
    @Transactional
    public void handleEvent(ThreadUpdateEvent event) {
        ThreadUpdateDto updateDto = event.getUpdateDto();

        requestService.runRequest(event.getUUID(), () -> {
            Thread thread =  threadRepo.findById(updateDto.getThreadId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));

            thread.setOperator(event.getLoginedEbUserDetail());
            thread.update(updateDto.getTitle(), updateDto.getDescription(), updateDto.getContents())
                    .next()
                    .runIfSuccess(()-> {
                        Set<Ticker> addedTickers= findTickersByNameOrCreate(updateDto.getAddedTickerNames());
                        Set<Tag> addedTags = findTagsByNameOrCreate(updateDto.getAddedTagNames());
                        Set<Ticker> removedTickers= findTickersByNameOrCreate(updateDto.getRemovedTickerNames());
                        Set<Tag> removedTags = findTagsByNameOrCreate(updateDto.getRemovedTagNames());

                        masterRepo.save(
                                thread.getSlave().getMaster()
                                        .addTickers(addedTickers).addTags(addedTags));
                        threadRepo.save(
                                thread.addTickers(addedTickers).addTags(addedTags)
                                        .removeTags(removedTags).removeTickers(removedTickers));
                    })
                    .consumeError(errs -> {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, errs.toString());
                    });
        });

    }

}
