package tacs.frba.utn.telegram.user;

import java.util.HashMap;

public class UserSession {
	
	public enum SessionState {
		AWAITING_PRE_INIT,
		AWAITING_INIT,
		AWAITING_USERNAME,
		AWAITING_PASSWORD,
		AWAITING_MENU,
		REPO_SEARCH_AWAITING_ID,
		FAVOURITES_AWAITING_OP,
		FAVOURITES_ADD_AWAITING_ID,
		FAVOURITES_REMOVE_AWAITING_ID,
		FAVOURITES_CLEAR_AWAITING_CONFIRMATION,
		ADMIN_VIEW_USER_AWAITING_ID,
		ADMIN_COMPARE_AWAITING_ONE,
		ADMIN_COMPARE_AWAITING_TWO,
		ADMIN_REPO_AWAITING_ID,
		ADMIN_REPO_REGISTRATION_AWAITING_DATE,
		SINGUP_AWAITING_FIRSTNAME,
		SINGUP_AWAITING_LASTNAME,
		SINGUP_AWAITING_USERNAME,
		SINGUP_AWAITING_PASSWORD
	}
	
	private long chatId;
	private long lastQuery;
	private User user;
	private SessionState state;
	private String cookie;
	
	HashMap<String, Object> cacheMemory;
	
	public UserSession(long chatId, User user) {
		this.chatId = chatId;
		this.user = user;
		refreshLastQuery();
		cacheMemory = new HashMap<String, Object>();
		state = SessionState.AWAITING_PRE_INIT;
	}
	
	public void refreshLastQuery() {
		lastQuery = System.currentTimeMillis();
	}

	public User getUser() {
		return user;
	}

	public SessionState getState() {
		return state;
	}

	public void setState(SessionState state) {
		this.state = state;
	}
	
	public Object getFromCache(String key) {
		return cacheMemory.get(key);
	}
	
	public Object removeFromCache(String key) {
		return cacheMemory.remove(key);
	}
	
	public Object addToCache(String key, Object value) {
		return cacheMemory.put(key, value);
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
