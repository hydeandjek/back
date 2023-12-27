package com.example.demo.productapi.api;

import com.example.demo.productapi.dto.ProductDTO;
import com.example.demo.productapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "/{pageNum}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<ProductDTO>> getProducts(@PathVariable int pageNum) {
        List<ProductDTO> products = productService.getProducts(pageNum);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
