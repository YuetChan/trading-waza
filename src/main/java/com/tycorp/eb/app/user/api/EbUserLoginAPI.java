package com.tycorp.eb.app.user.api;

import com.google.gson.JsonObject;
import com.tycorp.eb.app.user.AbstractEbUserService;
import com.tycorp.eb.app.user.dto.non_exposable.EbUserLoginDto;
import com.tycorp.eb.lib.gson.GsonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class EbUserLoginAPI extends AbstractEbUserService {

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<JsonObject> ebUserLogin(@RequestBody EbUserLoginDto dto) {
        var resJson= GsonHelper.getJsonObject();
        resJson.addProperty("jwt", login(dto.getUseremail(), dto.getPassword()));
        return new ResponseEntity(resJson, HttpStatus.OK);
    }

}
