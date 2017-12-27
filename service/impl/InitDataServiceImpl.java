package com.wb.plugins.model2table.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wb.plugins.model2table.annotation.InitData;
import com.wb.plugins.model2table.config.ConfigLoder;
import com.wb.plugins.model2table.service.InitDataService;
import com.wb.plugins.model2table.utils.ClassTools;

/**
 * @author 艾小天 
 * @email :wongtp@outlook.com
 * @date ：2017年12月26日 下午7:59:19
 * 
 */
@Transactional
@Service("InitDataService")
public class InitDataServiceImpl implements InitDataService {
	
	private static final Logger	log	= LogManager.getLogger(InitDataServiceImpl.class);
	
	@Override
	public void initData() {
		log.info("\n=========开始初始化数据=======");
		Set<Class<?>> classes = ClassTools.getInitDataClasses(ConfigLoder.getModelPack());
		for (Class<?> clas : classes) {
			Method[] methods = clas.getDeclaredMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(InitData.class)) {
					try {
						method.invoke(clas.newInstance());
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| InstantiationException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		log.info("\n=========初始化数据结束=======");
	}
}
