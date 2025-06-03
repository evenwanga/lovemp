package com.lovemp.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token配置类
 */
@Configuration
public class SaTokenConfiguration {

    /**
     * Sa-Token过滤器配置
     */
    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/auth/login", "/auth/register", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/h2-console/**")
                .setAuth(obj -> {
                    // 检查是否登录
                    SaRouter.match("/**", StpUtil::checkLogin);
                })
                .setError(e -> {
                    // 处理认证异常
                    SaHolder.getResponse().setStatus(HttpStatus.UNAUTHORIZED.value());
                    return "{\"code\":401,\"message\":\"" + e.getMessage() + "\"}";
                });
    }

    /**
     * 自定义权限验证接口
     */
    @Bean
    public StpInterface stpInterface() {
        return new StpInterface() {
            @Override
            public List<String> getPermissionList(Object loginId, String loginType) {
                // TODO: 实现从数据库获取权限列表
                List<String> permissionList = new ArrayList<>();
                return permissionList;
            }

            @Override
            public List<String> getRoleList(Object loginId, String loginType) {
                // TODO: 实现从数据库获取角色列表
                List<String> roleList = new ArrayList<>();
                return roleList;
            }
        };
    }
} 