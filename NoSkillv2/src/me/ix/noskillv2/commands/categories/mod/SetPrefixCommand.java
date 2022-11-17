package me.ix.noskillv2.commands.categories.mod;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.NoSkillv2;
import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.database.repo.GuildRepo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SetPrefixCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
        if (!ctx.getMember().hasPermission(Permission.MANAGE_SERVER)) {
            ctx.sendMessage("You must have the Manage Server permission to use his command");
            return;
        }
        
        final String newPrefix = String.join("", arguments);
        GuildRepo.updatePrefix(ctx.getGuild().getIdLong(), newPrefix);
        
        ctx.sendMessage("Prefix updated to: " + newPrefix);
	}

	@Override
	public String getName() {
		return "setprefix";
	}

	@Override
	public String getHelp() {
		return "Set the prefix for the bot in this server";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MOD;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "prefix", "prefix to set the bot", true)
		);
		
		return optionData;
	}


}
