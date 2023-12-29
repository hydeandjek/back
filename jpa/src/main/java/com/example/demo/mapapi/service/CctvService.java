package com.example.demo.mapapi.service;

import com.example.demo.mapapi.dto.CctvResponseDTO;
import com.example.demo.mapapi.entity.Cctv;
import com.example.demo.mapapi.repository.CctvRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CctvService {

    private final CctvRepository cctvRepository;

    public List<CctvResponseDTO> getCctvs(String gu, String dong, int pageNum) {
        int pageSize = 10;
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = startIndex + pageSize;

        List<Cctv> cctvList = cctvRepository.findAllByGuAndDong(gu, dong);

        if (startIndex >= cctvList.size()) {
            return Collections.emptyList(); // 페이지 범위를 벗어나는 경우 빈 리스트 반환
        }

        // 페이지에 해당하는 CCTV 리스트 추출
        List<Cctv> pageCctvList = cctvList.subList(startIndex, Math.min(endIndex, cctvList.size()));

        // CctvResponseDTO로 매핑하여 반환
        return pageCctvList.stream().map(item -> CctvResponseDTO.builder()
                        .data_id(item.getData_id())
                        .cctvAddr(item.getCctvAddr())
                        .cctvName(item.getCctvName())
                        .cctvNum(item.getCctvNum())
                        .x(item.getX())
                        .y(item.getY())
                        .build())
                .collect(Collectors.toList());
    }

    public long getTotalCount(String gu, String dong) {
        return cctvRepository.findAllByGuAndDong(gu, dong).stream().count();
    }

}
