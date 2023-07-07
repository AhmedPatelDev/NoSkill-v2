package me.ix.noskillv2.commands.categories.misc;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.dictionary.Dictionary;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DefineCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		EmbedBuilder eb = Utils.getDefaultEmbed(this, false);
		
		if(arguments.isEmpty()) {
			ctx.sendMessage("Usage: define [Word]");
			return;
		}
		
		if(arguments.size() > 1) {
			ctx.sendMessage("Definition only works with one word");
			return;
		}
		
		String response = Dictionary.getDefinitionResponse(arguments.get(0), "en_GB");
		
		if(!Dictionary.checkIfValid(response)) {
			eb.addField("Error", "Could not find the specified word", false);
			eb.setFooter("Developed by ix");
			ctx.sendMessage("", eb.build());
			eb.clear();
			return;
		}
		
		ArrayList<String> definitionList = Dictionary.parseToArrayList(response);
		
		for(String definition : definitionList) {
			String[] definitionData = definition.split("~");
			
			eb.addField(definitionData[0], definitionData[1], false);
		}
	
		ctx.sendMessage("", eb.build());
		eb.clear();
	}
	
	@Override
	public String getName() {
		return "define";
	}

	@Override
	public String getHelp() {
		return "Define a word.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MISC;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "message", "What the bot will define.", true)
		);
		
		return optionData;
	}
	
}