package com.tycorp.tw.rest;

import com.google.gson.JsonObject;
import com.tycorp.tw.service.UserService;
import com.tycorp.tw.rest.dto.non_exposable.UserRegisterDto;
import com.tycorp.tw.lib.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(value = "/users")
public class UserRegisterAPI {

    @Autowired
    private UserService userSvc;

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<JsonObject> userRegister(@Valid @RequestBody UserRegisterDto registerDto) {
        userSvc.register(registerDto.getInviteCode(), registerDto.getUseremail(), registerDto.getPassword(), registerDto.getUsername());

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", "Registration success");

        return new ResponseEntity(resJson, HttpStatus.CREATED);
    }

}
