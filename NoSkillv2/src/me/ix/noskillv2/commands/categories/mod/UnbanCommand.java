package me.ix.noskillv2.commands.categories.mod;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class UnbanCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		
	}

	@Override
	public String getName() {
		return "unban";
	}

	@Override
	public String getHelp() {
		return "Unban a user";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MOD;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "user", "what user to unban", true)
		);
		
		return optionData;
	}

}
