package me.ix.noskillv2.utils.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import me.ix.noskillv2.CommandManager;
import me.ix.noskillv2.NoSkillv2;
import me.ix.noskillv2.utils.Utils;
import net.dv8tion.jda.api.sharding.ShardManager;

public class WebServer extends Thread {

	private int port;
	private CommandManager mgr;
	ShardManager shardManager;

	long time = System.currentTimeMillis() / 1000L;

	public WebServer(int port, CommandManager mgr, ShardManager shardManager) {
		this.port = port;
		this.mgr = mgr;
		this.shardManager = shardManager;
	}

	public String getBase64Logo() {
		try {
			URL url = NoSkillv2.class.getClassLoader().getResource("me/ix/noskillv2/utils/webserver/logo.png");
			File file = new File(url.toURI());
			
			byte[] byteData = Files.readAllBytes(file.toPath());
			String base64String = Base64.getEncoder().encodeToString(byteData);
			
			return "data:image/png;base64," + base64String;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public String getUptimeText() {
		long timeNow = System.currentTimeMillis() / 1000L;
		long upTime = timeNow - time;

		int minutes = (int) (upTime / 60);
		int seconds = (int) (upTime % 60);

		return minutes + " minutes, " + seconds + " seconds";
	}

	public String getServerText() {
		int serverSize = shardManager.getGuilds().size();
		return "Watching " + serverSize + " Servers";
	}

	public String getCommandsText() {
		return this.mgr.getCommands().size() + " total commands";
	}

	public String getExecutedCommands() {
		String html = "<ul>";

		for (String cmdE : mgr.commandsExecuted) {
			html += "<li>" + cmdE + "</li>";
		}

		html += "</ul>";
		return html;
	}

	public String getContent() {
		try {
			URL url = NoSkillv2.class.getClassLoader().getResource("me/ix/noskillv2/utils/webserver/interface.html");
			File file = new File(url.toURI());
			List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			
			String sLine = "";
			
			for(String s: lines) {
				sLine += s;
			}
				
			sLine = sLine
					.replace("[{servertext}]", getServerText())
					.replace("[{uptimetext}]", getUptimeText())
					.replace("[{commandsText}]", getCommandsText())
					.replace("[{commandslist}]", getExecutedCommands())
					.replace("[{imageSrc}]", getBase64Logo());
			
			return sLine;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	private File getClassFile(String fileName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);

		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}
	}

	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(port);

			Utils.log("Started web interface on port: " + port);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String s;
				while ((s = in.readLine()) != null) {
					if (s.isEmpty()) {
						break;
					}
				}
				OutputStream clientOutput = clientSocket.getOutputStream();
				clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
				clientOutput.write("\r\n".getBytes());

				clientOutput.write(getContent().getBytes());

				clientOutput.write("\r\n\r\n".getBytes());
				clientOutput.flush();
				in.close();
				clientOutput.close();
			}
		} catch (Exception e) {
			Utils.log("Exception occurred on webserver thread");
			e.printStackTrace();
		}
	}
}
