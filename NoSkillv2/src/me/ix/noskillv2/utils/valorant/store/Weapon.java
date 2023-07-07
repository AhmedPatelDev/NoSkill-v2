package me.ix.noskillv2.utils.valorant.store;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Weapon extends Item {

    public Weapon(String itemID) throws IOException, ParseException {
        String url = "https://valorant-api.com/v1/weapons/skinlevels/" + itemID;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);

            JSONParser parser = new JSONParser();

            JSONObject skinObj = (JSONObject) parser.parse(result);
            JSONObject data = (JSONObject) skinObj.get("data");

            name = data.get("displayName").toString();
            imageURL = data.get("displayIcon").toString();
        }

        response.close();
        httpClient.close();
    }
}
