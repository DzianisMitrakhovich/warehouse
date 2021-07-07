package com.test.warehouse.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPart {
    @JsonAlias("art_id")
    private final int articleId;
    @JsonAlias("amount_of")
    private final int quantity;
}
