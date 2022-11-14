package me.ix.noskillv2.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public interface ICommand {

	public void execute(CommandContext ctx, ArrayList<String> arguments);
	
	String getName();
	
	String getHelp();
	
	CommandCategory getCategory();
	
	List<OptionData> getArguments();
	
	default List<String> getAliases(){
		return Arrays.asList();
	}
	
}
