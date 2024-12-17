package com.curriculum.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VideoVo {
    String id;
    String url;

    public VideoVo(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
