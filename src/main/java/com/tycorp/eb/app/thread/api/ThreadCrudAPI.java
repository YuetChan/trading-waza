package com.tycorp.eb.app.thread.api;

import com.google.gson.JsonObject;

import com.tycorp.eb.app.thread.AbstractThreadService;
import com.tycorp.eb.app.thread.dto.non_exposable.ThreadCreateDto;
import com.tycorp.eb.app.thread.event.ThreadCreateEvent;
import com.tycorp.eb.app.thread.dto.transformer.ThreadGetByFilterDtoTransformer;
import com.tycorp.eb.domain.thread.model.Thread;
import com.tycorp.eb.lib.gson.GsonHelper;
import com.tycorp.eb.lib.uuid.UUIDHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/threads")
public class ThreadCrudAPI extends AbstractThreadService {

    @GetMapping(value = "", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonObject> threadsGetByFilter(@RequestParam(required = false, name = "tickers") List<String> tickers,
                                                         @RequestParam(required = false, name = "tags") List<String> tags,
                                                         @RequestParam(required = true, name = "daysAgo") int daysAgo,
                                                         @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                         @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Long processedAt = Instant.now().minus(daysAgo, ChronoUnit.DAYS).toEpochMilli();
        Page<Thread> page = threadRepo.findByFilter(
                processedAt, 1l,
                tickers, tags,
                PageRequest.of(pageNum, pageSize));

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add(
                "threads",
                GsonHelper.createJsonElement(
                        page.getContent()
                                .stream().map(thread -> ThreadGetByFilterDtoTransformer.INSTANCE.transform(thread))
                                .collect(Collectors.toList()))
                        .getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

    @GetMapping(value = "/{threadId}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonObject> threadsGetById(@PathVariable(name = "threadId") Long threadId) {
        Thread thread = threadRepo.findById(threadId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add("thread", GsonHelper.createJsonElement(thread).getAsJsonObject());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<JsonObject> threadCreate(@RequestBody ThreadCreateDto dto) {
        String requestUUID = UUIDHelper.generateUUID();
        eventPublisher.publishEvent(
                ThreadCreateEvent.getBuilder()
                        .setOperator(authFacade.getAuthenticatedEbUserDetail())
                        .setSource(this).setUUID(requestUUID)
                        .setCreateDto(dto)
                        .build());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("requestUUID", requestUUID);

        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
    }

//    @PatchMapping(value = "/{threadId}", produces = "application/json")
//    public ResponseEntity<JsonObject> threadUpdate(@PathVariable(name = "threadId") long threadId,
//                                                   @RequestBody ThreadUpdateDto dto) {
//        String requestUUID = UUIDHelper.generateUUID();
//        dto.setThreadId(threadId);
//        eventPublisher.publishEvent(
//                ThreadUpdateEvent.getBuilder()
//                        .setOperator(authFacade.getAuthenticatedEbUserDetail())
//                        .setSource(this).setUUID(requestUUID)
//                        .setUpdateDto(dto)
//                        .build());
//
//        var resJson = DefaultGsonHelper.getJsonObject();
//        resJson.addProperty("requestUUID", requestUUID);
//        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
//    }

}
