package com.wb.plugins.model2table.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.wb.plugins.model2table.command.Columns;

/**
 * @author 艾小天 
 * @email :wongtp@outlook.com
 * @date ：2017年12月22日 上午9:54:46
 * 
 */
public interface CreateTableService {
	
	/**
	 * 根据结构注解解析出来的信息创建表
	 * @param tableMap 表结构的map
	 */
	public void createTable(@Param("tableMap") Map<String, List<Object>> tableMap, String tableComment);

	/**
	 * 根据表名查询表在库中是否存在，存在返回1，不存在返回0
	 * @param tableName 表结构的map
	 * @return 存在返回1，不存在返回0
	 */
	public int findTableCountByTableName(@Param("tableName") String tableName);
	
	/**
	 * 根据表名查询库中该表的字段结构等信息
	 * @param tableName 表结构的map
	 * @return 表的字段结构等信息
	 */
	public List<Columns> findTableEnsembleByTableName(@Param("tableName") String tableName);
	
	/**
	 * 增加字段
	 * @param tableMap 表结构的map
	 */
	public void addTableField(@Param("tableMap") Map<String, Object> tableMap);
	
	/**
	 * 删除字段
	 * @param tableMap 表结构的map
	 */
	public void removeTableField(@Param("tableMap") Map<String, Object> tableMap);
	
	/**
	 * 修改字段
	 * @param tableMap 表结构的map
	 */
	public void modifyTableField(@Param("tableMap") Map<String, Object> tableMap);
	
	/**
	 * 删除主键约束，附带修改其他字段属性功能
	 * @param tableMap 表结构的map
	 */
	public void dropKeyTableField(@Param("tableMap") Map<String, Object> tableMap);
	
	/**
	 * 删除唯一约束字段，不带修改其他字段属性的功能
	 * @param tableMap 表结构的map
	 */
	public void dropUniqueTableField(@Param("tableMap") Map<String, Object> tableMap);
	
	/**
	 * 根据表名删除表
	 * @param tableName 表结构的map
	 */
	public void dorpTableByName(@Param("tableName") String tableName);
	
	/**
	 * 获取当前连接的数据库的数据记录数
	 */
	public Integer getTableCount();
}
