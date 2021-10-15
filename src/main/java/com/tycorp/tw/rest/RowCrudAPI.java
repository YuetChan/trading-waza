package com.tycorp.tw.rest;

import com.google.gson.JsonObject;

import com.tycorp.tw.lib.DateTimeHelper;
import com.tycorp.tw.rest.dto.exposable.RowGetByFilterDto;
import com.tycorp.tw.rest.dto.non_exposable.RowCreateDto;
import com.tycorp.tw.rest.event.RowCreateEvent;
import com.tycorp.tw.rest.dto.transformer.RowGetByFilterDtoTransformer;
import com.tycorp.tw.domain.Row;
import com.tycorp.tw.lib.GsonHelper;
import com.tycorp.tw.lib.UUIDHelper;
import com.tycorp.tw.repository.RowRepository;
import com.tycorp.tw.rest.event.RowsBatchCreateEvent;
import com.tycorp.tw.spring_security.AuthenticationFacade;
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
import java.util.Set;
import java.util.stream.Collectors;

import static com.tycorp.tw.repository.ComplexSubscriptionMasterRepository.DEFAULT_SUBSCRIPTION_MASTER_ID;

@RestController
@RequestMapping(value = "/rows")
public class RowCrudAPI {

    @Autowired
    private AuthenticationFacade authFacade;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RowRepository rowRepo;

    @GetMapping(value = "", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonObject> rowsGetByFilter(@RequestParam(required = false, name = "indicators") Set<String> indicators,
                                                      @RequestParam(required = true, name = "daysAgo") int daysAgo,
                                                      @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                      @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Long processedAt = Instant.now().minus(daysAgo, ChronoUnit.DAYS).toEpochMilli();
        ZonedDateTime processedAtZdt = DateTimeHelper.truncateTime(Instant.ofEpochMilli(processedAt).atZone(ZoneId.of("America/New_York")));
        Long truncatedProcessedAt = processedAtZdt.toInstant().toEpochMilli();

        Page<Row> page = rowRepo.findByFilter(truncatedProcessedAt, DEFAULT_SUBSCRIPTION_MASTER_ID, indicators,
                PageRequest.of(pageNum, pageSize));

        List<RowGetByFilterDto> filterDtos = page.getContent().stream().map(row -> RowGetByFilterDtoTransformer.INSTANCE.transform(row))
                .collect(Collectors.toList());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add("rows", GsonHelper.createJsonElement(filterDtos).getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<JsonObject> rowCreate(@Valid @RequestBody RowCreateDto createDto) {
        String requestUUID = UUIDHelper.generateUUID();
        eventPublisher.publishEvent(
                RowCreateEvent.getBuilder()
                        .setOperator(authFacade.getAuthenticatedUserDetail())
                        .setSource(this).setUUID(requestUUID)
                        .setCreateDto(createDto)
                        .build());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("requestUUID", requestUUID);

        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/batch", produces = "application/json")
    public ResponseEntity<JsonObject> rowsBatchCreate(@Valid @RequestBody RowCreateDto[] createDtos) {
        String requestUUID = UUIDHelper.generateUUID();
        eventPublisher.publishEvent(
                RowsBatchCreateEvent.getBuilder()
                        .setOperator(authFacade.getAuthenticatedUserDetail())
                        .setSource(this).setUUID(requestUUID)
                        .setCreateDtos(createDtos)
                        .build());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("requestUUID", requestUUID);

        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
    }

}
