package me.ix.noskillv2.utils.valorant.setup;

public class Player {

	public String userID;
	public String accessToken;
	public String entitlementToken;
	
	public Player(String userID, String accessToken, String entitlementToken) {
		this.userID = userID;
		this.accessToken = accessToken;
		this.entitlementToken = entitlementToken;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getEntitlementToken() {
		return entitlementToken;
	}
}
