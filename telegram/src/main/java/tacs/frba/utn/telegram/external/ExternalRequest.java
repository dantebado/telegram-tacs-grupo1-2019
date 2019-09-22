package tacs.frba.utn.telegram.external;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

import utils.JsonTransformer;

public class ExternalRequest {
	
	private static String API_URL = "http://localhost:4040/";
	
	private static ExternalResponse requestAPI(String resource, String method, String sessionCookie, String body) {
		ExternalResponse response = null;
		
		URL url;
		try {
			url = new URL(API_URL + resource);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod(method);
	        if(sessionCookie != null) {
		        con.setRequestProperty("Cookie", "id="+sessionCookie);
	        }
	        con.setDoOutput(true);
	        con.getOutputStream().write(body.getBytes("UTF-8"));
	        
	        InputStream stream = con.getInputStream();
	        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
	        byte buffer[] = new byte[1024];
	        int bytesRead = 0;
	        while ((bytesRead = stream.read(buffer)) > 0) {
	            responseBody.write(buffer, 0, bytesRead);
	        }
	        
	        response = new ExternalResponse(con.getResponseCode(), responseBody.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public static ExternalResponse getAPI(String resource, String sessionCookie, String body) {
		return requestAPI(resource, "GET", sessionCookie, body);
	}
	
	public static ExternalResponse postAPI(String resource, String sessionCookie, String body) {
		return requestAPI(resource, "POST", sessionCookie, body);
	}
	
	public static ExternalResponse putAPI(String resource, String sessionCookie, String body) {
		return requestAPI(resource, "PUT", sessionCookie, body);
	}
	
	public static ExternalResponse deleteAPI(String resource, String sessionCookie, String body) {
		return requestAPI(resource, "DELETE", sessionCookie, body);
	}
	
	public static ExternalResponse patchAPI(String resource, String sessionCookie, String body) {
		return requestAPI(resource, "PATCH", sessionCookie, body);
	}

}
