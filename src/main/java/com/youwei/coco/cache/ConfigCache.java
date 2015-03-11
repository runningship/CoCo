package com.youwei.coco.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class ConfigCache {

	private static Properties props = new Properties();
	
	private static final String confFilePath = "D:\\conf\\coco.properties";
//	private static final String confFilePath = "/home/b2btest/coco/conf/coco.properties";
	static{
		load();
	}
	private static void load(){
		try {
			File file = new File(confFilePath);
			if(file.exists()){
				props.load(FileUtils.openInputStream(file));
			}else{
				InputStream is = ConfigCache.class.getResourceAsStream("coco.properties");
				props.load(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload(){
		props.clear();
		load();
	}
	
	public static String get(String key , String def){
		String val =  props.getProperty(key);
		if(StringUtils.isNotEmpty(val)){
			return val;
		}
		return def;
	}
	
	public static Set<Object> keySet(){
		return props.keySet();
	}
}
