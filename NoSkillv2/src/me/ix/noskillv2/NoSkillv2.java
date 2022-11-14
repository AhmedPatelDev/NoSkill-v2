package me.ix.noskillv2;

import javax.security.auth.login.LoginException;

import me.ix.noskillv2.utils.Utils;
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
	
	private final ShardManager shardManager;
	private final ConfigManager configManager;

	public NoSkillv2() throws LoginException {
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

		shardManager.addEventListener(new Listener());
	}

	public ShardManager getShardManager() {
		return shardManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public static void main(String[] args) {
		try {
			NoSkillv2 bot = new NoSkillv2();
		} catch (LoginException e) {
			Utils.log("ERROR: Invalid Token!");
		}
	}

}
