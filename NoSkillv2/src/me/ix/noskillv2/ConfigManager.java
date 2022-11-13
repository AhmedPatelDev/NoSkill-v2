package me.ix.noskillv2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import me.ix.noskillv2.utils.Utils;

public class ConfigManager {

	String[] keys = { "TOKEN", "STATUS" };
	String path = "config.cfg";
	
	private File config_file;
	
	public ConfigManager() {
		config_file = new File(path);

		if (config_file.exists()) {
			for(String key : keys) {
				if(getValueFromConfig(key) == null || getValueFromConfig(key) == "") {
					Utils.log("ERROR: " + key + " not found in config. Shutting down...");
					System.exit(0);
				}
			}
		} else {
			Utils.log("INFO: Could not find config.cfg file. Generating new file.");

			try {
				config_file.createNewFile();

				String configText = "";
				
				for(String key : keys) {
					configText += "[" + key + "]=\n";
				}

				FileWriter fw = new FileWriter(path);
				fw.write(configText);
				fw.close();

			} catch (IOException e) {
				Utils.log("ERROR: Could not generate config.cfg file.");
				System.exit(0);
			}
			
			Utils.log("INFO: Generated new config file. Shutting down...");
			System.exit(0);
		}
	}

	public String getValueFromConfig(String key) {
		try {
			Scanner reader = new Scanner(config_file);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();

				if (line.contains("=")) {
					String[] lineArr = line.split("=");

					if (lineArr[0].contains("[" + key + "]")) {
						return lineArr[1];
					}
				}
			}
		} catch (FileNotFoundException e) {
			Utils.log("ERROR: Config file not found.");
			System.exit(0);
		} catch (ArrayIndexOutOfBoundsException e) {
			Utils.log("ERROR: No value for " + key + " found in config. Shutting down...");
			System.exit(0);
		}
		return null;
	}
}
