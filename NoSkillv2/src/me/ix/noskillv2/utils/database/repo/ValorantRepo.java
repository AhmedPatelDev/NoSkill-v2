package me.ix.noskillv2.utils.database.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import me.ix.noskillv2.NoSkillv2;

public class ValorantRepo {

	public static class ValorantAccount {
		private String username;
		private String password;
		
		public ValorantAccount(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public String getUsername() {
			return username;
		}
		
		public String getPassword() {
			return password;
		}
	}
	
	public static ValorantAccount getValorantAccountFromId(String userID) {
		String username = "";
		String password = "";
		
		try {
			Connection ds = NoSkillv2.bot.getSqlConnection();
			
			String statement = "SELECT COUNT(1) "
								+ "FROM valorant_accounts "
								+ "WHERE id = ?;";
			PreparedStatement insertStatement = ds.prepareStatement(statement);
			insertStatement.setString(1, String.valueOf(userID));
			ResultSet resultSet = insertStatement.executeQuery();
			
			if (resultSet.next()) {
				username = getStringFromStatement(ds, "username", "id", userID);
				
				StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
				encryptor.setPassword(userID);
				password = encryptor.decrypt(getStringFromStatement(ds, "password", "id", userID));
				
				insertStatement.close();
				
				ValorantAccount acc = new ValorantAccount(username, password);
				
				return acc;
	        } else {
	        	insertStatement.close();
	        	return null;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getStringFromStatement(Connection ds, String whatToGet, String fromWhere, String valueOfFromWhere) throws SQLException {
		PreparedStatement ps = ds.prepareStatement("SELECT " + whatToGet + " FROM valorant_accounts WHERE " + fromWhere + " = ?");
		ps.setString(1, String.valueOf(valueOfFromWhere));
		ResultSet resultSet = ps.executeQuery();
		
		if (resultSet.next()) {
			String result = resultSet.getString(whatToGet);
			ps.close();
			return result;
        }else {
        	ps.close();
        	return null;
        }
	}
	
}
