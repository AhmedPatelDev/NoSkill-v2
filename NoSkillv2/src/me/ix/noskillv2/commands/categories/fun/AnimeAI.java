package me.ix.noskillv2.commands.categories.fun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AnimeAI implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		
	}
	
	public static int getRandomNumber(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}
	
	@Override
	public String getName() {
		return "AnimeAI";
	}

	@Override
	public String getHelp() {
		return "Generates stitched images from thisanimedoesnotexist.ai";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.FUN;
	}


	@Override
	public List<OptionData> getArguments() {
		return null;
	}
}
