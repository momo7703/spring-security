package com.momo.cn.security;

import com.momo.cn.entity.SysPermission;
import com.momo.cn.entity.SysRole;
import com.momo.cn.entity.SysUser;
import com.momo.cn.entity.SysUserRole;
import com.momo.cn.service.SysPermissionService;
import com.momo.cn.service.SysRoleService;
import com.momo.cn.service.SysUserRoleService;
import com.momo.cn.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        SysUser user=userService.selectByName(username);

        if(user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        List<String> roleList=new ArrayList<>();
        List<GrantedAuthority> authorities=new ArrayList<>();
        List<SysPermission> permissionList=new ArrayList<>();
        //根据userId获取List<role>
        // 根据role获取List<Permission>
        List<SysUserRole> userRoleList= userRoleService.listByUserId(user.getId());
        for(SysUserRole userRole:userRoleList){
            SysRole role=roleService.selectById(userRole.getRoleId());
            roleList.add(role.getName());

            List<SysPermission> p=sysPermissionService.listByRoleId(userRole.getRoleId());
            for(SysPermission permission:p){
                permissionList.add(permission);
            }
        }
        //去重
        List<SysPermission> pList=permissionList.stream().distinct().collect(Collectors.toList());
        for(SysPermission p:pList){
            authorities.add(new SimpleGrantedAuthority(p.getPermission()));
        }

        CustomUserDetails userDetails=new CustomUserDetails(user,roleList,authorities);
        return userDetails;
    }
}
