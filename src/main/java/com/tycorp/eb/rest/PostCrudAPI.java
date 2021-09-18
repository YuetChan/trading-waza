package com.tycorp.eb.rest;

import com.google.gson.JsonObject;

import com.tycorp.eb.lib.DateTimeHelper;
import com.tycorp.eb.rest.dto.exposable.PostGetByFilterDto;
import com.tycorp.eb.rest.dto.non_exposable.PostCreateDto;
import com.tycorp.eb.rest.event.PostCreateEvent;
import com.tycorp.eb.rest.dto.transformer.PostGetByFilterDtoTransformer;
import com.tycorp.eb.domain.Post;
import com.tycorp.eb.lib.GsonHelper;
import com.tycorp.eb.lib.UUIDHelper;
import com.tycorp.eb.repository.PostRepository;
import com.tycorp.eb.spring_security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.tycorp.eb.repository.ComplexSubscriptionMasterRepository.DEFAULT_SUBSCRIPTION_MASTER_ID;

@RestController
@RequestMapping(value = "/posts")
public class PostCrudAPI {

    @Autowired
    private AuthenticationFacade authFacade;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private PostRepository postRepo;

    @GetMapping(value = "", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonObject> postsGetByFilter(@RequestParam(required = false, name = "tickers") List<String> tickers,
                                                       @RequestParam(required = false, name = "tags") List<String> tags,
                                                       @RequestParam(required = true, name = "daysAgo") int daysAgo,
                                                       @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                       @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Long processedAt = Instant.now().minus(daysAgo, ChronoUnit.DAYS).toEpochMilli();
        ZonedDateTime processedAtZdt = DateTimeHelper.truncateTime(Instant.ofEpochMilli(processedAt).atZone(ZoneId.of("America/New_York")));
        Long truncatedProcessedAt = processedAtZdt.toInstant().toEpochMilli();

        Page<Post> page = postRepo.findByFilter(truncatedProcessedAt, DEFAULT_SUBSCRIPTION_MASTER_ID, tickers, tags,
                PageRequest.of(pageNum, pageSize));

        List<PostGetByFilterDto> getDtos = page.getContent().stream().map(post -> PostGetByFilterDtoTransformer.INSTANCE.transform(post))
                .collect(Collectors.toList());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add("posts", GsonHelper.createJsonElement(getDtos).getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<JsonObject> postCreate(@Valid @RequestBody PostCreateDto createDto) {
        String requestUUID = UUIDHelper.generateUUID();
        eventPublisher.publishEvent(
                PostCreateEvent.getBuilder()
                        .setOperator(authFacade.getAuthenticatedUserDetail())
                        .setSource(this).setUUID(requestUUID)
                        .setCreateDto(createDto)
                        .build());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("requestUUID", requestUUID);

        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
    }

}
