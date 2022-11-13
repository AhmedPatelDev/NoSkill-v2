package me.ix.noskillv2.commands.categories.fun;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SayCommand implements ICommand {

	@Override
	public void handle(MessageReceivedEvent event) {
		String contentRaw = event.getMessage().getContentRaw();
		
		ArrayList<String> arguments = new ArrayList<String>();
		
		for(String split : contentRaw.substring(1, contentRaw.length()).split(" ")) {
			arguments.add(split);
		}
		arguments.remove(0);
		
		execute(event, arguments);
	}
	
	@Override
	public void handle(SlashCommandInteractionEvent event) {
		ArrayList<String> arguments = new ArrayList<String>();
		for(OptionMapping option : event.getOptions()) {
			arguments.add(option.getAsString());
		}
		
		execute(event, arguments);
	}

	public void execute(Event event, ArrayList<String> arguments) {
		if(event instanceof MessageReceivedEvent) {
			((MessageReceivedEvent) event).getMessage().reply("You made me say: " + arguments.get(0)).queue();
			
		}
		if(event instanceof SlashCommandInteractionEvent) {
			((SlashCommandInteractionEvent) event).reply("You made me say: " + arguments.get(0)).queue();
			
		}
	}
	
	@Override
	public String getName() {
		return "say";
	}

	@Override
	public String getHelp() {
		return "Make the bot say something ephemerally";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.FUN;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "message", "What the bot will say.", true)
		);
		
		return optionData;
	}
	
}
