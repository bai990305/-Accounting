package com.bai.account.config;

import lombok.val;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;


@Configuration
public class ShiroConfig {
    public static final int HASH_ITERATIONS1 = 1000;

    /**
     * The bean for Security manager.
     *
     * @param realm the specific realm.
     * @return Security manager.
     */
    @Bean
    public SecurityManager securityManager(Realm realm) {
        val securityManager = new DefaultWebSecurityManager(realm);
        SecurityUtils.setSecurityManager(securityManager);
        ThreadContext.bind(securityManager);
        return securityManager;
    }

    /**
     * The bean for shiroFilterFactoryBean.
     *
     * @param securityManager the specific(具体的) manager.
     * @return shiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
        SecurityManager securityManager) {

        val shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        val filters = shiroFilterFactoryBean.getFilters();
        val shiroFilterDefinitionMap = new LinkedHashMap<String, String>();
        shiroFilterDefinitionMap.put("/v1.0/session", "anon");
        shiroFilterDefinitionMap.put("/v1.0/user", "anon");
        shiroFilterDefinitionMap.put("/v1.0/tags/**", "anon");
        shiroFilterDefinitionMap.put("/v1.0/records/**", "anon");

        //swagger related url.
        shiroFilterDefinitionMap.put("/swagger-ui.html/**", "anon");
        shiroFilterDefinitionMap.put("/swagger-resources/**", "anon");
        shiroFilterDefinitionMap.put("/v2/**", "anon");
        shiroFilterDefinitionMap.put("/webjars/**", "anon");

        shiroFilterDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterDefinitionMap);

        return shiroFilterFactoryBean;

    }

    /**
     * The bean for HashedCredentialsMatcher.
     * @return matcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        val matcher = new HashedCredentialsMatcher();
        //输入hash算法SHA-256
        matcher.setHashAlgorithmName("SHA-256");
        //哈希迭代次数
        matcher.setHashIterations(HASH_ITERATIONS1);
        //是否加盐
        matcher.setHashSalted(true);
        //是否使用base64编码
        matcher.setStoredCredentialsHexEncoded(false);
        return matcher;
    }
}
