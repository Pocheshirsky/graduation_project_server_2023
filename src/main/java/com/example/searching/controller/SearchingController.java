package com.example.searching.controller;

import com.example.searching.service.SearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SearchingController {

    @Autowired
    private SearchingService searchingService;

}
