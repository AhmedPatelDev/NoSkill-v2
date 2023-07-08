package me.ix.noskillv2.utils.valorant.store;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Accessory extends Item {

    public Accessory(Object item) throws IOException, ParseException {
        JSONObject offer = (JSONObject) ((JSONObject) item).get("Offer");
        JSONObject rewards = (JSONObject) ((JSONArray) offer.get("Rewards")).get(0);
        String itemTypeID = (String) rewards.get("ItemTypeID");
        String itemID = (String) rewards.get("ItemID");
        String itemType = itemTypeToName(itemTypeID);

        String url = "https://valorant-api.com/v1/" + itemType + "/" + itemID;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            JSONParser parser = new JSONParser();
            JSONObject objTest = (JSONObject) parser.parse(result);
            JSONObject data = (JSONObject) objTest.get("data");
            
            price = (long) ((JSONObject) offer.get("Cost")).get("85ca954a-41f2-ce94-9b45-8ca3dd39a00d");
            
            name = data.get("displayName").toString();

            if(itemType.equals("playertitles")) {
                imageURL = "https://static.wikia.nocookie.net/valorant/images/5/5d/Player_Title_image.png/revision/latest?cb=20210104061536&format=original";
            } else {
                imageURL = data.get("displayIcon").toString();
            }
        }

        response.close();
        httpClient.close();
    }

    private static String itemTypeToName(String UUID) {
        switch (UUID) {
            case "d5f120f8-ff8c-4aac-92ea-f2b5acbe9475":
                return "sprays";
            case "dd3bf334-87f3-40bd-b043-682a57a8dc3a":
                return "buddies/levels";
            case "3f296c07-64c3-494c-923b-fe692a4fa1bd":
                return "playercards";
            case "de7caa6b-adf7-4588-bbd1-143831e786c6":
                return "playertitles";
            default:
                return "idk";
        }
    }
}
