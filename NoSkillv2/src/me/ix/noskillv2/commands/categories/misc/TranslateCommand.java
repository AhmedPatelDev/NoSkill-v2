package me.ix.noskillv2.commands.categories.misc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.translation.TranslateUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class TranslateCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		EmbedBuilder eb = Utils.getDefaultEmbed(this, false);
		
		if(arguments.isEmpty()) {
			ctx.sendMessage("Usage: translate [Word]");
			return;
		}
		
		String toTranslate = "";
		for(String arg : arguments) {
			toTranslate += arg + " ";
		}
		
		String[] translation = null;
		try {
			translation = TranslateUtils.translate("en", toTranslate);
			eb.setDescription("Listing the translation for: " + toTranslate + "\n" 
			+ "Language Detected: " + TranslateUtils.getLangFromCode(translation[1]) + " [" + translation[1] + "]");
			eb.addField("Translation", translation[0], false);
		} catch (IOException e) {
			eb.setDescription("Listing the translation for: " + toTranslate + "\n" + "Language Detected: n/a");
			eb.addField("Translation", "Internal error occured", false);
		}
		
		ctx.sendMessage("", eb.build());
		eb.clear();
	}
	
	@Override
	public String getName() {
		return "translate";
	}

	@Override
	public String getHelp() {
		return "Translate a sentence from anything to english.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MISC;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "message", "What the bot will translate.", false)
		);
		
		return optionData;
	}
	
}