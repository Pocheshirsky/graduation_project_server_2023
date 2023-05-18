package com.example.searching.service;

import com.example.searching.repository.UserPoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchingService {

    @Autowired
    private UserPoolRepository userPoolRepository;
}
