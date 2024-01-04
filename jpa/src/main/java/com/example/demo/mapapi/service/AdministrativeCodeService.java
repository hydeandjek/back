package com.example.demo.mapapi.service;


import com.example.demo.auth.TokenUserInfo;
import com.example.demo.mapapi.dto.AdministrativeCodeDTO;
import com.example.demo.mapapi.repository.AdministrativeCodeRepository;
import com.example.demo.userapi.entity.User;
import com.example.demo.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdministrativeCodeService{

    private final AdministrativeCodeRepository adminRepository;
    private final UserRepository userRepository;

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

    private User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }

    public String getMyAddress(TokenUserInfo userInfo) {
        String myAddrress = String.valueOf(userRepository.findMyAddr(userInfo.getUserId()));

        return myAddrress;

//        List<AddressResponseDTO> dtoList = new ArrayList<>();
//        for (User addr : myAddrress) {
//            AddressResponseDTO dto = AddressResponseDTO.builder()
//                    .myAddress(addr.getUserAddress())
//                    .userName(addr.getUserName())
//                    .build();
//            dtoList.add(dto);
//        }
//        return dtoList;
    }

}
