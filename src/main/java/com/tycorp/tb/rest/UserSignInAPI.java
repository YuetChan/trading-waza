package com.tycorp.tb.rest;

import com.google.gson.JsonObject;
import com.tycorp.tb.service.UserService;
import com.tycorp.tb.rest.dto.non_exposable.UserSignInDto;
import com.tycorp.tb.lib.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserSignInAPI {

    @Autowired
    private UserService userSvc;

    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<JsonObject> userSignIn(@RequestBody UserSignInDto signInDto) {
        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("jwt", userSvc.signIn(signInDto.getUseremail(), signInDto.getPassword()));

        return new ResponseEntity(resJson, HttpStatus.OK);
    }

}
