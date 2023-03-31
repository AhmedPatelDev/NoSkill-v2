package me.ix.noskillv2.commands.categories.mod;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class KickCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		List<Member> mentionedMembers = ctx.getMentionedMembers();
		Member executor = ctx.getMember();
		Member target = mentionedMembers.get(0);
		Member selfUser = ctx.getGuild().getSelfMember();
	}

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String getHelp() {
		return "kicks a user from the server.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MOD;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.USER, "user", "what user to kick", true)
		);
		
		return optionData;
	}

}
