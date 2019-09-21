package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class PrivilegesProcessor {
	
	public static Boolean checkAdminPrivileges(UserSession session, Update update, SendMessage message) {
		if(session.getUser().getIsAdmin()) {
			return true;
		} else {
			message.setText("No tenés permisos para realizar esa acción. Seleccioná una diferente.");
			session.setState(SessionState.FAVOURITES_AWAITING_OP);
			FavouriteLayout.setInitialLayout(message);
			return false;
		}		
	}

}
