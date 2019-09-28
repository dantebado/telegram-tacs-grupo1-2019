package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class FavouritesComparatorProcessor {
	
	public static void processAskOne(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá un usuario para comparar sus favoritos contra otro. También podés *cancelar* la operación.");
		session.setState(SessionState.ADMIN_COMPARE_AWAITING_ONE);
	}
	
	public static void processAskAnother(UserSession session, Update update, SendMessage message) {
		String oneUser = update.getMessage().getText();
		
		if(!oneUser.equalsIgnoreCase("cancelar")) {
			if(TACSConnector.userExists(oneUser, session)) {
				session.addToCache("comparator_1", oneUser);
				message.setText("¿Contra qué usuario querés comparar a " + oneUser + "? También podés *cancelar* la operación.");
				session.setState(SessionState.ADMIN_COMPARE_AWAITING_TWO);
			} else {
				message.setText("Ese usuario no existe, reintentá. También podés *cancelar* la operación.");
				session.setState(SessionState.ADMIN_COMPARE_AWAITING_ONE);
			}
		} else {
			message.setText("Operación cancelada. Seleccioná la siguiente acción para ejecutar.");
			MenuProcessor.refreshMainMenu(session, update, message);
		}		
	}
	
	public static void processComparate(UserSession session, Update update, SendMessage message) {
		String anotherUser = update.getMessage().getText();
		
		if(!anotherUser.equalsIgnoreCase("cancelar")) {
			if(TACSConnector.userExists(anotherUser, session)) {
				String oneUser = (String) session.removeFromCache("comparator_1");
				
				if(oneUser.equals(anotherUser)) {
					session.addToCache("comparator_1", oneUser);
					message.setText("El usuario que ingresaste es el mismo que el primero. Seleccioná uno diferente. También podés *cancelar* la operación.");
					session.setState(SessionState.ADMIN_COMPARE_AWAITING_TWO);
				} else {
					message.setText("Esta es la comparación entre los usuarios " + oneUser + " y " + anotherUser + ".\n¿Qué hacemos ahora?");
					MenuProcessor.refreshMainMenu(session, update, message);
				}
				
			} else {
				message.setText("Ese usuario no existe, reintentá ingresarlo. También podés *cancelar* la operación.");
				session.setState(SessionState.ADMIN_COMPARE_AWAITING_TWO);
			}
		} else {
			session.removeFromCache("comparator_1");
			message.setText("Operación cancelada. Seleccioná qué querés hacer ahora.");
			MenuProcessor.refreshMainMenu(session, update, message);
		}		
	}

}
