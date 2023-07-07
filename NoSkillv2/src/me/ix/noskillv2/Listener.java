package me.ix.noskillv2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.database.SQLiteDataSource;
import me.ix.noskillv2.utils.database.repo.GuildRepo;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Listener extends ListenerAdapter {
	
	private final CommandManager manager = new CommandManager();
	
	public CommandManager getManager() {
		return manager;
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		Utils.log(event.getJDA().getSelfUser().getName() + " is ready");
		
		List<CommandData> commandData = new ArrayList<CommandData>();

		for(ICommand cmd : manager.getCommands()) {
			if(cmd.getArguments() != null) {
				commandData.add(Commands.slash(cmd.getName(), cmd.getHelp()).addOptions(cmd.getArguments()));
			} else {
				commandData.add(Commands.slash(cmd.getName(), cmd.getHelp()));
			}
		}
		
		event.getJDA().updateCommands().addCommands(commandData).queue();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		if (message.toLowerCase().contains("-valorantlogin")) {
			String[] msgSplit = message.split(" ");

			if (msgSplit.length != 3) {
				event.getMessage().reply("Correct Usage: `-ValorantLogin username password`").queue();
				return;
			}

			String seed = event.getAuthor().getId();
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword(seed);
			
			String username = msgSplit[1];
			String password = encryptor.encrypt(msgSplit[2]);

			try {
				Connection ds = NoSkillv2.bot.getSqlConnection();

				String statement = "REPLACE INTO valorant_accounts(id, username, password) VALUES(?, ?, ?)";
				PreparedStatement insertStatement = ds.prepareStatement(statement);

				insertStatement.setString(1, String.valueOf(event.getAuthor().getId()));
				insertStatement.setString(2, String.valueOf(username));
				insertStatement.setString(3, String.valueOf(password));
				insertStatement.execute();
				insertStatement.close();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}

			event.getMessage().reply("Your Discord and Valorant accounts are now linked!"
					+ "\n- I **STRONGLY** suggest you delete your message containing your password."
					+ "\n- **ALL** passwords are stored encrypted.").queue();
			
			return;
		}
		
		String guildPrefix = GuildRepo.getPrefix(event.getGuild().getIdLong());
		
		if(message.length() < guildPrefix.length()) {
			return;
		}
		
		String prefixReceived = message.substring(0, guildPrefix.length());
		
		if (!event.getMessage().getMentions().getMembers().isEmpty()) {
			if (event.getMessage().getMentions().getMembers().get(0).getIdLong() == event.getGuild().getSelfMember().getIdLong()) {
				event.getChannel().sendMessage("My prefix in this discord is: " + guildPrefix + "\nUse `" + guildPrefix
						+ "help` to get a list of commands").queue();
			}
		}
		
		if(prefixReceived.trim().equals(guildPrefix.trim())) {
			manager.handle(event);
		}
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		manager.handle(event);
	}
	
	@Override
	public void onStringSelectInteraction(StringSelectInteractionEvent event) {
		
	}
	
	@Override
	public void onGuildReady(GuildReadyEvent event) {
		event.getGuild().updateCommands().queue();
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		event.getGuild().updateCommands().queue();
	}
	
	public void addCommandData(Event event) {
		List<CommandData> commandData = new ArrayList<CommandData>();

		for(ICommand cmd : manager.getCommands()) {
			if(cmd.getArguments() != null) {
				commandData.add(Commands.slash(cmd.getName(), cmd.getHelp()).addOptions(cmd.getArguments()));
			} else {
				commandData.add(Commands.slash(cmd.getName(), cmd.getHelp()));
			}
		}
		
		((GenericGuildEvent) event).getGuild().updateCommands().addCommands(commandData).queue();
	}
}
