package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.MenuLayout;
import tacs.frba.utn.telegram.bot.layouts.PreInitLayout;
import tacs.frba.utn.telegram.user.SessionsManager;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class MenuProcessor {
	
	public static void processUpdate(UserSession session, Update update, SendMessage message) {
		String messageContent = update.getMessage().getText();
		
		switch(messageContent) {
			case "Logout":
				SessionsManager.getManager().terminateSession(update.getMessage().getChatId());
				message.setText("Sesión cerrada. ¡Vuelva pronto!");
				break;
			default:
				message.setText("Opción incorrecta o no soportada. Reintente.");
				session.setState(SessionState.AWAITING_MENU);
				if(session.getUser().getIsAdmin()) {
					MenuLayout.setAdminLayout(message);
				} else {
					MenuLayout.setUserLayout(message);
				}
				break;
		}
	}

}
