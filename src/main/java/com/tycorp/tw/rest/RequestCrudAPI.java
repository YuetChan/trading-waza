package com.tycorp.tw.rest;

import com.google.gson.JsonObject;
import com.tycorp.tw.lib.GsonHelper;
import com.tycorp.tw.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/requests")
public class RequestCrudAPI {

    @Autowired
    private RequestService requestSvc;

    @GetMapping(value = "/{UUID}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<JsonObject> requestGetByUUID(@PathVariable(name = "UUID") String UUID) {
        JsonObject resJson= GsonHelper.getJsonObject();
        resJson.addProperty("status", requestSvc.getRequestByUUID(UUID));

        return new ResponseEntity(resJson, HttpStatus.ACCEPTED);
    }

}
