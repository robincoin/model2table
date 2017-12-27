package com.wb.plugins.model2table.command;

import org.apache.ibatis.session.SqlSession;

public interface BaseDaoSupport {
	
	public SqlSession getSession();
	
}
