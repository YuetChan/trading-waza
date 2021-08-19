package com.tycorp.eb.rest;

import com.tycorp.eb.exception.DomainEntityNotFoundException;
import com.tycorp.eb.service.RequestService;
import com.tycorp.eb.rest.dto.non_exposable.PostCreateDto;
import com.tycorp.eb.rest.event.PostCreateEvent;
import com.tycorp.eb.domain.*;
import com.tycorp.eb.domain.User;
import com.tycorp.eb.domain.Post;
import com.tycorp.eb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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

                SubscriptionSlave slave = slaveRepo.findById(createDto.getSlaveId())
                        .orElseThrow(() -> new DomainEntityNotFoundException("Subscription not found"));
                User user = userRepo.findById(createDto.getUserId())
                        .orElseThrow(() -> new DomainEntityNotFoundException("User not found"));

                Set<Ticker> tickers = tickerRepo.findAllByNamesOrCreate(createDto.getTickers());
                Set<Tag> tags = tagRepo.findAllByNamesOrCreate(createDto.getTags());

                Post.Builder builder = Post.getBuilder();
                builder.setOperator(event.getSignedInUserDetail());
                Post post = builder
                        .setProcessedAt(createDto.getProcessedAt())
                        .setSlave(slave)
                        .setUser(user)
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
