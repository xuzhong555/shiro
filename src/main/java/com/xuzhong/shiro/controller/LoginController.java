package com.xuzhong.shiro.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xuzhong.shiro.service.ShiroService;

@Controller
@RequestMapping("shiro")
public class LoginController {
	
	@Autowired
	private ShiroService shiroService;

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/login")
	public String dologin(@RequestParam(value="username",required=false) String username,
			@RequestParam(value="password",required=false) String password){
		
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
        	// 把用户名和密码封装为 UsernamePasswordToken 对象
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            // rememberme
            token.setRememberMe(true);
            try {
            	System.out.println("1. " + token.hashCode());
            	// 执行登录. 实际执行realm中的doGetAuthenticationInfo方法
                currentUser.login(token);
            } 
            // ... catch more exceptions here (maybe custom ones specific to your application?
            // 所有认证时异常的父类. 
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            	System.out.println("登录失败: " + ae.getMessage());
            }
        }
		
		return "redirect:/list";
	}
	
	@RequestMapping("/testShiroAnnotation")
	public String totestShiroAnnotation(HttpSession session){
		session.setAttribute("key", "value");
		shiroService.testMethod();
		return "list";
	}
}
