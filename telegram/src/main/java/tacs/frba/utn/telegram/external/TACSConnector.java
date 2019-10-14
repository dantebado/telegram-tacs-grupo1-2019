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
		return ExternalRequest.postAPI("logout", session.getCookie(), null);
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
	
	public static ExternalResponse addRepo(String favId, String repoId, UserSession session) {
		return ExternalRequest.putAPI("user/favourites/" + favId + "/repositories/" + repoId, session.getCookie(), null);
	}
	
	public static ExternalResponse deleteRepo(String favId, String repoId, UserSession session) {
		return ExternalRequest.deleteAPI("user/favourites/" + favId + "/repositories/" + repoId, session.getCookie(), null);
	}
	
	public static ExternalResponse comparateRepos(String oneRepo, String otherRepo, UserSession session) {
		return ExternalRequest.getAPI("admin/favourites/" + oneRepo + "/" + otherRepo, session.getCookie(), null);
	}
	
	public static ExternalResponse getAdvancedSearch(UserSession session) {
		String query = (String)session.getFromCache("query");
		String name = (String)session.getFromCache("name");
		String language = (String)session.getFromCache("language");
		String sort = (String)session.getFromCache("sort");
		String order = (String)session.getFromCache("order");
		
		String uri = "user/github/repositories?";
		boolean atLeastOneField = false;
		
		if(query != null) {
			atLeastOneField = true;
			uri += "q=" + query;
		}
		if(name != null) {
			if(atLeastOneField) {
				uri += "&";
			}else {
				atLeastOneField = true;
			}
			uri += "name=" + name;
		}
		if(language != null) {
			if(atLeastOneField) {
				uri += "&";
			}else {
				atLeastOneField = true;
			}
			uri += "language=" + language;
		}
		if(sort != null) {
			if(atLeastOneField) {
				uri += "&";
			}else {
				atLeastOneField = true;
			}
			uri += "sort=" + sort;
		}
		if(order != null) {
			if(atLeastOneField) {
				uri += "&";
			}else {
				atLeastOneField = true;
			}
			uri += "order=" + order;
		}

		return ExternalRequest.getAPI(uri, session.getCookie(), null);
	}

}
