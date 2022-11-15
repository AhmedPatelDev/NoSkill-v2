package me.ix.noskillv2.commands.categories.music;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.lavaplayer.GuildMusicManager;
import me.ix.noskillv2.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PauseCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        
        if (!selfVoiceState.inAudioChannel()) {
        	ctx.sendMessage("I need to be in a voice channel for this to work");
            return;
        }

        if (!memberVoiceState.inAudioChannel()) {
        	ctx.sendMessage("You need to be in a voice channel for this command to work");
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
        	ctx.sendMessage("You need to be in the same voice channel as me for this to work");
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null) {
        	ctx.sendMessage("There is no track playing currently");
            return;
        }

        final AudioTrackInfo info = track.getInfo();

        audioPlayer.setPaused(true);
        
        ctx.sendMessage("Paused `" + info.title + "`");
	}

	@Override
	public String getName() {
		return "pause";
	}

	@Override
	public String getHelp() {
		return "Pause the current song.";
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
