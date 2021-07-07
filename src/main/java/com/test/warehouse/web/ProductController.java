package com.test.warehouse.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.service.ProductService;
import com.test.warehouse.service.exception.ServiceException;
import com.test.warehouse.web.dto.ProductForSale;
import com.test.warehouse.web.dto.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void uploadProductsFromFile(@RequestParam("file") MultipartFile productsFile) throws IOException, ServiceException {
        ProductWrapper productWrapper = objectMapper.readValue(productsFile.getBytes(), ProductWrapper.class);
        log.debug("Products received for upload: {}", productWrapper);
        productService.saveAll(productWrapper.getProducts());
    }

    @GetMapping(value = "/products-available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AvailableProductDto>> getAvailableProducts() throws ServiceException {
        return ResponseEntity.ok(productService.listAvailable());
    }

    @PostMapping(path = "/products/checkout", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void sellProduct(@RequestBody ProductForSale productForSale) throws ServiceException, InsufficientAmountInStock {
        log.debug("Received product for sale: {}", productForSale);
        productService.sellProduct(productForSale.getName(), productForSale.getAmount());
    }
}
