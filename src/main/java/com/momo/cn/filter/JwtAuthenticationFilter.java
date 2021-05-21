package com.momo.cn.filter;

import com.momo.cn.entity.SysPermission;
import com.momo.cn.entity.SysRole;
import com.momo.cn.entity.SysUserRole;
import com.momo.cn.security.token.CustomLoginAuthenticationToken;
import com.momo.cn.service.SysPermissionService;
import com.momo.cn.service.SysRoleService;
import com.momo.cn.service.SysUserRoleService;
import com.momo.cn.util.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT过滤器
 * 在访问受限URL前，验证JWT token
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private static final PathMatcher pathMatcher=new AntPathMatcher();

    static final String USER_ID_KEY="USER_ID";

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    /**
     * 验证token 成功则返回包含authorities的token；失败则返回null
     */
    private CustomLoginAuthenticationToken getAuthentication(HttpServletRequest request){
//        Collection<GrantedAuthority> authorities=new ArrayList<>();
        String token=request.getHeader("Authorization");

        if(token!=null){
            Map map= JwtUtils.unSign(token);
            Integer userId=(Integer)map.get(USER_ID_KEY);

            if(userId!=null){
                List<String> roleList=new ArrayList<>();
                List<GrantedAuthority> myAuthorities=new ArrayList<>();
                List<SysPermission> permissionList=new ArrayList<>();

                SysUserRoleService userRoleService=SpringBeanFactoryUtils.getBean(SysUserRoleService.class);
                SysRoleService roleService=SpringBeanFactoryUtils.getBean(SysRoleService.class);
                SysPermissionService sysPermissionService=SpringBeanFactoryUtils.getBean(SysPermissionService.class);
                //根据userId获取List<role>
                // 根据role获取List<Permission>
                List<SysUserRole> userRoleList= userRoleService.listByUserId(userId);
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
                    myAuthorities.add(new SimpleGrantedAuthority(p.getPermission()));
                }

                //因为JWT已经验证了用户合法性，所以principal和credentials直接为null即可
                return new CustomLoginAuthenticationToken(null,null,myAuthorities);
            }
            return null;
        }
        return null;
    }

    /**
     * 限定拦截请求路径
     */
    private boolean isProtectedUrl(HttpServletRequest request){
        return pathMatcher.match("/api/**",request.getServletPath());
    }

}
