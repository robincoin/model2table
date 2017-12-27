package com.wb.plugins.model2table.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * 用于加载 classPath 下的 model2table.properties 文件
 * @author 艾小天 
 * @email :wongtp@outlook.com
 * @date ：2017年12月22日 上午9:44:10
 */
@Component
public class ConfigLoder {
	
	private static final Logger logger = LogManager.getLogger(ConfigLoder.class);
	private static Properties props = null;
	
	private static List<String> modelPack = null;
	
	static {
		modelPack = new ArrayList<>();
		InputStream inputStream = null;
		try {
			props = new Properties();
			Resource fileRource = new ClassPathResource("model2table.properties");
			inputStream = fileRource.getInputStream();
			
			if(inputStream == null) {
				logger.error("配置文件 classpath:model2table.properties 读取失败，可能文件不存在或者命名不正确。");
			}
			props.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getProperty(String key) {
		return getProperty(key, "");
	}
	
	public static String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}
	
	public static List<String> getModelPack() {
		return modelPack;
	}

	public static void setModelPack(List<String> modelPack) {
		ConfigLoder.modelPack = modelPack;
	}

}
