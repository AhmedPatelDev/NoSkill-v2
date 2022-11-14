package me.ix.noskillv2.commands.categories.fun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class RedditCommand implements ICommand {
	
	@Override
	public void execute(CommandContext ctx, ArrayList<String> arguments) {
		JSONParser parser = new JSONParser();
		String postLink = "";
		String title = "";
		String url = "";
		
		try {
			URL memeURL = new URL("https://meme-api.herokuapp.com/gimme/" + arguments.get(0) + "/");
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(memeURL.openConnection().getInputStream()));
		
			String lines;
			while((lines = bufferedReader.readLine()) != null) {
				JSONArray array = new JSONArray();
				array.add(parser.parse(lines));
				
				for(Object o : array) {
					JSONObject jsonObject = (JSONObject) o;
					
					postLink = (String) jsonObject.get("postLink");
					title = (String) jsonObject.get("title");
					url = (String) jsonObject.get("url");
				}
			}
			bufferedReader.close();

			EmbedBuilder memeEmbed = new EmbedBuilder();
			
			memeEmbed.setTitle(title, postLink);
			memeEmbed.setImage(url);
			memeEmbed.setColor(0x36393F);
			memeEmbed.setFooter("Developed by ix");
			
			ctx.sendMessage("", memeEmbed.build());
		} catch (Exception exc) {
			ctx.sendMessage("Could not find a post. Try checking the subreddit name");
		}
	}

	@Override
	public String getName() {
		return "reddit";
	}

	@Override
	public String getHelp() {
		return "Randomly grabs a post from a specified subreddit";
	}

	@Override
	public CommandCategory getCategory() {
		return CommandCategory.FUN;
	}

	@Override
	public List<OptionData> getArguments() {
		List<OptionData> optionData = new ArrayList<OptionData>();

		optionData.add(
				new OptionData(OptionType.STRING, "reddit", "Reddit to grab post from.", true)
		);
		
		return optionData;
	}

}
