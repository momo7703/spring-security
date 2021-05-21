package com.momo.cn.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 2185863805116482199L;

    private Integer id;

    private String url;

    private Integer roleId;

    private String permission;

}
