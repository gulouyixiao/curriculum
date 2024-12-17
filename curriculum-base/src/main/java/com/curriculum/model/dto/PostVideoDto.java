package com.curriculum.model.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PostVideoDto {
    private String title;
    private String tags;
    private String cover;
    private String style;
    private String description;
}
