package me.ix.noskillv2.utils.database.repo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.ix.noskillv2.NoSkillv2;

public class InitializeTables {

	public static String createTableString(String tableName, String[] columns) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("CREATE TABLE IF NOT EXISTS %s (", tableName));
		
		for(int i = 0; i < columns.length; i++) {
			sb.append(columns[i] + (i == (columns.length - 1) ? "" : ","));
		}
		
		sb.append(");");
		
		return sb.toString();
	}

	public static void setupPrefixTable(Connection connection) {
		String defaultPrefix = NoSkillv2.DEFAULT_PREFIX;

		try (final Statement statement = connection.createStatement()) {
			String tableStatement = createTableString("guild_settings", new String[] {
				"id INTEGER PRIMARY KEY AUTOINCREMENT",
				"guild_id VARCHAR(20) NOT NULL",
				String.format("prefix VARCHAR(255) NOT NULL DEFAULT '%s'", defaultPrefix),
				"guild_name VARCHAR(255)"
			});
			statement.execute(tableStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
