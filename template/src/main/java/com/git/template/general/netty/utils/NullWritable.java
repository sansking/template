package com.git.template.general.netty.utils;

import java.io.Serializable;

public class NullWritable implements Serializable {

	private static final long serialVersionUID = -8191640400484155111L;
	private static NullWritable instance = new NullWritable();

	private NullWritable() {
	}

	public static NullWritable nullWritable() {
		return instance;
	}
}