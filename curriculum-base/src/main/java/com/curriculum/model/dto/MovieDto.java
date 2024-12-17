package com.curriculum.model.dto;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
public class MovieDto {
    private String grade;
    private String parentId;
    private String title;
}
