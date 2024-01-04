package com.example.demo.mapapi.api;

import com.example.demo.auth.TokenEmailCheckInfo;
import com.example.demo.auth.TokenUserInfo;
import com.example.demo.mapapi.dto.AddressResponseDTO;
import com.example.demo.mapapi.dto.AdministrativeCodeDTO;
import com.example.demo.mapapi.service.AdministrativeCodeService;
import com.example.demo.mapapi.service.CctvService;
import com.example.demo.userapi.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/map")
@CrossOrigin
public class MapController {

    private final AdministrativeCodeService adminService;

    // 구 선택 시 동 목록 요청 처리
    @GetMapping(path = "/{gu}", produces = "application/json; charset=UTF-8")
    public List<AdministrativeCodeDTO> getDongList(@PathVariable String gu) {
        return adminService.getDongList(gu);
    }

    // 회원 주소 요청
    @GetMapping("/myaddress/gudong")
    public String getMyAddress(@AuthenticationPrincipal TokenUserInfo userInfo) {
        String addrList = adminService.getMyAddress(userInfo);

        return addrList;
    }

}
