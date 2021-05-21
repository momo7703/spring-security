package com.momo.cn.service;

import com.momo.cn.entity.SysUser;
import com.momo.cn.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    public SysUser selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public SysUser selectByName(String name) {
        return userMapper.selectByName(name);
    }

    public SysUser selectByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }
}
