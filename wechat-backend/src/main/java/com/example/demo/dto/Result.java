package com.example.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by Jerliner on 2018/12/27.
 */
@Data
public class Result {

    private String name;

    private String zjhm;

    private List<Item> writes;

    private List<Item> interViews;

}
