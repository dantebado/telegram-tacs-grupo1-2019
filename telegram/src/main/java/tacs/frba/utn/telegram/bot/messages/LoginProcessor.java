package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import tacs.frba.utn.telegram.bot.Bot;
import tacs.frba.utn.telegram.bot.layouts.MenuLayout;
import tacs.frba.utn.telegram.bot.layouts.PreInitLayout;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.SessionsManager;
import tacs.frba.utn.telegram.user.User;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class LoginProcessor {
	
	public static void processUpdateOnPreInit(UserSession session, Update update, SendMessage message) {
		message.setText("Bienvenid@");
		PreInitLayout.setLayout(message);
		session.setState(SessionState.AWAITING_INIT);
	}
	
	public static void processUpdateOnInit(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá tu nombre de usuario:");
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
		DeleteMessage dm = new DeleteMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
		try {
			Bot.getBot().execute(dm);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		
		String username = (String) session.getFromCache("username");
		
		//Realizar login
		ExternalResponse apiResponse = TACSConnector.tryLogin(username, update.getMessage().getText(), session);
		
		if(apiResponse.getCode() == 200) {
			message.setText("Bienvenid@ @" + username + "!! ¿Qué querés hacer hoy?");
			session.setCookie(apiResponse.getCookie("id"));
			session.removeFromCache("username");
			session.removeFromCache("loginTries");
			session.setUser(new User(username, update.getMessage().getText(), TACSConnector.isUserAdmin(username, session)));			
			MenuProcessor.refreshMainMenu(session, update, message);
		} else if(apiResponse.getCode() == 401) {
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
		} else {
			message.setText("Se produjo un error.");
			PreInitLayout.setLayout(message);
			session.setState(SessionState.AWAITING_INIT);
		}
	}
	
	public static void processLogout(UserSession session, Update update, SendMessage message) {
		ExternalResponse apiResponse = TACSConnector.tryLogout(session);
		
		if(apiResponse.getCode() == 200) {
			SessionsManager.getManager().terminateSession(update.getMessage().getChatId());
			message.setText("@" + session.getUser().getUsername() + ", sesión cerrada. ¡Vuelva pronto!");
		} else {
			SessionsManager.getManager().terminateSession(update.getMessage().getChatId());
			message.setText("Se produjo un error al cerrar la sesión.");
		}
	}

}
