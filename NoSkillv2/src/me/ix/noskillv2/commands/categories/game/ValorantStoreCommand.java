package me.ix.noskillv2.commands.categories.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.database.repo.ValorantRepo;
import me.ix.noskillv2.utils.database.repo.ValorantRepo.ValorantAccount;
import me.ix.noskillv2.utils.valorant.setup.Auth;
import me.ix.noskillv2.utils.valorant.setup.Player;
import me.ix.noskillv2.utils.valorant.store.Graphics;
import me.ix.noskillv2.utils.valorant.store.Store;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ValorantStoreCommand implements ICommand {

	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		User user = ctx.getUser();
		String userID = user.getId();

		ValorantAccount va = ValorantRepo.getValorantAccountFromId(userID);

		if (va == null) {
			ctx.sendMessage("Direct message me to set up account: `-ValorantLogin username password`");
		}

		String username = va.getUsername();
		String password = va.getPassword();

		try {
			Auth auth = new Auth(username, password);
			Player player = auth.getValPlayer();
			Store store = new Store(player);
			Graphics storeGraphics = new Graphics(store);

			File skinsfile = new File(user.getId() + "skins.png");
			ImageIO.write(storeGraphics.getStoreImage(), "png", skinsfile);
			ctx.sendFile("", skinsfile);

		} catch (Exception e) {
			e.printStackTrace();
			String errorString = "";
			errorString += "Error occured. Check your credentials."
					+ "\nDirect message me to set up account: `-ValorantLogin username password`"
					+ "\n***ALL*** passwords are stored encrypted.";
			ctx.sendMessage(errorString);
		}

	}

	@Override
	public String getName() {
		return "valorantstore";
	}

	@Override
	public String getHelp() {
		return "Get store for a valorant player.";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.GAME;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(new OptionData(OptionType.USER, "user", "select a user", false));

		return optionData;
	}

}
