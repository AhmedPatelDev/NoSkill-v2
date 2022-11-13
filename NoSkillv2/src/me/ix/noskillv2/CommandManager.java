package me.ix.noskillv2;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.commands.categories.fun.SayCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

private final List<ICommand> commands = new ArrayList<>();
	
	public CommandManager() {
		addCommand(new SayCommand());
	}

	@SuppressWarnings("unused")
	private void addCommand(ICommand cmd) {
		boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
		
		if(nameFound) {
			throw new IllegalArgumentException("A command with this name is already present");
		}
		
		commands.add(cmd);
	}
	
	public List<ICommand> getCommands(){
		return commands;
	}
	
	@Nullable
	public ICommand getCommand(String search) {
		String searchLower = search.toLowerCase();
		
		for(ICommand cmd : this.commands) {
			if(cmd.getName().toLowerCase().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}
		
		return null;
	}
	

	public void handle(SlashCommandInteractionEvent event) {
        String command = event.getName();
        ICommand cmd = this.getCommand(command);
        
        if(cmd != null) {
            cmd.handle(event);
        }
    }

	public void handle(MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		String command = message.split(" ")[0];
		String commandClean = command.substring(1, command.length());
		
		ICommand cmd = this.getCommand(commandClean);
		if(cmd != null) {
            cmd.handle(event);
        }
	}
}
