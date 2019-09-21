package tacs.frba.utn.telegram.external;

import tacs.frba.utn.telegram.user.UserSession;
import utils.JsonTransformer;

public class TACSConnector {
	
	public static Boolean tryLogin(String username, String password, UserSession session) {
		ExternalUser user = new ExternalUser(username, password);
		
		String data = JsonTransformer.getGson().toJson(user);
		ExternalResponse reqResponse = ExternalRequest.getAPI("login", null, data);
		
		session.setCookie(reqResponse.getResponseJson().get("cookie").getAsString());
		System.out.println(session.getCookie());
		
		return reqResponse.getResponseJson().get("success").getAsBoolean();
	}
	
	public static Boolean isUserAdmin(String username) {
		return username.equals("silvisuca");
	}
	
	public static Boolean userExists(String id) {
		return true;
	}
	
	public static Boolean repoExist(String repo) {
		return true;
	}

}
