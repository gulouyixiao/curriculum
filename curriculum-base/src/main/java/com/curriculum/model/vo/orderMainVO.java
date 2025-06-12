package com.curriculum.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
public class orderMainVO implements Serializable {

    private Long id;

    private LocalDateTime createDate;

    private String status;

    private String name;

    private double unitPrice;

    private int orderNumber;

    private String pic;


}