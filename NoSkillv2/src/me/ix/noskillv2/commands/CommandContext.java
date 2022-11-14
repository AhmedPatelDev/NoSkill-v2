package me.ix.noskillv2.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandContext {

	Event e;
	
	public CommandContext(Event e) {
		this.e = e;
	}
	
	public Event getEvent() {
		return e;
	}
	
	public JDA getJDA() {
		return e.getJDA();
	}
	
	public Member getMember() {
		if (e instanceof SlashCommandInteractionEvent) {
			return ((SlashCommandInteractionEvent) e).getMember();
		}
		if(e instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) e).getMember();
		}
		return null;
	}
	
	public Guild getGuild() {
		if (e instanceof SlashCommandInteractionEvent) {
			return ((SlashCommandInteractionEvent) e).getGuild();
		}
		if(e instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) e).getGuild();
		}
		return null;
	}
	
	public Channel getChannel() {
		if (e instanceof SlashCommandInteractionEvent) {
			return ((SlashCommandInteractionEvent) e).getChannel();
		}
		if(e instanceof MessageReceivedEvent) {
			return ((MessageReceivedEvent) e).getChannel();
		}
		return null;
	}
	
	public void sendMessage(String message, MessageEmbed embed) {
		if(e instanceof MessageReceivedEvent) {
			if(embed == null) {
				((MessageReceivedEvent) e).getMessage().reply(message).queue();
			} else {
				((MessageReceivedEvent) e).getMessage().reply(message).setEmbeds(embed).queue();
			}
		}
		if(e instanceof SlashCommandInteractionEvent) {
			if(embed == null) {
				((SlashCommandInteractionEvent) e).reply(message).queue();
			} else {
				((SlashCommandInteractionEvent) e).reply(message).setEmbeds(embed).queue();
			}
		}
	}
	
	public void sendMessage(String message) {
		if(e instanceof MessageReceivedEvent) {
			((MessageReceivedEvent) e).getMessage().reply(message).queue();
		}
		if(e instanceof SlashCommandInteractionEvent) {
			((SlashCommandInteractionEvent) e).reply(message).queue();
		}
	}
}
