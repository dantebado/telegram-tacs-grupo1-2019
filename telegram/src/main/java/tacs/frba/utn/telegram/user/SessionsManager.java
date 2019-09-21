package tacs.frba.utn.telegram.user;

import java.util.HashMap;

public class SessionsManager {
	
	private static SessionsManager instance;
	private HashMap<Long, UserSession> sessionsMap;
	
	public SessionsManager() {
		sessionsMap = new HashMap<Long, UserSession>();
	}
	
	public static SessionsManager getManager() {
		if(instance == null) {
			instance = new SessionsManager();
		}
		return instance;
	}
	
	public UserSession findSession(long chatId) {
		return sessionsMap.get(chatId);
	}
	
	public UserSession registerSession(Long chatId, UserSession session) {
		sessionsMap.put(chatId, session);
		return session;
	}
	
	public void terminateSession(Long chatId) {
		sessionsMap.remove(chatId);
	}

}
