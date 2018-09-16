package com.piggysoft.bobing.Constant;

import java.util.concurrent.ConcurrentHashMap;

public class GlobalMap {
	// 存放cmd 命令的map
		public static ConcurrentHashMap<String, String> userSessionkeyMap = 
				new ConcurrentHashMap<String, String>();
}
