package com.example.demo.publicapi.api;

import com.example.demo.publicapi.dto.PublicResponseDTO;
import com.example.demo.publicapi.service.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/public")
@CrossOrigin
public class PublicController {

    private final PublicService publicService;

    @GetMapping(path = "/{pageNum}" , produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> getPublics(@PathVariable int pageNum) {
        return ResponseEntity.ok().body(publicService.getPublics(pageNum));
    }

    @GetMapping("/count")
    public ResponseEntity<?> getPublicTotalCount() {
        return ResponseEntity.ok().body(publicService.getTotalCount());
    }
}
