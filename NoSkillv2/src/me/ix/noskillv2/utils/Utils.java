package me.ix.noskillv2.utils;

import java.awt.Color;
import java.util.ArrayList;

import me.ix.noskillv2.NoSkillv2;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
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
	
	public static EmbedBuilder getDefaultEmbed(ICommand cmd, boolean colorBar) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(Color.magenta);
		embed.setTitle(NoSkillv2.BOT_NAME + " - " + cmd.getName());
		embed.setFooter("Developed by ix", "https://cdn.discordapp.com/avatars/196675844683464705/c48427a14519a5b1abf196391d7a9bbf?size=1024");
		
		if(colorBar) {
			embed.setImage("https://i.imgur.com/9hVqfby.gif");
		}
		
		return embed;
	}
}
