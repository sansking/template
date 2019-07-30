package com.git.template.test.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.git.template.test.mapper.TestMapper;

@Service
public class TestService {

	
	@Autowired
	private TestMapper testMapper;
	
	public List<Map<String, Object>> getDataList(Map<String,Object> params) {
		
		return null;
	}
	
	private void params(Map<String,Object> params) {
		
		Object size = params.get("pageSize");
		Object number = params.get("pageNumber");
		if(size == null || !size.toString().matches("[1~9]\\d{0,7}")) {
			params.put("pageSize", 1);
		}
		if(number == null || !number.toString().matches("[1~9]\\d{0,7}")) {
			params.put("pageNumber", 20);
		}
		
		int fixedSize = Integer.parseInt(params.get("pageSize").toString());
		int fixedNumber = Integer.parseInt(params.get("pageNumber").toString());
		
		
		
	}

}
