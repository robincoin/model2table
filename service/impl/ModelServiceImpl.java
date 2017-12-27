package com.wb.plugins.model2table.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wb.base.dao.BaseDao;
import com.wb.plugins.model2table.annotation.Column;
import com.wb.plugins.model2table.annotation.Table;
import com.wb.plugins.model2table.command.BaseDaoSupport;
import com.wb.plugins.model2table.service.ModelService;


@Transactional
@Service("ModelService")
public class ModelServiceImpl implements ModelService {

	private static final Logger	log	= LogManager.getLogger(ModelServiceImpl.class);
	
	private static final String KEYFIELDMAP = "keyFieldMap";
	
	@Resource
	private BaseDaoSupport baseDaoSupport;
	
	private SqlSession sqlSession = null;
	
	private SqlSession getSqlSession() {
		if (sqlSession == null) {
			sqlSession = baseDaoSupport.getSession();
		}
		return sqlSession;
	}
	
	private static final String sqlPath = "com.wb.plugins.model2table.service.ModelService.";

	public <T> void insert(T obj) {
		Table tableName = obj.getClass().getAnnotation(Table.class);
		if ((tableName == null) || (tableName.name() == null || tableName.name() == "")) {
			log.error("必须使用model中的对象！");
			return;
		}
		Field[] declaredFields = getAllFields(obj);
		Map<Object, Map<Object, Object>> tableMap = new HashMap<Object, Map<Object, Object>>();
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		for (Field field : declaredFields) {
			try{
				// 私有属性需要设置访问权限
				field.setAccessible(true);
				System.out.println("\n\n\n\n========field.getName():" + field.getName());
				Column column = field.getAnnotation(Column.class);
				if (column == null) {
					log.info("该field没有配置注解不是表中的字段！");
					continue;
				}

				// 如果是自增,并且是保存的场合，不需要添加到map中做保存
				if (column.isAutoIncrement()) {
					log.info("字段：" + field.getName() + "是自增的不需要添加到map中");
					continue;
				}

				dataMap.put(field.getName(), field.get(obj));
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			}catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		tableMap.put(tableName.name(), dataMap);
		// 执行保存操作
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().insert(sqlPath + "save", map);
	}
	
	public <T> void update(T obj) {
		Table tableName = obj.getClass().getAnnotation(Table.class);
		if ((tableName == null) || (tableName.name() == null || tableName.name() == "")) {
			log.error("必须使用model中的对象！");
			return;
		}
		Field[] declaredFields = getAllFields(obj);
		Map<Object, Map<Object, Object>> tableMap = new HashMap<Object, Map<Object, Object>>();
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		Map<String, Object> keyFieldMap = new HashMap<String, Object>();
		for (Field field : declaredFields) {
			try{
				// 私有属性需要设置访问权限
				field.setAccessible(true);
				System.out.println("\n\n\n\n========field.getName():" + field.getName());
				Column column = field.getAnnotation(Column.class);
				if (column == null) {
					log.info("该field没有配置注解不是表中的字段！");
					continue;
				}
				
				// 如果是自增,并且是保存的场合，不需要添加到map中做保存
				if (column.isAutoIncrement()) {
					log.info("字段：" + field.getName() + "是自增的不需要添加到map中");
					continue;
				}

				// 如果是主键，并且不是空的时候，这时候应该是更新操作
				if (column.isKey() && field.get(obj) != null) {
					keyFieldMap.put(field.getName(), field.get(obj));
					continue;
				}
				dataMap.put(field.getName(), field.get(obj));
			}catch (IllegalArgumentException e) {
				e.printStackTrace();
			}catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		dataMap.put(KEYFIELDMAP, keyFieldMap);
		tableMap.put(tableName.name(), dataMap);
		// 执行更新操作根据主键
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().update(sqlPath + "update", map);
	}

	private <T> Field[] getAllFields(T obj) {
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		
		// 递归扫描父类的filed
		declaredFields = recursionParents(obj.getClass(), declaredFields);
		return declaredFields;
	}

	public <T> void delete(T obj) {
		// 得到表名
		Table tableName = obj.getClass().getAnnotation(Table.class);
		if ((tableName == null) || (tableName.name() == null || tableName.name() == "")) {
			log.error("必须使用model中的对象！");
			return;
		}
		Field[] declaredFields = getAllFields(obj);
		Map<Object, Map<Object, Object>> tableMap = new HashMap<Object, Map<Object, Object>>();
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		for (Field field : declaredFields){
			// 设置访问权限
			field.setAccessible(true);
			// 得到字段的配置
			Column column = field.getAnnotation(Column.class);
			if (column == null) {
				log.info("该field没有配置注解不是表中在字段！");
				continue;
			}
			try{
				dataMap.put(column.name(), field.get(obj));
			}catch (IllegalArgumentException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
		}
		tableMap.put(tableName.name(), dataMap);
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().delete(sqlPath + "delete", map);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> query(T obj) {
		// 得到表名
		Table tableName = obj.getClass().getAnnotation(Table.class);
		if ((tableName == null) || (tableName.name() == null || tableName.name() == "")) {
			log.error("必须使用model中的对象！");
			return null;
		}
		Field[] declaredFields = getAllFields(obj);
		Map<Object, Map<Object, Object>> tableMap = new HashMap<Object, Map<Object, Object>>();
		Map<Object, Object> dataMap = new HashMap<Object, Object>();
		for (Field field : declaredFields){
			// 设置访问权限
			field.setAccessible(true);
			// 得到字段的配置
			Column column = field.getAnnotation(Column.class);
			if (column == null) {
				log.info("该field没有配置注解不是表中在字段！");
				continue;
			}
			try{
				dataMap.put(column.name(), field.get(obj));
			}catch (IllegalArgumentException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}
		}
		tableMap.put(tableName.name(), dataMap);
		Map<String, Object> param = new HashMap<>();
		param.put("tableMap", tableMap);
		List<Map<String, Object>> query = getSqlSession().selectList(sqlPath + "query", param);
		List<T> list = new ArrayList<T>();
		try{
			for (Map<String, Object> map : query) {
				T newInstance = (T) obj.getClass().newInstance();
				Field[] declaredFields2 = newInstance.getClass().getDeclaredFields();
				for (Field field : declaredFields2){
					field.setAccessible(true);
					// 得到字段的配置
					Column column = field.getAnnotation(Column.class);
					if (column == null) {
						log.info("该field没有配置注解不是表中在字段！");
						continue;
					}
					String name = field.getName();
					field.set(newInstance, map.get(name));
				}
				list.add(newInstance);
			}
		}catch (InstantiationException e){
			e.printStackTrace();
		}catch (IllegalAccessException e){
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 递归扫描父类的fields
	 * @param clas
	 * @param fields
	 */
	@SuppressWarnings("rawtypes")
	private Field[] recursionParents(Class<?> clas, Field[] fields) {
		if(clas.getSuperclass()!=null){
			Class clsSup = clas.getSuperclass();
			fields = (Field[]) ArrayUtils.addAll(fields,clsSup.getDeclaredFields());
			fields = recursionParents(clsSup, fields);
		}
		return fields;
	}

}
