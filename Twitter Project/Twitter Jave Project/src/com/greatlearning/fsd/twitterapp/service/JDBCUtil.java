package com.greatlearning.fsd.twitterapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public final class JDBCUtil {
	private static Optional<Connection> OptionalConnection = Optional.ofNullable(null);
	
	private static final String URL = "jdbc:mysql://localhost:3306/twitter_db";
	private static final String USERNAME = "Preetom";
	private static final String PASSWORD = "Preetom@sql";

	
	public static Optional<Connection> getConnection() throws SQLException {
		if(OptionalConnection.isPresent() == false) {
			try {
				OptionalConnection = Optional.of(DriverManager.getConnection(URL, USERNAME, PASSWORD));
			} catch (SQLException e) {
				throw e;
			}
		}
		
		return OptionalConnection;
	}
}
