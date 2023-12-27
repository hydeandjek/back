package com.example.demo.furnitureapi.service;

import com.example.demo.furnitureapi.dto.FurnitureDTO;
import com.example.demo.furnitureapi.entity.Furniture;
import com.example.demo.furnitureapi.repository.FurnitureRepository;
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
public class FurnitureService {

    private final FurnitureRepository furnitureRepository;

    public List<FurnitureDTO> getFurnitures(int pageNum) {
        List<Furniture> furnitures = furnitureRepository.findAll();
        List<FurnitureDTO> furnitureDTOS = new ArrayList<>();

        int startIndex = (pageNum - 1) * 6;
        int endIndex = startIndex + 6;

        for (int i = startIndex; i<endIndex && i<furnitures.size(); i++) {
            Furniture furniture = furnitures.get(i);
            FurnitureDTO furnitureDTO = new FurnitureDTO();
            furnitureDTO.setFurnitureRank(furniture.getFurnitureRank());
            furnitureDTO.setFurnitureImg(furniture.getFurnitureImg());
            furnitureDTO.setFurnitureUrl(furniture.getFurnitureUrl());
            furnitureDTO.setFurnitureName(furniture.getFurnitureName());
            furnitureDTO.setFurniturePrice(furniture.getFurniturePrice());
            furnitureDTOS.add(furnitureDTO);
        }

        return furnitureDTOS;

    }

}
