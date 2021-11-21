package com.tycorp.tw.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tycorp.tw.domain.Indicator;
import com.tycorp.tw.domain.Ticker;
import com.tycorp.tw.lib.GsonHelper;
import com.tycorp.tw.repository.IndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/meta-data")
public class MetaDataCrudAPI {

    @Autowired
    private IndicatorRepository indicatorRepo;

    @GetMapping(value = "/indicators", produces = "application/json")
    public ResponseEntity<JsonArray> indicatorsGetByNameLike(@RequestParam(required = false, name = "name") String name,
                                                             @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                             @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Page<Indicator> page = indicatorRepo.findByNameLike(name  + "%");

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add("indicators", GsonHelper.createJsonElement(page.getContent()).getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

}
