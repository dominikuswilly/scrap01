package com.example.brick.controller;

import com.example.brick.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {

    @Autowired
    GeneralService generalService;

    @GetMapping(value = "/check01", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity check01(){
        Object obj = generalService.getCheck01();
        return new ResponseEntity(obj, HttpStatus.OK);
    }
}
