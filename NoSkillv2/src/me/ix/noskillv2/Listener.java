package me.ix.noskillv2;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Listener extends ListenerAdapter {
	
	private final CommandManager manager = new CommandManager();
	
	@Override
	public void onReady(ReadyEvent event) {
		Utils.log(event.getJDA().getSelfUser().getName() + " is ready");
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		String prefixReceived = message.substring(0, 1);
		
		if(prefixReceived.equals("-")) {
			manager.handle(event);
		}
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
		manager.handle(event);
	}
	
	@Override
	public void onGuildReady(GuildReadyEvent event) {
		addCommandData(event);
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		addCommandData(event);
	}
	
	public void addCommandData(Event event) {
		List<CommandData> commandData = new ArrayList<CommandData>();

		for(ICommand cmd : manager.getCommands()) {
			commandData.add(Commands.slash(cmd.getName(), cmd.getHelp()).addOptions(cmd.getArguments()));
		}
		
		((GenericGuildEvent) event).getGuild().updateCommands().addCommands(commandData).queue();
	}
}
