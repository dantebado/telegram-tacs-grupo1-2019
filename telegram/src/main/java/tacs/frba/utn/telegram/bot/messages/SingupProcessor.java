package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.PreInitLayout;
import tacs.frba.utn.telegram.external.ExternalRequest;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.User;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;
import utils.JsonTransformer;

public class SingupProcessor {
	
	public static void processUpdateOnInit(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá tu nombre:");
		session.setState(SessionState.SINGUP_AWAITING_FIRSTNAME);
	}
	
	public static void processUpdateFirstName(UserSession session, Update update, SendMessage message) {
		String firstname = update.getMessage().getText();
		session.addToCache("firstname", firstname);
		
		message.setText("Hola " + firstname + ", ahora ingresá tu apellido:");
		session.setState(SessionState.SINGUP_AWAITING_LASTNAME);
	}
	
	public static void processUpdateLastName(UserSession session, Update update, SendMessage message) {
		String lastname = update.getMessage().getText();
		session.addToCache("lastname", lastname);
		String firstname = (String) session.getFromCache("firstname");
		
		message.setText("Bien " + firstname + ". Ahora ingresá un nombre de usuario para autenticarte en la app:");
		session.setState(SessionState.SINGUP_AWAITING_USERNAME);
	}
	
	public static void processUpdateUserName(UserSession session, Update update, SendMessage message) {
		String username = update.getMessage().getText();
		
		session.addToCache("username", username);
		message.setText("¡Excelente nombre! Por último decinos con qué contraseña querés iniciar sesión:");
		session.setState(SessionState.SINGUP_AWAITING_PASSWORD);
	}
	
	public static void processUpdatePassword(UserSession session, Update update, SendMessage message) {
		String firstname = (String) session.removeFromCache("firstname");
		String lastname = (String) session.removeFromCache("lastname");
		String username = (String) session.removeFromCache("username");
		String password = update.getMessage().getText();
		
		User user = new User(username, password, false);
		user.setName(firstname);
		user.setLastName(lastname);
		
		ExternalResponse result = TACSConnector.trySignup(user);
		if(result.getCode() == 409) {
			session.addToCache("firstname", firstname);
			session.addToCache("lastname", lastname);
			message.setText("Ese nombre de usuario no está disponible. Intentá con uno nuevo. Ingresalo ahora.");
			PreInitLayout.setLayout(message);
			session.setState(SessionState.SINGUP_AWAITING_USERNAME);
		} else {
			message.setText("¡Tu cuenta ya fue creada! Te invitamos a iniciar sesión y comenzar a utilizar la app.");
			PreInitLayout.setLayout(message);
			session.setState(SessionState.AWAITING_INIT);
		}
		
	}

}
