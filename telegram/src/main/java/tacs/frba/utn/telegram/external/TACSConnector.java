package tacs.frba.utn.telegram.external;

import tacs.frba.utn.telegram.user.User;
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
	
	public static Boolean isUserAdmin(String username, UserSession adminSession) {
		return getDataOnUser(username, adminSession).getCode() == 200;
	}
	
	public static ExternalResponse getRepoAnalytics(String dateString, UserSession adminSession) {
		return ExternalRequest.getAPI("admin/analytics?fechaDesde=" + dateString, adminSession.getCookie(), null);
	}
	
	public static Boolean userExists(String username, UserSession session) {
		return getDataOnUser(username, session).getCode() == 200;
	}

	public static ExternalResponse getDataOnUser(String user, UserSession session) {
		return ExternalRequest.getAPI("admin/users/" + user, session.getCookie(), null);
	}

	public static ExternalResponse trySignup(User user) {
		String data = JsonTransformer.getGson().toJson(user);
		return ExternalRequest.postAPI("signup", null, data);
	}

	public static ExternalResponse queryRepoAsAdmin(String repo, UserSession session) {
		return ExternalRequest.getAPI("admin/repositories/" + repo, session.getCookie(), null);
	}

	public static ExternalResponse getRepoDetails(String repoId, UserSession session) {
		return ExternalRequest.getAPI("user/repositories/" + repoId, session.getCookie(), null);
	}
	
	public static ExternalResponse getFavouritesDetails(UserSession session) {
		return ExternalRequest.getAPI("user/favourites", session.getCookie(), null);
	}

}
