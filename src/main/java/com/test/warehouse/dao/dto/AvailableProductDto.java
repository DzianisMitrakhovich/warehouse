package com.test.warehouse.dao.dto;

import lombok.Data;

@Data
public class AvailableProductDto {
    private final String name;
    private final int availableAmount;
}
