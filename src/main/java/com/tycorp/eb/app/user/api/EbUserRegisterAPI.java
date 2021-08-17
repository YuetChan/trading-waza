package com.tycorp.eb.app.user.api;

import com.google.gson.JsonObject;
import com.tycorp.eb.app.user.AbstractEbUserService;
import com.tycorp.eb.app.user.dto.non_exposable.EbUserRegisterDto;
import com.tycorp.eb.lib.gson.GsonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class EbUserRegisterAPI extends AbstractEbUserService {

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<JsonObject> ebUserRegister(@RequestBody EbUserRegisterDto dto) {
        register(dto.getCode(), dto.getUseremail(), dto.getPassword(), dto.getUsername());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", "Registration success");

        return new ResponseEntity(resJson, HttpStatus.CREATED);
    }

}
