//package com.xuzhong.shiro.config;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.Filter;
//
//import org.apache.log4j.Logger;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
//import org.apache.shiro.authc.pam.AuthenticationStrategy;
//import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.mgt.RememberMeManager;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.realm.Realm;
//import org.apache.shiro.spring.LifecycleBeanPostProcessor;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.subject.SubjectContext;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.xuzhong.shiro.realm.SecondRealm;
//import com.xuzhong.shiro.realm.ShiroRealm;
//
//@Configuration
//@EnableTransactionManagement
//public class ShiroConfiguration {
//
//	private final Logger logger = Logger.getLogger(ShiroConfiguration.class);
//	
//	
//	/**
//	 *  配置 CacheManager
//	 */
//	@Bean
//	public EhCacheManager getEhCacheManager() {
//		EhCacheManager ehcacheManager = new EhCacheManager();
//		ehcacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
//		return ehcacheManager;
//	}
//	
//	/**
//	 *  配置 Realm
//	 */
//	public ShiroRealm myShiroRealm(HashedCredentialsMatcher matcher) {
//		ShiroRealm shiroRealm = new ShiroRealm();
//		shiroRealm.setCredentialsMatcher(matcher);
//		return shiroRealm;
//	}
//	public SecondRealm mySecondRealm(HashedCredentialsMatcher matcher) {
//		SecondRealm secondRealm = new SecondRealm();
//		secondRealm.setCredentialsMatcher(matcher);
//		return secondRealm;
//	}
//	
//	
//	/**
//	 * 配置 LifecycleBeanPostProcessor. 可以自定的来调用配置在 Spring IOC 容器中 shiro bean
//	 * 的生命周期方法.
//	 */
//	@Bean
//	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//		return new LifecycleBeanPostProcessor();
//	}
//
//	/**
//	 * 启用 IOC 容器中使用 shiro 的注解. 但必须在配置了 LifecycleBeanPostProcessor 之后才可以使用.
//	 */
//	@Bean
//	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
//		proxyCreator.setProxyTargetClass(true);
//		return proxyCreator;
//
//	}
//
//
//	/**
//	 * 配置 SecurityManager
//	 * 
//	 * @return
//	 */
//	@Bean
//	public SecurityManager securityManager() {
//		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//
//		// 设置realm.
//		securityManager.setAuthenticator(modularRealmAuthenticator());
//		List<Realm> realms = new ArrayList<>();
//		realms.add(myShiroRealm(hashedCredentialsMatcherMD5()));
//		realms.add(mySecondRealm(hashedCredentialsMatcherSHA1()));
//		securityManager.setRealms(realms);
//		// 配置 CacheManager.
//		securityManager.setCacheManager(getEhCacheManager());
//		// 自定义session管理
//		securityManager.setSessionManager(new ServletContainerSessionManager());
//		return securityManager;
//	}
//	/**
//	 * ModularRealmAuthenticator配置多realm
//	 * 不同reaml对用不同加密算法,应对不同数据库不同的加密算法
//	 * 将realms设置在securityManager,源码中会强转为此属性
//	 */
//	@Bean
//	public ModularRealmAuthenticator modularRealmAuthenticator(){
//		ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
//		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
//		return modularRealmAuthenticator;
//	}
//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
//		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//		advisor.setSecurityManager(securityManager());
//		return advisor;
//	}
//
//	/**
//	 * ShiroFilterFactoryBean 处理拦截资源文件问题。
//	 * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
//	 * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager Filter Chain定义说明
//	 * 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
//	 * 3、部分过滤器可指定参数，如perms，roles
//	 */
//	@Bean
//	public ShiroFilterFactoryBean shiroFilter() {
//		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//
//		// 必须设置 SecurityManager
//		shiroFilterFactoryBean.setSecurityManager(securityManager());
//
//		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//		shiroFilterFactoryBean.setLoginUrl("/index");
//		// 登录成功后要跳转的链接
//		shiroFilterFactoryBean.setSuccessUrl("/list");
//		// 未授权界面;
//		shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");		
//
//		// 自定义拦截器
//		Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
//		// 限制同一帐号同时在线的个数。
//		// filtersMap.put("kickout", kickoutSessionControlFilter());
//		shiroFilterFactoryBean.setFilters(filtersMap);
//
//		// 权限控制map.
//		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//		// 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 ,否则之后的有可能失效
//		// authc:所有url都必须认证通过才可以访问;
//		//anon:所有url都都可以匿名访问; 
//		//logout:登出
//		//roles: 角色过滤器
//		filterChainDefinitionMap.put("/login", "anon");
//		filterChainDefinitionMap.put("/shiro/login", "anon");
//		filterChainDefinitionMap.put("/shiro/logout", "logout");
//		filterChainDefinitionMap.put("/user", "authc,roles[user]");
//		filterChainDefinitionMap.put("/admin", "authc,roles[admin]");
//		
//		
//		filterChainDefinitionMap.put("/**", "authc");
//		
//		/*
//		 * List<SysPermissionInit> list = sysPermissionInitService.selectAll();
//		 * 
//		 * for (SysPermissionInit sysPermissionInit : list) {
//		 * filterChainDefinitionMap.put(sysPermissionInit.getUrl(),
//		 * sysPermissionInit.getPermissionInit()); }
//		 */
//
//		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//		return shiroFilterFactoryBean;
//	}
//	
//	/**
//     * 密码匹配凭证管理器
//     *
//     * @return
//     */
//    @Bean(name = "hashedCredentialsMatcherMD5")
//    public HashedCredentialsMatcher hashedCredentialsMatcherMD5() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        // 采用MD5方式加密
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        // 设置加密次数
//        hashedCredentialsMatcher.setHashIterations(1024);
//        return hashedCredentialsMatcher;
//    }
//    
//    /**
//     * 密码匹配凭证管理器
//     *
//     * @return
//     */
//    @Bean(name = "hashedCredentialsMatcherSHA1")
//    public HashedCredentialsMatcher hashedCredentialsMatcherSHA1() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        // 采用MD5方式加密
//        hashedCredentialsMatcher.setHashAlgorithmName("SHA1");
//        // 设置加密次数
//        hashedCredentialsMatcher.setHashIterations(1024);
//        return hashedCredentialsMatcher;
//    }
//}