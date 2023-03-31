package me.ix.noskillv2.commands.categories.mod;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.database.repo.GuildRepo;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class CleanCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		ArrayList<Message> messagesToDelete = new ArrayList<Message>();
		
		TextChannel channel = (TextChannel) ctx.getChannel();
		
		List<Message> messages = (List<Message>) channel.getHistory().retrievePast(100).complete();
		
		for (Message m : messages) {
			if (m.getAuthor().getIdLong() == ctx.getGuild().getSelfMember().getIdLong()) {
				messagesToDelete.add(m); 	
			}

			if(m.getContentRaw().toLowerCase().startsWith(GuildRepo.getPrefix(ctx.getGuild().getIdLong()))) {
				messagesToDelete.add(m);
			}
		}
		
		channel.purgeMessages(messagesToDelete);
	}

	@Override
	public String getName() {
		return "clean";
	}

	@Override
	public String getHelp() {
		return "Clean recent bot commands.";
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
