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

public class QueueCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		final Member self = ctx.getGuild().getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if (!selfVoiceState.inAudioChannel()) {
			ctx.sendMessage("I need to be in a voice channel for this to work");
			return;
		}

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
		final AudioPlayer audioPlayer = musicManager.audioPlayer;
		final AudioTrack track = audioPlayer.getPlayingTrack();
		final AudioTrackInfo info = track.getInfo();

		String toPrint = "Now playing " + info.title + " by " + info.author + "\n(Link: <" + info.uri + ">)\n Queue:";

		int count = 2;

		for (AudioTrack queueTrack : musicManager.scheduler.getQueue()) {
			toPrint += "\n" + count + ") " + queueTrack.getInfo().title;
			count++;
		}

		ctx.sendMessage(toPrint);
	}

	@Override
	public String getName() {
		return "queue";
	}

	@Override
	public String getHelp() {
		return "List songs in queue.";
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
