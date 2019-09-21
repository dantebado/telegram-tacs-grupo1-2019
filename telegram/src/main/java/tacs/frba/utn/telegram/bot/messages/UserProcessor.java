package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class UserProcessor {
	
	public static void processUserDetails(UserSession session, Update update, SendMessage message) {
		if(PrivilegesProcessor.checkAdminPrivileges(session, update, message)) {
			message.setText("Ingresá el usuario que querés consultar o podés *cancelar* la operación.");
			session.setState(SessionState.ADMIN_VIEW_USER_AWAITING_ID);
		}
	}
	
	public static void processUserQuery(UserSession session, Update update, SendMessage message) {
		if(PrivilegesProcessor.checkAdminPrivileges(session, update, message)) {
			String user = update.getMessage().getText();
			if(user.equalsIgnoreCase("cancelar")) {
				message.setText("¿Qué querés hacer ahora?");
				MenuProcessor.refreshMainMenu(session, update, message);
			} else {
				message.setText("Detalles de usuario " + user + ".\nPodés continuar operando.");
				MenuProcessor.refreshMainMenu(session, update, message);
			}
		}
	}
	
}
