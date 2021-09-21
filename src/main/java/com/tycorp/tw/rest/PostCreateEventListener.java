package com.tycorp.tw.rest;

import com.tycorp.tw.exception.DomainEntityNotFoundException;
import com.tycorp.tw.service.RequestService;
import com.tycorp.tw.rest.dto.non_exposable.PostCreateDto;
import com.tycorp.tw.rest.event.PostCreateEvent;
import com.tycorp.tw.domain.*;
import com.tycorp.tw.domain.User;
import com.tycorp.tw.domain.Post;
import com.tycorp.tw.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;

@Component
public class PostCreateEventListener {

    @Autowired
    private SubscriptionMasterRepository masterRepo;
    @Autowired
    private SubscriptionSlaveRepository slaveRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private TickerRepository tickerRepo;
    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private RequestService requestSvc;

    @Async("asyncExecutor")
    @EventListener({PostCreateEvent.class})
    @Transactional
    public void handleEvent(PostCreateEvent event) {
        requestSvc.runRequest(event.getUUID(), () -> {
            try {
                PostCreateDto createDto = event.getCreateDto();

                SubscriptionSlave slave = slaveRepo.findById(createDto.getSlaveId()).orElseThrow(
                        () -> new DomainEntityNotFoundException("Subscription not found"));
                User user = userRepo.findById(createDto.getUserId()).orElseThrow(
                        () -> new DomainEntityNotFoundException("User not found"));

                Set<Ticker> tickers = tickerRepo.findAllByNamesOrCreate(createDto.getTickers());
                Set<Tag> tags = tagRepo.findAllByNamesOrCreate(createDto.getTags());

                Post.Builder builder = Post.getBuilder();
                builder.setOperator(event.getSignedInUserDetail());
                Post post = builder
                        .setProcessedAt(createDto.getProcessedAt())
                        .setSlave(slave).setUser(user)
                        .setTitle(createDto.getTitle()).setDescription(createDto.getDescription())
                        .setContents(createDto.getContents())
                        .setTickers(tickers).setTags(tags)
                        .build();

                postRepo.save(post);

                SubscriptionMaster master = slave.getMaster();
                master.addTickers(tickers);
                master.addTags(tags);
                master.addPost(post);

                masterRepo.save(master);
            }catch (Exception ex) {
                throw ex;
            }
        });
    }

}
