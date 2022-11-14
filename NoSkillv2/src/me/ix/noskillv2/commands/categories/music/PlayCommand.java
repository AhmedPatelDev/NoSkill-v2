package me.ix.noskillv2.commands.categories.music;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.lavaplayer.GuildMusicManager;
import me.ix.noskillv2.utils.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        
        String link = String.join(" ", arguments);
        
        if(!isUrl(link)) {
        	link = "ytsearch:" + link;
        }
        
        if(track != null && audioPlayer.isPaused()) {
			audioPlayer.setPaused(false);
			ctx.sendMessage("Resumed `track.getInfo().title`");
			return;
		}
        
        if (!ctx.getMember().getVoiceState().inAudioChannel()) {
        	ctx.sendMessage("You need to be in a voice channel for this command to work");
            return;
        }
        
        if (!ctx.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
        	final AudioManager audioManager = ctx.getGuild().getAudioManager();
    		final AudioChannelUnion memberChannel = ctx.getMember().getVoiceState().getChannel();
    		
    		audioManager.openAudioConnection(memberChannel);
        }
        
        final Channel channel = ctx.getChannel();
        
        PlayerManager.getInstance().loadAndPlay((TextChannel) channel, link);
	}

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getHelp() {
		return "Plays a song using a youtube link";
	}
	
	@Override
	public CommandCategory getCategory() {
		return CommandCategory.MUSIC;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "song", "Link or Name of youtube video.", true)
		);
		
		return optionData;
	}

	private boolean isUrl(String Url) {
		try {
			new URI(Url);
			return true;
		}catch(URISyntaxException e) {
			return false;
		}
	}
	
}
