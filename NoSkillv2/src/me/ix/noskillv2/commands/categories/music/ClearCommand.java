package me.ix.noskillv2.commands.categories.music;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.lavaplayer.GuildMusicManager;
import me.ix.noskillv2.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ClearCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		final TextChannel channel = (TextChannel) ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            channel.sendMessage("I need to be in a voice channel for this to work").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.queue.clear();

        channel.sendMessage("The music queue has been cleared").queue();
	}

	@Override
	public String getName() {
		return "clear";
	}

	@Override
	public String getHelp() {
		return "Clear the queue.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MUSIC;
	}

	@Override
	public List<OptionData> getArguments() {
		return null;
	}
	
}
