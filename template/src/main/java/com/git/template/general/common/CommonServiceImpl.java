package com.git.template.general.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonServiceImpl implements CommonService{

	@Autowired
	private CommonDao dao;
	
	@Override
	public List<Map<String, Object>> getData(String sql) {
		if(checkSql(sql)){
			return dao.getData(sql);
		}else{
			throw new RuntimeException();
		}
	}

	private boolean checkSql(String sql) {
		return true;
	}

}
