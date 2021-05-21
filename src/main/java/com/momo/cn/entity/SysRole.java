package com.momo.cn.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = -5086278171828094239L;

    private Integer id;

    private String name;

}
