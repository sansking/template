package com.git.template.general.excel.core;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;

public interface ExcelController {
	
	public ExcelService<?> getService();
	
	@RequestMapping("/getList")
	default public List<?> getList(){
		return getService().getList();
	}
	
}
