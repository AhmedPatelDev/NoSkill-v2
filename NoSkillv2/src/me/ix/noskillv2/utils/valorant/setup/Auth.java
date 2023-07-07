package me.ix.noskillv2.utils.valorant.setup;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Auth {

	public static JsonParser jsonParser;
	public static CloseableHttpClient httpClient;

	String username = "";
	String password = "";

	public Auth(String username, String password) throws IOException {
		this.username = username;
		this.password = password;
	}

	@SuppressWarnings("deprecation")
	public Player getValPlayer() throws IOException {
		jsonParser = new JsonParser();
		resetExecutor();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("client_id", "play-valorant-web-prod");
		jsonObject.addProperty("nonce", 1);
		jsonObject.addProperty("redirect_uri", "https://playvalorant.com/opt_in");
		jsonObject.addProperty("response_type", "token id_token");

		post("https://auth.riotgames.com/api/v1/authorization", jsonObject.toString());

		JsonObject authObject = new JsonObject();
		authObject.addProperty("type", "auth");
		authObject.addProperty("username", this.username);
		authObject.addProperty("password", this.password);

		JsonObject authResponse = put("https://auth.riotgames.com/api/v1/authorization", authObject.toString()).getAsJsonObject();
		JsonObject responseObject = authResponse.get("response").getAsJsonObject();
		JsonObject parametersObject = responseObject.get("parameters").getAsJsonObject();
		String uri = String.valueOf(parametersObject.get("uri"));
		String[] parts = uri.replace("https://playvalorant.com/opt_in#", "").split("&");
		String token = parts[0].replace("access_token=", "").replace("\"", "");

		Header authHeader = new Header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		JsonObject entitlementResponse = post("https://entitlements.auth.riotgames.com/api/token/v1", "{}", authHeader).getAsJsonObject();
		String entitlementToken = entitlementResponse.get("entitlements_token").getAsString();

		JsonObject userResponse = post("https://auth.riotgames.com/userinfo", "{}", authHeader).getAsJsonObject();
		String userId = userResponse.get("sub").getAsString();

		Player player = new Player(userId, token, entitlementToken);
		return player;
	}

	public static JsonElement get(String url, Header... headers) throws IOException {
		HttpGet httpGet = new HttpGet(url);

		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");

		for (Header header : headers) {
			if (header != null) {
				httpGet.setHeader(header.getKey(), header.getValue());
			}
		}

		CloseableHttpResponse response = httpClient.execute(httpGet);
		String responseJSON = EntityUtils.toString(response.getEntity());
		return jsonParser.parse(responseJSON);
	}

	public JsonElement post(String url, String json, Header... headers) throws IOException {
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json);
		httpPost.setEntity(entity);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		for (Header header : headers) {
			if (header != null) {
				httpPost.setHeader(header.getKey(), header.getValue());
			}
		}

		CloseableHttpResponse response = httpClient.execute(httpPost);
		String responseJSON = EntityUtils.toString(response.getEntity());
		return jsonParser.parse(responseJSON);
	}

	public JsonElement put(String url, String json, Header... headers) throws IOException {
		HttpPut httpPut = new HttpPut(url);
		StringEntity entity = new StringEntity(json);
		httpPut.setEntity(entity);

		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");

		for (Header header : headers) {
			if (header != null) {
				httpPut.setHeader(header.getKey(), header.getValue());
			}
		}

		CloseableHttpResponse response = httpClient.execute(httpPut);
		String responseJSON = EntityUtils.toString(response.getEntity());
		return jsonParser.parse(responseJSON);
	}

	public void resetExecutor() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
		httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
	}
}
