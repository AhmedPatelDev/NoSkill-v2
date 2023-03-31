package me.ix.noskillv2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.database.SQLiteDataSource;
import me.ix.noskillv2.utils.database.repo.InitializeTables;
import me.ix.noskillv2.utils.webserver.WebServer;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class NoSkillv2 {

	public static String DEFAULT_PREFIX = "-";
	public static final Map<Long, String> PREFIXES = new HashMap<>();
	
	private final ShardManager shardManager;
	private final ConfigManager configManager;
	private final Connection sqlConnection;
	
	public static NoSkillv2 bot;
	
	public NoSkillv2() throws LoginException, SQLException {
		configManager = new ConfigManager();
		String token = configManager.getValueFromConfig("TOKEN");

		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
		builder.setActivity(Activity.watching("in development."));

		for (GatewayIntent intent : GatewayIntent.values()) {
			builder.enableIntents(intent);
		}

		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.setChunkingFilter(ChunkingFilter.ALL);

		for (CacheFlag flag : CacheFlag.values()) {
			builder.enableCache(flag);
		}

		shardManager = builder.build();

		Listener listener = new Listener();
		shardManager.addEventListener(listener);
		
		WebServer ws = new WebServer(690, listener.getManager(), shardManager);
		ws.start();
		
		SQLiteDataSource ds = new SQLiteDataSource();
		sqlConnection = ds.getConnection();
		InitializeTables.setupPrefixTable(sqlConnection);
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public Connection getSqlConnection() {
		return sqlConnection;
	}
	
	public static void main(String[] args) {
		try {
			bot = new NoSkillv2();
		} catch (LoginException e) {
			Utils.log("ERROR: Invalid Token!");
		} catch (SQLException e) {
			Utils.log("ERROR: Issue occurred with SQL Connection!");
		}
	}
	
}
