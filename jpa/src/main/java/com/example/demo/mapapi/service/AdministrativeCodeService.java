package com.example.demo.mapapi.service;


import com.example.demo.mapapi.dto.AdministrativeCodeDTO;
import com.example.demo.mapapi.repository.AdministrativeCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdministrativeCodeService{

    private final AdministrativeCodeRepository adminRepository;

    public List<AdministrativeCodeDTO> getDongList(String gu) {

        List<AdministrativeCodeDTO> dongList = new ArrayList<>();
        List<String> list = adminRepository.getDongList(gu);
        for(String dong : list) {
            AdministrativeCodeDTO dto = new AdministrativeCodeDTO();
            dto.setDong(dong);
            dongList.add(dto);
        }
        return dongList;

    }
}
