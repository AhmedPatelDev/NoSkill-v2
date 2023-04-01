package me.ix.noskillv2.commands.categories.misc;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.CommandManager;
import me.ix.noskillv2.NoSkillv2;
import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.database.repo.GuildRepo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		
		CommandManager commandManager = NoSkillv2.bot.getListener().getManager();
		
		EmbedBuilder eb = Utils.getDefaultEmbed(this, true);

		String arg = "";
		if(!arguments.isEmpty()) {
			arg = arguments.get(0);
			
			CommandCategory cat = CommandCategory.getByName(arg);
			
			eb.setDescription(cat.getLabel() + " " + cat.toString() + " COMMANDS");
			
			for(ICommand cmd : commandManager.getCommands()) {
				if(cmd.getCategory().equals(cat)) {
					eb.addField(cmd.getName(), cmd.getHelp(), false);
				}
			}
		} else {
			String prefix = GuildRepo.getPrefix(ctx.getGuild().getIdLong());

			eb.setDescription("Command Categories");
			for(CommandCategory cat : me.ix.noskillv2.commands.CommandCategory.values()) {
				eb.addField("ㅤ" + cat.getLabel() + " " + cat.toString(), "` " + prefix + "help " + cat.toString().toLowerCase() + " `", true);
			}
		}

		ctx.sendMessage("", eb.build());
	}
	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getHelp() {
		return "Get Category list or command list for category.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MISC;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "help", "Command Category", false)
		);
		
		return optionData;
	}
	
}
