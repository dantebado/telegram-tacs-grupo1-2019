package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.MenuLayout;
import tacs.frba.utn.telegram.bot.layouts.PreInitLayout;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class LoginProcessor {
	
	public static void processUpdateOnPreInit(UserSession session, Update update, SendMessage message) {
		message.setText("Bienvenid@");
		PreInitLayout.setLayout(message);
		session.setState(SessionState.AWAITING_INIT);
	}
	
	public static void processUpdateOnInit(UserSession session, Update update, SendMessage message) {
		message.setText("Ingrese su nombre de usuario:");
		session.setState(SessionState.AWAITING_USERNAME);
	}
	
	public static void processUpdateOnUsername(UserSession session, Update update, SendMessage message) {
		String username = update.getMessage().getText();
		message.setText("Hola @" + username + ", ingresá tu contraseña:");
		session.addToCache("username", username);
		session.addToCache("loginTries", 0);
		session.setState(SessionState.AWAITING_PASSWORD);
	}
	
	public static void processUpdateOnPassword(UserSession session, Update update, SendMessage message) {
		String username = (String) session.getFromCache("username");
		
		//Realizar login
		boolean loginSuccess = true;
		
		if(loginSuccess) {
			message.setText("Bienvenid@ @" + username + "!! ¿Qué querés hacer hoy?");
			session.removeFromCache("username");
			session.removeFromCache("loginTries");
			session.setState(SessionState.AWAITING_MENU);
			if(session.getUser().getIsAdmin()) {
				MenuLayout.setAdminLayout(message);
			} else {
				MenuLayout.setUserLayout(message);
			}
		} else {
			int tries = (Integer) session.removeFromCache("loginTries");
			tries++;			
			if(tries >= 3) {
				session.removeFromCache("username");
				message.setText("Superaste el límite de intentos. Comenzá el login nuevamente.");
				PreInitLayout.setLayout(message);
				session.setState(SessionState.AWAITING_INIT);
			} else {
				session.addToCache("loginTries", tries);
				message.setText("La contaseña ingresada no es correcta, reintentá:");
				session.setState(SessionState.AWAITING_PASSWORD);
			}
		}
	}

}
