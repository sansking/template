package com.git.template.test.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.git.template.general.common.Result;
import com.git.template.test.service.TestService;

@Controller
@RequestMapping("/sys")
public class TestController {
	
	@Autowired
	TestService testService;
	
	@RequestMapping("/test.action")
	public Result<Map<String,Object>> getTest(@RequestParam(required=true)Map<String,Object> params) {
		
		Result<Map<String,Object>> r = new Result<>();
		
		try {
			List<Map<String,Object>> dataList = testService.getDataList(params);		
			r.setCode(200);
			r.setMsg("传输成功");
			r.setDataList(dataList);
			return r;
		}catch(Exception e) {
			r.setCode(500);
			r.setMsg("发生异常"+e);
			return r;
		}
		
	}
}
