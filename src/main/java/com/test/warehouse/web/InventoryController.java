package com.test.warehouse.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.warehouse.service.ArticleService;
import com.test.warehouse.service.exception.ServiceException;
import com.test.warehouse.web.dto.Inventory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
public class InventoryController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/inventory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void uploadArticlesFromInventoryFile(@RequestParam("file") MultipartFile articlesFile) throws IOException, ServiceException {
        Inventory inventory = objectMapper.readValue(articlesFile.getBytes(), Inventory.class);
        log.debug("Inventory received for upload : {}", inventory);
        articleService.saveArticles(inventory.getInventory());
    }
}
