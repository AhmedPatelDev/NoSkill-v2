package me.ix.noskillv2.commands.categories.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.lavaplayer.GuildMusicManager;
import me.ix.noskillv2.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class SkipCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

		EmbedBuilder eb = Utils.getDefaultEmbed(this, true);
        
        if (!selfVoiceState.inAudioChannel()) {
        	eb.addField("Skip Info", "I need to be in a voice channel for this to work", false);
        	ctx.sendMessage("", eb.build());
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
        	eb.addField("Skip Info", "You need to be in a voice channel for this command to work", false);
        	ctx.sendMessage("", eb.build());
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
        	eb.addField("Skip Info", "You need to be in the same voice channel as me for this to work", false);
        	ctx.sendMessage("", eb.build());
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
        	eb.addField("Skip Info", "There is no track playing currently", false);
        	ctx.sendMessage("", eb.build());
            return;
        }

        musicManager.scheduler.nextTrack();
    	eb.addField("Skip Info", "Skipped the current track", false);
    	ctx.sendMessage("", eb.build());
	}

	@Override
	public String getName() {
		return "skip";
	}

	@Override
	public String getHelp() {
		return "Skips the currently playing song.";
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
