package com.tycorp.tb.rest;

import com.google.gson.JsonObject;
import com.tycorp.tb.service.UserService;
import com.tycorp.tb.rest.dto.non_exposable.UserRegisterDto;
import com.tycorp.tb.lib.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users")
public class UserRegisterAPI {

    @Autowired
    private UserService userSvc;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<JsonObject> userRegister(@RequestBody UserRegisterDto registerDto) {
        userSvc.register(registerDto.getInviteCode(), registerDto.getUseremail(), registerDto.getPassword(), registerDto.getUsername());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", "Registration success");

        return new ResponseEntity(resJson, HttpStatus.CREATED);
    }

}
