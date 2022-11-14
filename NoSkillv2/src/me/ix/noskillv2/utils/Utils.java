package me.ix.noskillv2.utils;

import java.util.ArrayList;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class Utils {

	/* Placeholder Method */
	public static void log(String input) {
		System.out.println(input);
	}
	
	public static ArrayList<String> getArgumentsFromEvent(SlashCommandInteractionEvent event) {
		ArrayList<String> arguments = new ArrayList<String>();
		
		for(OptionMapping option : event.getOptions()) {
			arguments.add(option.getAsString());
		}
		
		return arguments;
	}

	public static ArrayList<String> getArgumentsFromEvent(MessageReceivedEvent event) {
		ArrayList<String> arguments = new ArrayList<String>();
		
		String contentRaw = event.getMessage().getContentRaw();
		for(String split : contentRaw.substring(1, contentRaw.length()).split(" ")) {
			arguments.add(split);
		}
		arguments.remove(0);
		
		return arguments;
	}
	
}
