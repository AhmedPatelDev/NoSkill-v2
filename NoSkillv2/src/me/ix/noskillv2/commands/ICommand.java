package me.ix.noskillv2.commands;

import java.util.Arrays;
import java.util.List;

public interface ICommand {

	void handle(CommandContext ctx);
	
	String getName();
	
	String getHelp();
	
	CommandCategory getCategory();
	
	default List<String> getAliases(){
		return Arrays.asList();
	}
	
}
