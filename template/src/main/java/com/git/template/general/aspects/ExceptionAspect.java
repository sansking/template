package com.git.template.general.aspects;

import java.util.ArrayList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.git.template.general.common.Result;


@Aspect
@Component
public class ExceptionAspect {
	
	
	@Pointcut("@annotation(com.git.template.general.aspects.ExceptionHandler)")
	public void exceptPointcut(){}
	
	
	//该方法用于捕获异常并向前端发送异常相关信息
	@Around("exceptPointcut()")
	public Object around(ProceedingJoinPoint point){
		try{
			Object result = point.proceed(point.getArgs());
			return result;
		}catch(Throwable t){
			return new Result<Object>(new ArrayList<Object>(),500,t.getMessage());
		}
	}
	
}
