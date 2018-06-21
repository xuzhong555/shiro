package com.xuzhong.shiro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	
	@RequestMapping("/")
	public String to(){
		
		return "login";
	}
	@RequestMapping("/index")
	public String tologin(){
		
		return "login";
	}
	
	@RequestMapping("/list")
	public String tolist(){
		
		return "list";
	}
	
	@RequestMapping("/unauthorized")
	public String tounauthorized(){
		
		return "unauthorized";
	}
	
	@RequestMapping("/admin")
	public String toadmin(){
		
		return "admin";
	}
	
	@RequestMapping("/user")
	public String touser(){
		
		return "user";
	}
	
	@RequestMapping({"/404","/500","/error"})
	public String to404(){
		
		return "err";
	}
	
}
