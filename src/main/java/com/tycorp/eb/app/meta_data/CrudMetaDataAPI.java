package com.tycorp.eb.app.meta_data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.lib.gson.GsonHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/metaData")
public class CrudMetaDataAPI extends AbstractMetaDataService {

    @GetMapping(value = "/tickers", produces = "application/json")
    public ResponseEntity<JsonArray> tickersGetByNameLike(@RequestParam(required = false, name = "name") String name,
                                                          @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                          @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Page<Ticker> page = tickerRepo.findByMasterIdAndNameLike(1l, name  + "%", PageRequest.of(pageNum, pageSize));

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add(
                "tickers",
                GsonHelper.createJsonElement(page.getContent())
                        .getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

    @GetMapping(value = "/tags", produces = "application/json")
    public ResponseEntity<JsonArray> tagsGetByNameLike(@RequestParam(required = false, name = "name") String name,
                                                       @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
                                                       @RequestParam(required = false, name = "pageSize", defaultValue = "20") int pageSize) {
        Page<Tag> page = tagRepo.findByMasterIdAndNameLike(1l, name  + "%", PageRequest.of(pageNum, pageSize));

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.add(
                "tags",
                GsonHelper.createJsonElement(page.getContent())
                        .getAsJsonArray());
        resJson.addProperty("totalPages", page.getTotalPages());
        resJson.addProperty("totalElements", page.getTotalElements());

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

}
