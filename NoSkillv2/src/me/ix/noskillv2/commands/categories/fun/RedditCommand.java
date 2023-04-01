package me.ix.noskillv2.commands.categories.fun;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RedditCommand implements ICommand {
	
	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		
	}

	@Override
	public String getName() {
		return "reddit";
	}

	@Override
	public String getHelp() {
		return "Randomly grabs a post from a specified subreddit";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.FUN;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "reddit", "Reddit to grab post from.", true)
		);
		
		return optionData;
	}

}
