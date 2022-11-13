package me.ix.noskillv2.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public interface ICommand {

	void handle(MessageReceivedEvent event);
	void handle(SlashCommandInteractionEvent event);
	
	String getName();
	
	String getHelp();
	
	CommandCategory getCategory();
	
	List<OptionData> getArguments();
	
	default List<String> getAliases(){
		return Arrays.asList();
	}
	
}
