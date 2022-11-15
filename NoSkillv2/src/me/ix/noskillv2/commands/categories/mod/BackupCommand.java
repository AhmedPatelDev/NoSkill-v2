package me.ix.noskillv2.commands.categories.mod;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class BackupCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		
	}

	@Override
	public String getName() {
		return "backup";
	}

	@Override
	public String getHelp() {
		return "Backup state of server.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MOD;
	}

	@Override
	public List<OptionData> getArguments() {
		return null;
	}

	
	
}
