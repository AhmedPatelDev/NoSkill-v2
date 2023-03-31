package me.ix.noskillv2.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

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
	
	public List<Member> getMentionedMembers() {
		ArrayList<Member> members = new ArrayList<Member>();
		
		if (e instanceof SlashCommandInteractionEvent) {
			SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) e;
			for(OptionMapping option : event.getOptions()) {
				if(option.getType() == OptionType.USER) {
					members.add(option.getAsMember());
				}
			}
			
		}
		if(e instanceof MessageReceivedEvent) {
			MessageReceivedEvent event = (MessageReceivedEvent) e;
			
			for(Member member : event.getMessage().getMentions().getMembers()) {
				members.add(member);
			}
		}
		return members;
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
