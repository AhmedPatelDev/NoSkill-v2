package me.ix.noskillv2.utils.valorant.store;

import com.google.gson.JsonObject;
import me.ix.noskillv2.utils.valorant.setup.Auth;
import me.ix.noskillv2.utils.valorant.setup.Header;
import me.ix.noskillv2.utils.valorant.setup.Player;
import org.apache.http.HttpHeaders;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Store {

    ArrayList<Weapon> storeWeapons = new ArrayList<Weapon>();
    ArrayList<Accessory> storeAccessories = new ArrayList<Accessory>();

    long timeUntilWeaponReset = 0;
    long timeUntilAccessoryReset = 0;

    public Store(Player player) throws IOException, ParseException {
        JsonObject StoreFront = getStoreFront(player);

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(StoreFront.toString());

        JSONObject skinsPanelLayout = (JSONObject) obj.get("SkinsPanelLayout");
        timeUntilWeaponReset = (long) skinsPanelLayout.get("SingleItemOffersRemainingDurationInSeconds");
        JSONArray singleItemOffers = (JSONArray) skinsPanelLayout.get("SingleItemOffers");
        for(Object item : singleItemOffers) {
            Weapon tempWeapon = new Weapon(item.toString());
            storeWeapons.add(tempWeapon);
        }

        JSONObject accessoryStore = (JSONObject) obj.get("AccessoryStore");
        timeUntilAccessoryReset = (long) accessoryStore.get("AccessoryStoreRemainingDurationInSeconds");
        JSONArray accessoryStoreOffers = (JSONArray) accessoryStore.get("AccessoryStoreOffers");
        for(Object item : accessoryStoreOffers) {
            Accessory tempAccessory = new Accessory(item);
            storeAccessories.add(tempAccessory);
        }
    }

    private static JsonObject getStoreFront(Player player) throws IOException, ParseException {
        Header authHeader = new Header(HttpHeaders.AUTHORIZATION, "Bearer " + player.getAccessToken());
        Header enHeader = new Header("X-Riot-Entitlements-JWT", player.getEntitlementToken());
        JsonObject json = Auth.get("https://pd.eu.a.pvp.net/store/v2/storefront/" + player.getUserID(), authHeader, enHeader).getAsJsonObject();
        return json;
    }

    public ArrayList<Weapon> getStoreWeapons() {
        return storeWeapons;
    }

    public ArrayList<Accessory> getStoreAccessories() {
        return storeAccessories;
    }

    public long getTimeUntilAccessoryReset() {
        return timeUntilAccessoryReset;
    }

    public long getTimeUntilWeaponReset() {
        return timeUntilWeaponReset;
    }
}
