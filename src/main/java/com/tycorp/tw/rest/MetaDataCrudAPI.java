package com.tycorp.tw.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tycorp.tw.domain.Tag;
import com.tycorp.tw.domain.Ticker;
import com.tycorp.tw.lib.GsonHelper;
import com.tycorp.tw.repository.TagRepository;
import com.tycorp.tw.repository.TickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.tycorp.tw.repository.ComplexSubscriptionMasterRepository.DEFAULT_SUBSCRIPTION_MASTER_ID;

@RestController
@RequestMapping(value = "/metaData")
public class MetaDataCrudAPI {

    @Autowired
    private TickerRepository tickerRepo;
    @Autowired
    private TagRepository tagRepo;

    @GetMapping(value = "/tickers", produces = "application/json")
    public ResponseEntity<JsonArray> tickersGetByNameLike(@RequestParam(required = false, name = "name") String name,
                                                          @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                          @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Page<Ticker> page = tickerRepo.findByMasterIdAndNameLike(DEFAULT_SUBSCRIPTION_MASTER_ID, name  + "%",
                PageRequest.of(pageNum, pageSize));

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add("tickers", GsonHelper.createJsonElement(page.getContent()).getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

    @GetMapping(value = "/tags", produces = "application/json")
    public ResponseEntity<JsonArray> tagsGetByNameLike(@RequestParam(required = false, name = "name") String name,
                                                       @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                       @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Page<Tag> page = tagRepo.findByMasterIdAndNameLike(DEFAULT_SUBSCRIPTION_MASTER_ID, name  + "%",
                PageRequest.of(pageNum, pageSize));

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add("tags", GsonHelper.createJsonElement(page.getContent()).getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

}
