package com.wb.plugins.model2table.command;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class BaseDaoSupportImpl extends SqlSessionDaoSupport implements BaseDaoSupport {
	
	public SqlSession getSession() {
		return this.getSqlSession();
	}
	
}
