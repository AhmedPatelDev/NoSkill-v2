package me.ix.noskillv2.utils.database.repo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.ix.noskillv2.NoSkillv2;

public class GuildRepo {
	
	public static void updatePrefix(long guildId, String newPrefix) {
		 NoSkillv2.PREFIXES.put(guildId, newPrefix);
		 
		 try (final PreparedStatement preparedStatement = NoSkillv2.bot.getSqlConnection()
	                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

			 preparedStatement.setString(1, newPrefix);
	         preparedStatement.setString(2, String.valueOf(guildId));

	         preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	public static String getPrefix(long guildId) {
		String prefix = NoSkillv2.PREFIXES.computeIfAbsent(guildId, k -> {
			try (final PreparedStatement preparedStatement = NoSkillv2.bot.getSqlConnection()
					.prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

				preparedStatement.setString(1, String.valueOf(guildId));

				try (final ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString("prefix");
					}
				}

				try (final PreparedStatement insertStatement = NoSkillv2.bot.getSqlConnection()
						.prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

					insertStatement.setString(1, String.valueOf(guildId));

					insertStatement.execute();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return NoSkillv2.DEFAULT_PREFIX;
		});
		
		return prefix;
	}
}
