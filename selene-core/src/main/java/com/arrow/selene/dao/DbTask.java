package com.arrow.selene.dao;

import java.sql.Connection;
import java.sql.SQLException;

interface DbTask<R> {
	R execute(Connection connection) throws SQLException;
}