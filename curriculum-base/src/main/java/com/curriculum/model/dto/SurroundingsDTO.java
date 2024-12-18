package com.curriculum.model.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SurroundingsDTO {
    private int page;
    private int pageSize;
    private String tags;
}
