package com.tycorp.tw.exception;

import com.google.gson.JsonObject;
import com.tycorp.tw.lib.GsonHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerConfig {

    @org.springframework.web.bind.annotation.ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(HttpServletRequest req, Exception e) {

        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", ((ResponseStatusException) e).getReason());

        return new ResponseEntity(resJson, ((ResponseStatusException) e).getStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidException(HttpServletRequest req, Exception e){
        e.printStackTrace();
        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", e.getMessage());

        return new ResponseEntity(resJson, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameterException(HttpServletRequest req, Exception e){
        e.printStackTrace();
        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", e.getMessage());

        return new ResponseEntity(resJson, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletRequest req, Exception e) {
        e.printStackTrace();
        JsonObject resJson = GsonHelper.getJsonObject();
        resJson.addProperty("message", "Unknown error");

        return new ResponseEntity(resJson, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
