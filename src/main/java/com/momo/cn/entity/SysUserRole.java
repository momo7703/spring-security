package com.momo.cn.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -1659521988686140948L;

    private Integer userId;

    private Integer roleId;

}
