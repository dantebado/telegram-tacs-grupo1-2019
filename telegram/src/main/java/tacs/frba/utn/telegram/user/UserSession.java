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
		FAVOURITES_CLEAR_AWAITING_CONFIRMATION
	}
	
	private long chatId;
	private long lastQuery;
	private User user;
	private SessionState state;
	
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

}
