package com.git.template.general.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonDao {
	List<Map<String, Object>> getData(String sql);

}
