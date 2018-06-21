package com.xuzhong.shiro.controller;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptioController {

	
	 private final static Logger logger = LoggerFactory.getLogger(ExceptioController.class);
	 
	 
	 
	    @ExceptionHandler(value = Exception.class)
	    @ResponseBody
	    public String Handle(Exception e){

	        if (e instanceof AuthorizationException){
	        	AuthorizationException authorizationException = (AuthorizationException) e;
	            return authorizationException.getMessage();

	        }else {
	            logger.info("[系统异常]{}",e);
	            return "error";
	        }

	    }
}
