package com.example.demo.publicapi.service;

import com.example.demo.publicapi.dto.PublicResponseDTO;
import com.example.demo.publicapi.repository.PublicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PublicService {

    private final PublicRepository publicRepository;

    public List<PublicResponseDTO> getPublics(int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 10);
        return publicRepository.findAll(pageRequest).get().map(item -> PublicResponseDTO.builder()
                .data_id(item.getData_id())
                .publicTitle(item.getPublicTitle())
                .publicNum(item.getPublicNum())
                .publicAddr(item.getPublicAddr())
                .x(item.getX())
                .y(item.getY())
                .build())
                .collect(Collectors.toList());
    }


    public long getTotalCount() {
        return publicRepository.count();
    }
}
