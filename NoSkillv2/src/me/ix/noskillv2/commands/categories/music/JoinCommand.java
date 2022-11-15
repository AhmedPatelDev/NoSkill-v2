package me.ix.noskillv2.commands.categories.music;

import java.util.ArrayList;
import java.util.List;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		final Member self = ctx.getGuild().getSelfMember();

		final GuildVoiceState selfVoiceState = self.getVoiceState();

		if (selfVoiceState.inAudioChannel()) {
			ctx.sendMessage("Bot is already in a voice channel");
			return;
		}

		final Member member = ctx.getMember();
		final GuildVoiceState memberVoiceState = member.getVoiceState();

		if (!memberVoiceState.inAudioChannel()) {
			ctx.sendMessage("You need to be in a voice channel for this command to work");
			return;
		}

		final AudioManager audioManager = ctx.getGuild().getAudioManager();
		final VoiceChannel memberChannel = (VoiceChannel) memberVoiceState.getChannel();

		audioManager.openAudioConnection(memberChannel);
		ctx.sendMessage("Connecting to `" + memberChannel.getName() + "`");
	}

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getHelp() {
		return "Make the bot join the voice channel.";
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
