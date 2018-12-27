package com.example.demo.dto;

import lombok.Data;

/**
 * Created by Jerliner on 2018/12/27.
 */
@Data
public class Item {

    private String subject;
    
    private String source;

    private String pass;

    /**
     * 考号
     */
    private String number;
    
    private String batch;
    
    private String province;

}
