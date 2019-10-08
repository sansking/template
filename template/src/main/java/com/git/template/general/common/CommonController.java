package com.git.template.general.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.git.template.general.exceptions.PermissionCheckException;

@Controller
@RequestMapping("/common")
public class CommonController {
	
	@Autowired
	private CommonConfig config;
	
	@Autowired
	private CommonService service;
	
	@RequestMapping("/view")
	public String getView(){
		return config.getView();
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public List<Map<String, Object>> getData(@RequestParam String sql,@RequestParam String checksum){
		if(check(checksum)){
			return service.getData(sql);
		}else{
			throw new PermissionCheckException();
		}
	}

	private boolean check(String checksum) {
		return true;
	}
}
