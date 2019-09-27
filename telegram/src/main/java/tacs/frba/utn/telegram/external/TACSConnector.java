package tacs.frba.utn.telegram.external;

import tacs.frba.utn.telegram.user.UserSession;
import utils.JsonTransformer;

public class TACSConnector {
	
	public static ExternalResponse tryLogin(String username, String password, UserSession session) {
		ExternalUser user = new ExternalUser(username, password);
		
		String data = JsonTransformer.getGson().toJson(user);
		return ExternalRequest.postAPI("login", null, data);
	}
	
	public static ExternalResponse tryLogout(UserSession session) {
		return ExternalRequest.postAPI("logout", null, null);
	}
	
	public static Boolean isUserAdmin(String username) {
		return false;
	}
	
	public static Boolean userExists(String id) {
		return true;
	}
	
	public static Boolean repoExist(String repo) {
		return true;
	}
	
	public static Boolean existsUserWithUsername(String username) {
		return false;
	}

}
