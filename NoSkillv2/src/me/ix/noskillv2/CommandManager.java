package me.ix.noskillv2;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.commands.categories.fun.RedditCommand;
import me.ix.noskillv2.commands.categories.misc.SayCommand;
import me.ix.noskillv2.commands.categories.mod.BackupCommand;
import me.ix.noskillv2.commands.categories.mod.BanCommand;
import me.ix.noskillv2.commands.categories.mod.CleanCommand;
import me.ix.noskillv2.commands.categories.mod.KickCommand;
import me.ix.noskillv2.commands.categories.mod.SetPrefixCommand;
import me.ix.noskillv2.commands.categories.mod.ShutdownCommand;
import me.ix.noskillv2.commands.categories.mod.UnbanCommand;
import me.ix.noskillv2.commands.categories.music.ClearCommand;
import me.ix.noskillv2.commands.categories.music.JoinCommand;
import me.ix.noskillv2.commands.categories.music.LeaveCommand;
import me.ix.noskillv2.commands.categories.music.NowPlayingCommand;
import me.ix.noskillv2.commands.categories.music.PauseCommand;
import me.ix.noskillv2.commands.categories.music.PlayCommand;
import me.ix.noskillv2.commands.categories.music.QueueCommand;
import me.ix.noskillv2.commands.categories.music.SkipCommand;
import me.ix.noskillv2.commands.categories.music.StopCommand;
import me.ix.noskillv2.utils.Utils;
import me.ix.noskillv2.utils.database.repo.GuildRepo;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	private final List<ICommand> commands = new ArrayList<>();

	public final ArrayList<String> commandsExecuted = new ArrayList<String>();
	
	public CommandManager() {
		/* FUN */
		addCommand(new RedditCommand());

		/* GAME */

		/* MISC */
		addCommand(new SayCommand());

		/* MOD */
		addCommand(new BackupCommand());
		addCommand(new BanCommand());
		addCommand(new CleanCommand());
		addCommand(new KickCommand());
		addCommand(new SetPrefixCommand());
		addCommand(new ShutdownCommand());
		addCommand(new UnbanCommand());
		
		/* MUSIC */
		addCommand(new ClearCommand());
		addCommand(new JoinCommand());
		addCommand(new LeaveCommand());
		addCommand(new NowPlayingCommand());
		addCommand(new PlayCommand());
		addCommand(new QueueCommand());
		addCommand(new SkipCommand());
		addCommand(new StopCommand());
		//addCommand(new PauseCommand()); (removed due to complications with required args)
	}

	@SuppressWarnings("unused")
	private void addCommand(ICommand cmd) {
		boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

		if (nameFound) {
			throw new IllegalArgumentException("A command with this name is already present");
		}

		commands.add(cmd);
	}

	public List<ICommand> getCommands() {
		return commands;
	}

	@Nullable
	public ICommand getCommand(String search) {
		String searchLower = search.toLowerCase();

		for (ICommand cmd : this.commands) {
			if (cmd.getName().toLowerCase().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}

		return null;
	}

	public void handle(Event e) {
		if (e instanceof SlashCommandInteractionEvent) {
			SlashCommandInteractionEvent event = (SlashCommandInteractionEvent) e;
			String command = event.getName();
			ICommand cmd = this.getCommand(command);
			
			if (cmd != null) {
				event.getChannel().sendTyping().queue();

				ArrayList<String> arguments = Utils.getArgumentsFromEvent(event);

				commandsExecuted.add(
						"<span style=\"color: orange; font-weight: bold; margin-right: 5px; \">[" + event.getUser().getAsTag() + "]</span>" +
						"<span style=\"color: white;\">" + cmd.getName() + " " + arguments.toString() + " in " + event.getGuild().getName() + "</span>"
				);
				
				CommandContext ctx = new CommandContext(event);
				
				cmd.execute(ctx, arguments);
			}
		}
		if (e instanceof MessageReceivedEvent) {
			MessageReceivedEvent event = (MessageReceivedEvent) e;
			String guildPrefix = GuildRepo.getPrefix(event.getGuild().getIdLong());
			
			String message = event.getMessage().getContentRaw();
			String command = message.split(" ")[0];
			String commandClean = command.substring(guildPrefix.length(), command.length());
			
			ICommand cmd = this.getCommand(commandClean);
			if (cmd != null) {
				event.getChannel().sendTyping().queue();

				CommandContext ctx = new CommandContext(event);
				
				ArrayList<String> arguments = Utils.getArgumentsFromEvent(event);

				commandsExecuted.add(
						"<span style=\"color: orange; font-weight: bold; margin-right: 5px; \">[" + event.getAuthor().getAsTag() + "] </span>" +
						"<span style=\"color: white;\">" + cmd.getName() + " " + arguments.toString() + " in " + event.getGuild().getName() + "</span>"
				);
				
				// Check for commands where loose arguments are used.
				if(cmd.getName() == "play") {
					cmd.execute(ctx, arguments);
				} else {
					if(cmd.getArguments() != null) {
						if(arguments.size() != cmd.getArguments().size()) {
							event.getMessage().reply("Incorrect arguments. Use `" + NoSkillv2.DEFAULT_PREFIX + "help " + cmd.getName() + "`").queue();
							return;
						}
					}
					cmd.execute(ctx, arguments);
				}
			}
		}
	}
}
