package com.example.demo.publicapi.service;

import com.example.demo.publicapi.repository.PublicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PublicService {

    private final PublicRepository publicRepository;




}
