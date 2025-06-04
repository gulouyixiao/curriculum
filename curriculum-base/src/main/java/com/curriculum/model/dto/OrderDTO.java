package com.curriculum.model.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private Long page;
    private Long pageSize;
    private String status;
}
