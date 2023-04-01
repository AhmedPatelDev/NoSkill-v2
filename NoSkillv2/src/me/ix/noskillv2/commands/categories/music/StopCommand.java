package me.ix.noskillv2.commands.categories.music;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.NoSkillv2;
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

public class StopCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		EmbedBuilder eb = Utils.getDefaultEmbed(this, false);
		
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
        	eb.addField("Stop Info", "I need to be in a voice channel for this to work", true);
        	ctx.sendMessage("", eb.build());
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
        	eb.addField("Stop Info", "You need to be in a voice channel for this command to work", false);
        	ctx.sendMessage("", eb.build());
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
        	eb.addField("Stop Info", "You need to be in the same voice channel as me for this to work", false);
        	ctx.sendMessage("", eb.build());
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        eb.addField("Stop Info", "The player has been stopped and the queue has been cleared", false);
    	ctx.sendMessage("", eb.build());
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
