package com.tycorp.eb.config;

import com.google.gson.JsonObject;
import com.tycorp.eb.lib.GsonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerConfig {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(HttpServletRequest req, Exception e) {
        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", ((ResponseStatusException) e).getReason());
        return new ResponseEntity(resJson, ((ResponseStatusException) e).getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", "Unknown error");
        return new ResponseEntity(resJson, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
