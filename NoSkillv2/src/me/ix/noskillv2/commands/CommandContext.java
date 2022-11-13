package me.ix.noskillv2.commands;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandContext implements ICommandContext {

	private final MessageReceivedEvent event;
	private final List<String> args;
	
	public CommandContext(MessageReceivedEvent event, List<String> args) {
		this.event = event;
		this.args = args;
	}
	
	public Guild getGuild() {
		return this.getEvent().getGuild();
	}
	
	public MessageReceivedEvent getEvent() {
		return event;
	}

	public List<String> getArgs() {
		return args;
	}
	
}