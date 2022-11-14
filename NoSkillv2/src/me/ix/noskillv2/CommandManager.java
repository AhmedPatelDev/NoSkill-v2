package me.ix.noskillv2;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.commands.categories.fun.RedditCommand;
import me.ix.noskillv2.commands.categories.misc.SayCommand;
import me.ix.noskillv2.commands.categories.music.PlayCommand;
import me.ix.noskillv2.utils.Utils;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

	private final List<ICommand> commands = new ArrayList<>();

	public CommandManager() {
		/* FUN */
		addCommand(new RedditCommand());

		/* GAME */

		/* MISC */
		addCommand(new SayCommand());

		/* MOD */

		/* MUSIC */
		addCommand(new PlayCommand());
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

				CommandContext ctx = new CommandContext(event);
				
				cmd.execute(ctx, arguments);
			}
		}
		if (e instanceof MessageReceivedEvent) {
			MessageReceivedEvent event = (MessageReceivedEvent) e;
			String message = event.getMessage().getContentRaw();
			String command = message.split(" ")[0];
			String commandClean = command.substring(1, command.length());

			ICommand cmd = this.getCommand(commandClean);
			if (cmd != null) {
				event.getChannel().sendTyping().queue();

				ArrayList<String> arguments = Utils.getArgumentsFromEvent(event);

				if (arguments.size() != cmd.getArguments().size()) {
					event.getMessage().reply("Incorrect arguments. Use `" + NoSkillv2.DEFAULT_PREFIX + "help " + cmd.getName() + "`").queue();
					return;
				}

				CommandContext ctx = new CommandContext(event);
				
				cmd.execute(ctx, arguments);
			}
		}
	}
}
