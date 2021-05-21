package com.momo.cn.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = 3523665570575795589L;

    private Integer id;
    
    private String name;
    
    private String password;

    private String phone;

}
