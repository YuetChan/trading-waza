package com.tycorp.eb.app.request;

import com.google.gson.JsonObject;
import com.tycorp.eb.app.AbstractEbAppAggregateService;
import com.tycorp.eb.lib.gson.GsonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/requests")
public class CrudRequestAPI extends AbstractEbAppAggregateService {

    @GetMapping(value = "/{UUID}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonObject> requestGetByUUID(@PathVariable(name = "UUID") String UUID) {
        JsonObject resJson= GsonHelper.getJsonObject();
        resJson.addProperty("status", requestService.getRequestByUUID(UUID));

        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
    }

}
