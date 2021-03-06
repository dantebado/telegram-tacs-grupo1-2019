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
			case "Ver detalles de un Repositorio":
				RepoDetailsProcessor.processInit(session, update, message);
				break;
			case "Administrar mis Favoritos":
				FavouriteProcessor.processInit(session, update, message);
				break;
				
			case "Ver detalles de un Usuario":
				UserProcessor.processUserDetails(session, update, message);
				break;
			case "Comparar Favoritos de dos Usuarios":
				FavouritesComparatorProcessor.processAskOne(session, update, message);;
				break;
			case "Ver popularidad de un Repositorio":
				RepoPopularityProcessor.processAskRepo(session, update, message);
				break;
				
			case "Ver Repositorios registrados":
				RepoRegistrationProcessor.processMenu(session, update, message);
				break;
				
			case "Realizar una Búsqueda Avanzada":
				AdvancedSearchProcessor.processAdvancedSearchInit(session, update, message);
				break;
				
			case "Logout":
				LoginProcessor.processLogout(session, update, message);
				break;				
				
			default:
				message.setText("Opción incorrecta o no soportada. Reintente.");
				refreshMainMenu(session, update, message);
				break;
		}
	}
	
	public static void refreshMainMenu(UserSession session, Update update, SendMessage message) {
		session.setState(SessionState.AWAITING_MENU);
		if(session.getUser().getIsAdmin()) {
			MenuLayout.setAdminLayout(message);
		} else {
			MenuLayout.setUserLayout(message);
		}
	}

}
