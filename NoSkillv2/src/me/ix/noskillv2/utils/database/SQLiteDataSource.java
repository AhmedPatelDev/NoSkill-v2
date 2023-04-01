package me.ix.noskillv2.utils.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SQLiteDataSource {

	private HikariConfig config = new HikariConfig();
	private HikariDataSource ds;
	
	private Connection dbConnection;
	
	public SQLiteDataSource() {
		try {
			final File dbFile = new File("bot_database.db");
			
			if(!dbFile.exists()) {
				if(dbFile.createNewFile()) {
					System.out.println("Created database file");
				}else {
					System.out.println("Could not create database file");
				}
			}
			
		}catch(IOException exc) {
			exc.printStackTrace();
		}
		
        config.setJdbcUrl("jdbc:sqlite:bot_database.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}
	
	public Connection getConnection() throws SQLException {
		if(dbConnection == null || dbConnection.isClosed()) {
			dbConnection = ds.getConnection();
		}
		
		return dbConnection;
	}
	
}
