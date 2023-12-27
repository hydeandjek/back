package com.example.demo.applianceapi.service;

import com.example.demo.applianceapi.dto.ApplianceDTO;
import com.example.demo.applianceapi.entity.Appliance;
import com.example.demo.applianceapi.repository.ApplianceRepository;
import com.example.demo.productapi.dto.ProductDTO;
import com.example.demo.productapi.entity.Product;
import com.example.demo.productapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApplianceService {

    private final ApplianceRepository applianceRepository;

    public List<ApplianceDTO> getAppliances(int pageNum) {
        List<Appliance> appliances = applianceRepository.findAll();
        List<ApplianceDTO> applianceDTOS = new ArrayList<>();

        int startIndex = (pageNum - 1) * 6;
        int endIndex = startIndex + 6;

        for (int i = startIndex; i<endIndex && i<appliances.size(); i++) {
            Appliance appliance = appliances.get(i);
            ApplianceDTO applianceDTO = new ApplianceDTO();
            applianceDTO.setApplianceRank(appliance.getApplianceRank());
            applianceDTO.setApplianceImg(appliance.getApplianceImg());
            applianceDTO.setApplianceUrl(appliance.getApplianceUrl());
            applianceDTO.setApplianceName(appliance.getApplianceName());
            applianceDTO.setAppliancePrice(appliance.getAppliancePrice());
            applianceDTOS.add(applianceDTO);
        }

        return applianceDTOS;

    }

}
