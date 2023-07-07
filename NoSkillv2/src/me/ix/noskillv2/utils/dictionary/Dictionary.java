package me.ix.noskillv2.utils.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Dictionary {
	
	private static HttpURLConnection connection;
	
	public static String getDefinitionResponse(String word, String language) {
		
		String URLString = "https://api.dictionaryapi.dev/api/v2/entries/" + language + "/" + word;
		
		System.out.println(URLString);
		
		BufferedReader reader;
		String line;
		StringBuffer responseContent = new StringBuffer();
		
		try {
			URL url = new URL(URLString);
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			int status = connection.getResponseCode();
			
			if(status > 299) {
				reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				while((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while((line = reader.readLine()) != null) {
					responseContent.append(line);
				}
				reader.close();
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
		
		return responseContent.toString();
	}
	
	public static ArrayList parseToArrayList(String responseBody) {
		
		ArrayList<String> definitionsArrayList = new ArrayList<String>();
		
		JSONArray dataList = new JSONArray(responseBody);
		
		for(int i = 0; i < dataList.length(); i++) { // First Part -> lists word, phonetics, meanings
			JSONObject data = dataList.getJSONObject(i);
			String word = data.getString("word");
			JSONArray meaningsList = data.getJSONArray("meanings");
			
			for(int ii = 0; ii < meaningsList.length(); ii++) { // Second part -> lists noun/verb and definitions
				JSONObject meaningsListData = meaningsList.getJSONObject(ii);
				String partOfSpeech = meaningsListData.getString("partOfSpeech");
				JSONArray definitionsList = meaningsListData.getJSONArray("definitions");
				
				for(int iii = 0; iii < definitionsList.length(); iii++) { // Third part -> lists definition, example, synonms
					JSONObject definitionListData = definitionsList.getJSONObject(iii);
					String definition = definitionListData.getString("definition");
					
					definitionsArrayList.add(partOfSpeech + "~" + definition);
				}
			}
		}

		return definitionsArrayList;
	}
	
	public static boolean checkIfValid(String responseBody) {
		if(responseBody.contains("meanings")) {
			return true;
		}else {
			return false;
		}
	}
}
