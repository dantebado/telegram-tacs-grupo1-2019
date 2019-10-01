package tacs.frba.utn.telegram.external;

import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import utils.JsonTransformer;

public class ExternalResponse {
	
	private int code;
	private String responseBody;
	private JsonObject responseJson;
	private HashMap<String, String> cookies;
	
	public ExternalResponse(int code, String body, String setCookies) {
		this.code = code;
		this.responseBody = body;
		if(body != null) {
			this.responseJson = JsonTransformer.getGson().fromJson(body, JsonObject.class);	
		}
		cookies = new HashMap<>();
		try {
			if(setCookies != null) {
				for(String cookie : setCookies.split(";")) {
					cookies.put(cookie.split("=")[0], cookie.split("=")[1]);
				}
			}
		} catch (Exception e) {
			
		}
		
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public JsonObject getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(JsonObject responseJson) {
		this.responseJson = responseJson;
	}
	
	public JsonElement getResponseData() {
		return responseJson.get("data");
	}
	
	public String getCookie(String cookieName) {
		return cookies.get(cookieName);
	}

}
