package com.wb.plugins.model2table.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wb.plugins.model2table.command.BaseDaoSupport;
import com.wb.plugins.model2table.command.Columns;
import com.wb.plugins.model2table.service.CreateTableService;

/**
 * @author 艾小天 
 * @email :wongtp@outlook.com
 * @date ：2017年12月22日 上午10:03:32
 */
@Transactional
@Service("CreateTableService")
public class CreateTableServiceImpl implements CreateTableService {
	
	@Resource
	private BaseDaoSupport baseDaoSupport;
	
	private SqlSession sqlSession = null;
	
	private SqlSession getSqlSession() {
		if (sqlSession == null) {
			sqlSession = baseDaoSupport.getSession();
		}
		return sqlSession;
	}
	
	private static final String sqlPath = "com.wb.plugins.model2table.service.CreateTableService.";

	@Override
	public void createTable(Map<String, List<Object>> tableMap, String tableComment) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		map.put("tableComment", tableComment);
		getSqlSession().selectOne(sqlPath + "createTable", map);
	}

	@Override
	public int findTableCountByTableName(String tableName) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		return getSqlSession().selectOne(sqlPath + "findTableCountByTableName", map);
	}

	@Override
	public List<Columns> findTableEnsembleByTableName(String tableName) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		return getSqlSession().selectList(sqlPath + "findTableEnsembleByTableName", map);
	}

	@Override
	public void addTableField(Map<String, Object> tableMap) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().selectOne(sqlPath + "addTableField", map);
	}

	@Override
	public void removeTableField(Map<String, Object> tableMap) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().selectOne(sqlPath + "removeTableField", map);
	}

	@Override
	public void modifyTableField(Map<String, Object> tableMap) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().selectOne(sqlPath + "modifyTableField", map);
	}

	@Override
	public void dropKeyTableField(Map<String, Object> tableMap) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().selectOne(sqlPath + "dropKeyTableField", map);
	}

	@Override
	public void dropUniqueTableField(Map<String, Object> tableMap) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableMap", tableMap);
		getSqlSession().selectOne(sqlPath + "dropUniqueTableField", map);
	}

	@Override
	public void dorpTableByName(String tableName) {
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		getSqlSession().selectOne(sqlPath + "dorpTableByName", map);
	}

	@Override
	public Integer getTableCount() {
		return getSqlSession().selectOne(sqlPath + "getTableCount");
	}

}
