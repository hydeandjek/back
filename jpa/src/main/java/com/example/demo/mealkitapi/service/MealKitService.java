package com.example.demo.mealkitapi.service;

import com.example.demo.mealkitapi.dto.MealKitDTO;
import com.example.demo.mealkitapi.entity.MealKit;
import com.example.demo.mealkitapi.repository.MealKitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MealKitService {

    private final MealKitRepository mealKitRepository;

    public List<MealKitDTO> getMealKits(int pageNum) {
        List<MealKit> mealKits = mealKitRepository.findAll();
        List<MealKitDTO> mealKitDTOS = new ArrayList<>();

        int startIndex = (pageNum - 1) * 9;
        int endIndex = startIndex + 9;

        for (int i = startIndex; i<endIndex && i<mealKits.size(); i++) {
            MealKit mealKit = mealKits.get(i);
            MealKitDTO mealKitDTO = new MealKitDTO();
            mealKitDTO.setMealKitRank(mealKit.getMealKitRank());
            mealKitDTO.setMealKitImg(mealKit.getMealKitImg());
            mealKitDTO.setMealKitUrl(mealKit.getMealKitUrl());
            mealKitDTO.setMealKitName(mealKit.getMealKitName());
            mealKitDTO.setMealKitPrice(mealKit.getMealKitPrice());
            mealKitDTOS.add(mealKitDTO);
        }

        return mealKitDTOS;

    }

}
