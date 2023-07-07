package me.ix.noskillv2.commands.categories.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import me.ix.noskillv2.commands.CommandCategory;
import me.ix.noskillv2.commands.CommandContext;
import me.ix.noskillv2.commands.ICommand;
import me.ix.noskillv2.utils.database.repo.ValorantRepo;
import me.ix.noskillv2.utils.database.repo.ValorantRepo.ValorantAccount;
import me.ix.noskillv2.utils.valorant.setup.Auth;
import me.ix.noskillv2.utils.valorant.setup.Player;
import me.ix.noskillv2.utils.valorant.store.Accessory;
import me.ix.noskillv2.utils.valorant.store.Item;
import me.ix.noskillv2.utils.valorant.store.Store;
import me.ix.noskillv2.utils.valorant.store.Weapon;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ValorantStoreCommand implements ICommand {

	public String trunc(String input, int maxLength) {
		if (input.length() <= maxLength) {
			return input;
		} else {
			return input.substring(0, maxLength - 3) + "...";
		}
	}

	private BufferedImage resizeImageUseHeight(BufferedImage oldImg, int heightTo, double scale) {
		double width = oldImg.getWidth();
		double height = oldImg.getHeight();

		double scaleFactor = (heightTo / height) * scale;
		width *= scaleFactor;
		height *= scaleFactor;

		Image temporary = oldImg.getScaledInstance((int) Math.round(width), (int) Math.round(height), Image.SCALE_SMOOTH);
		BufferedImage newImage = new BufferedImage((int) Math.round(width), (int) Math.round(height), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dn = newImage.createGraphics();
		g2dn.drawImage(temporary, 0, 0, null);
		g2dn.dispose();

		return newImage;
	}

	private BufferedImage resizeImageUseWidth(BufferedImage oldImg, int widthTo, double scale) {
		double width = oldImg.getWidth();
		double height = oldImg.getHeight();

		double scaleFactor = (widthTo / width) * scale;
		width *= scaleFactor;
		height *= scaleFactor;

		Image temporary = oldImg.getScaledInstance((int) Math.round(width), (int) Math.round(height), Image.SCALE_SMOOTH);
		BufferedImage newImage = new BufferedImage((int) Math.round(width), (int) Math.round(height), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dn = newImage.createGraphics();
		g2dn.drawImage(temporary, 0, 0, null);
		g2dn.dispose();

		return newImage;
	}

	public static BufferedImage shiftHue(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage shiftedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = shiftedImage.createGraphics();

		g2d.drawImage(image, 0, 0, null);

		float hueShift = (float) (Math.random() - 0.5);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				int alpha = (rgb >> 24) & 0xFF;
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;

				float[] hsb = Color.RGBtoHSB(red, green, blue, null);
				float hue = hsb[0];

				float shiftedHue = (hue + hueShift) % 1.0f;
				if (shiftedHue < 0) {
					shiftedHue += 1.0f;
				}

				int shiftedRGB = Color.HSBtoRGB(shiftedHue, hsb[1], hsb[2]);

				int shiftedPixel = (alpha << 24) | (shiftedRGB & 0xFFFFFF);
				shiftedImage.setRGB(x, y, shiftedPixel);
			}
		}

		g2d.dispose();

		return shiftedImage;
	}

    public static String convertSeconds(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = ((seconds % (24 * 3600)) % 3600) / 60;
        long remainingSeconds = ((seconds % (24 * 3600)) % 3600) % 60;

        String formattedTime = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, remainingSeconds);
        return formattedTime;
    }
	
    public static Color getPixelColor(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;

        return new Color(red, green, blue);
    }
    
    public static Color invertColor(Color color) {
        int red = 255 - color.getRed();
        int green = 255 - color.getGreen();
        int blue = 255 - color.getBlue();
        return new Color(red, green, blue);
    }
    
	public BufferedImage getCombinedImage(ArrayList<Item> items, long resetTime, int doubleOff) throws MalformedURLException, IOException {
		BufferedImage combinedImage = new BufferedImage(1715, 303, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = combinedImage.createGraphics();
		
		Random random = new Random();
        int randomInt = random.nextInt(5) + 1;
		
		BufferedImage border = ImageIO.read(new File("border" + randomInt + ".png"));
		//BufferedImage borderShift = shiftHue(border);
		g2d.drawImage(border, 0, 0, null);

		String skinString = "";
		int offset = 0;

		g2d.setColor(invertColor(getPixelColor(border, 887, 43)));
		g2d.setFont(new Font("Courier New", Font.BOLD, 15));
		g2d.drawString(convertSeconds(resetTime), 887, 43);

		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Courier New", Font.BOLD, 23));
		
		int squareWidth = 383; // Width of each square
		int squareHeight = 200; // Height of each square
		int startOfGrid = 55; // Starting x-coordinate of the grid
		int spaceBetweenGrid = 30; // Space between squares in the grid

		for (Item item : items) {

			String name = item.name;
			String imageURL = item.imageURL;

			skinString += " - " + name + "\n";

			BufferedImage image = ImageIO.read(new URL(imageURL));
			BufferedImage resizedImage = resizeImageUseHeight(image, squareHeight, 0.5);

			if (resizedImage.getWidth() > squareWidth) {
				resizedImage = resizeImageUseWidth(resizedImage, squareWidth, 1);
			}

			int squareIndex = offset % 4; // Determine the square index
			int x = startOfGrid + squareIndex * (squareWidth + spaceBetweenGrid); // Calculate x-coordinate

			g2d.drawString(trunc(name, 26), x + (squareWidth / 2) - (g2d.getFontMetrics().stringWidth(trunc(name, 26)) / 2), 253);

			g2d.drawImage(resizedImage, x + (squareWidth / 2) - (resizedImage.getWidth() / 2), 100, null);

			offset++;
		}
		g2d.dispose();

		return combinedImage;
	}

	public static BufferedImage stitchImages(BufferedImage image1, BufferedImage image2) {
		int width = Math.max(image1.getWidth(), image2.getWidth());
		int height = image1.getHeight() + image2.getHeight() + 10; // Add extra height for the separator

		BufferedImage stitchedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = stitchedImage.createGraphics();

		g2d.drawImage(image1, 0, 0, null);

		// Draw separator line
		g2d.setColor(Color.BLACK); // You can set the color of the separator here
		g2d.fillRect(0, image1.getHeight(), width, 10); // Adjust the height and other parameters as needed

		g2d.drawImage(image2, 0, image1.getHeight() + 10, null); // Add extra height for the separator

		g2d.dispose();

		return stitchedImage;
	}

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

			long weaponReset = store.getTimeUntilWeaponReset();
			ArrayList<Weapon> weapons = store.getStoreWeapons();
			ArrayList<Item> weaponItems = new ArrayList<>(weapons);
			BufferedImage weaponsImage = getCombinedImage(weaponItems, weaponReset, 0);

			long accessoryReset = store.getTimeUntilAccessoryReset();
			ArrayList<Accessory> accessories = store.getStoreAccessories();
			ArrayList<Item> accessoryItems = new ArrayList<>(accessories);
			BufferedImage accessoriesImage = getCombinedImage(accessoryItems, accessoryReset, 50);

			BufferedImage stitched = stitchImages(weaponsImage, accessoriesImage);

			File skinsfile = new File(user.getId() + "skins.png");
			ImageIO.write(stitched, "png", skinsfile);
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
