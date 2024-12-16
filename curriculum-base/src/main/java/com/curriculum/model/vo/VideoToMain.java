package com.curriculum.model.vo;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VideoToMain {
    private int id;
    private String username;
    private String title;
    private String description;
    private String tags;
    private String cover;
    private String timelength;
    private String style;
}
