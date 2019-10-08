package com.git.template.general.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="common")
public class CommonConfig {

	private String view;

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	
}
