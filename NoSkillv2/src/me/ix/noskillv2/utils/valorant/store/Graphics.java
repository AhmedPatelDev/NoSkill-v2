package me.ix.noskillv2.utils.valorant.store;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Graphics {

	BufferedImage storeImage;
	
	public Graphics(Store store) throws MalformedURLException, IOException {
		long weaponReset = store.getTimeUntilWeaponReset();
		ArrayList<Weapon> weapons = store.getStoreWeapons();
		ArrayList<Item> weaponItems = new ArrayList<>(weapons);
		BufferedImage weaponsImage = getCombinedImage(weaponItems, weaponReset, false);

		long accessoryReset = store.getTimeUntilAccessoryReset();
		ArrayList<Accessory> accessories = store.getStoreAccessories();
		ArrayList<Item> accessoryItems = new ArrayList<>(accessories);
		BufferedImage accessoriesImage = getCombinedImage(accessoryItems, accessoryReset, true);

		BufferedImage stitchedImages = stitchImages(weaponsImage, accessoriesImage);
		BufferedImage borderStitched = addBorder(stitchedImages, 1, Color.cyan);
		
		storeImage = borderStitched;
	}
	
	public BufferedImage getStoreImage() {
		return storeImage;
	}
	
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
    
    public static BufferedImage addBorder(BufferedImage image, int borderSize, Color borderColor) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage borderedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = borderedImage.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the original image
        g2d.drawImage(image, 0, 0, null);

        // Draw the border
        g2d.setColor(borderColor);

        // Top border
        g2d.fillRect(0, 0, width, borderSize);

        // Bottom border
        g2d.fillRect(0, height - borderSize, width, borderSize);

        // Left border
        g2d.fillRect(0, 0, borderSize, height);

        // Right border
        g2d.fillRect(width - borderSize, 0, borderSize, height);

        g2d.dispose();

        return borderedImage;
    }
    
    public BufferedImage getCombinedImage(ArrayList<Item> items, long resetTime, boolean isAccessory) throws MalformedURLException, IOException {
        BufferedImage combinedImage = new BufferedImage(1715, 303, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();

        Random random = new Random();
        int randomInt = random.nextInt(5) + 1;

        BufferedImage border = ImageIO.read(new File("border" + 3 + ".png"));
        g2d.drawImage(border, 0, 0, null);

        String skinString = "";
        int offset = 0;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(invertColor(getPixelColor(border, 887, 43)));
        g2d.setFont(new Font("Courier New", Font.BOLD, 17));

        drawStringWithDropShadow(g2d, convertSeconds(resetTime), 879, 45);

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

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("consolas", Font.BOLD, 23));

            drawStringWithDropShadow(g2d, trunc(name.toUpperCase(), 26), x + (squareWidth / 2) - (g2d.getFontMetrics().stringWidth(trunc(name, 26)) / 2), 253);

            g2d.drawImage(resizedImage, x + (squareWidth / 2) - (resizedImage.getWidth() / 2), 100, null);

            String currencyURL = "";

            if (isAccessory) {
                currencyURL = "https://static.wikia.nocookie.net/valorant/images/9/9f/Kingdom_Credits.png/revision/latest?cb=20230608160711&format=original";
            } else {
                currencyURL = "https://static.wikia.nocookie.net/valorant/images/9/9d/Valorant_Points.png/revision/latest?cb=20200408014952&format=original";
            }

            BufferedImage currencyImage = resizeImageUseWidth(ImageIO.read(new URL(currencyURL)), 28, 1);
            g2d.drawImage(currencyImage, x, 70, null);
            g2d.setFont(new Font("Courier New", Font.BOLD, 19));
            drawStringWithDropShadow(g2d, String.valueOf(item.getPrice()), x + 35, 90);

            offset++;
        }
        g2d.dispose();

        return combinedImage;
    }

    private void drawStringWithDropShadow(Graphics2D g2d, String text, int x, int y) {
        Color textColor = g2d.getColor();
        Color shadowColor = Color.cyan;
        int shadowOffset = 1;

        // Draw drop shadow
        g2d.setColor(shadowColor);
        g2d.drawString(text, x + shadowOffset, y + shadowOffset);

        // Draw main text
        g2d.setColor(textColor);
        g2d.drawString(text, x, y);
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
	
}
