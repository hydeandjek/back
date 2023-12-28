package com.example.demo.productapi.service;

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
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> getProducts(int pageNum) {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();

        int startIndex = (pageNum - 1) * 9;
        int endIndex = startIndex + 9;

        for (int i = startIndex; i<endIndex && i<products.size(); i++) {
            Product product = products.get(i);
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductRank(product.getProductRank());
            productDTO.setProductImg(product.getProductImg());
            productDTO.setProductUrl(product.getProductUrl());
            productDTO.setProductName(product.getProductName());
            productDTO.setProductPrice(product.getProductPrice());
            productDTOS.add(productDTO);
        }

        return productDTOS;

    }

}
