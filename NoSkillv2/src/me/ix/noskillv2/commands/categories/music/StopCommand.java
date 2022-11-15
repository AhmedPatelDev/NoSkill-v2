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
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class StopCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
        	ctx.sendMessage("I need to be in a voice channel for this to work");
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
        	ctx.sendMessage("You need to be in a voice channel for this command to work");
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
        	ctx.sendMessage("You need to be in the same voice channel as me for this to work");
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        ctx.sendMessage("The player has been stopped and the queue has been cleared");
	}

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public String getHelp() {
		return "Stops the music player.";
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
