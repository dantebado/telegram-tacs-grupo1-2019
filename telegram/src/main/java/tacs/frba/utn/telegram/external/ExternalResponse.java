package tacs.frba.utn.telegram.external;

import com.google.gson.JsonObject;

import utils.JsonTransformer;

public class ExternalResponse {
	
	private int code;
	private String responseBody;
	private JsonObject responseJson;
	
	public ExternalResponse(int code, String body) {
		this.code = code;
		this.responseBody = body;
		if(body != null) {
			this.responseJson = JsonTransformer.getGson().fromJson(body, JsonObject.class);			
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

}
