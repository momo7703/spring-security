package com.momo.cn.security;

import com.momo.cn.entity.SysRole;
import com.momo.cn.entity.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private SysUser user;

    private List<String> roleList;

    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(SysUser user,List<String> roleList,Collection<? extends GrantedAuthority> authorities){
        this.user=user;
        this.roleList=roleList;
        this.authorities=authorities;
    }

    public List<String> getRoleList(){
        return roleList;
    }

    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    public String getPassword(){
        return user.getPassword();
    }

    public String getUsername(){
        return user.getName();
    }

    public boolean isAccountNonExpired(){
        return true;
    }

    public boolean isAccountNonLocked(){
        return true;
    }

    public boolean isCredentialsNonExpired(){
        return true;
    }

    public boolean isEnabled(){
        return true;
    }

}
