package com.git.template.general.code.viewer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.git.template.general.aspects.ExceptionHandler;
import com.git.template.general.common.Result;

/**
 * 本类中提供外部访问的接口用于得到src文件夹下的一些文件的内容
 * 该接口中大致应该有如下方法:
 * 	1. String getSrc(File file/ String path):
 * 		得到当前文件内容的方法
 *  2. String getView(String viewName)
 *  	得view的方法
 *  3. R updateSrc(String viewName,String content)
 *  	更新Src的方法
 *  4. R addSrc(String viewName,String content)
 *  	增加Src的方法
 *  5. restart()
 *  	重新启动tomcat的方法
 * @author wangp
 *
 */
@RestController
@RequestMapping("/src")
public class ViewController {
	
	@ExceptionHandler
	@RequestMapping("/single")
	public Result<String> getSrc(String fileName){

		throw new RuntimeException("x");
		// return new Result<String>(new ArrayList<String>(),200,"ok");
	}
	
}
