package me.ix.noskillv2.commands.categories.music;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.lavaplayer.GuildMusicManager;
import me.ix.noskillv2.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		final Member self = ctx.getGuild().getSelfMember();
		final GuildVoiceState selfVoiceState = self.getVoiceState();

		EmbedBuilder eb = Utils.getDefaultEmbed(this, true);
    	
		if (!selfVoiceState.inAudioChannel()) {
	    	eb.addField("Leave Info", "I need to be in a voice channel for this to work", false);
	    	ctx.sendMessage("", eb.build());
			return;
		}

		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inAudioChannel()) {
	    	eb.addField("Leave Info", "You need to be in a voice channel for this command to work", false);
	    	ctx.sendMessage("", eb.build());
			return;
		}

		if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
	    	eb.addField("Leave Info", "You need to be in the same voice channel as me for this to work", false);
	    	ctx.sendMessage("", eb.build());
			return;
		}

		final Guild guild = ctx.getGuild();

		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

		musicManager.scheduler.queue.clear();
		musicManager.audioPlayer.stopTrack();

		final AudioManager audioManager = guild.getAudioManager();

		audioManager.closeAudioConnection();

    	eb.addField("Leave Info", "I have left the voice channel", false);
    	ctx.sendMessage("", eb.build());
	}

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public String getHelp() {
		return "Disconnect and clear queue.";
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
