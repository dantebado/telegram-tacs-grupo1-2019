package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class RepoPopularityProcessor {
	
	public static void processAskRepo(UserSession session, Update update, SendMessage message) {
		message.setText("¿Qué repo deseás consultar? Podés también *cancelar* la consulta.");
		session.setState(SessionState.ADMIN_REPO_AWAITING_ID);
	}
	
	public static void processQuery(UserSession session, Update update, SendMessage message) {
		String repo = update.getMessage().getText();
		
		if(repo.equalsIgnoreCase("cancelar")) {
			message.setText("La consulta fue cancelada. ¿Cuál es la próxima operación?");
			MenuProcessor.refreshMainMenu(session, update, message);
		} else {
			if(TACSConnector.repoExist(repo)) {				
				message.setText("Los usuarios que añadieron el repo " + repo + " a sus favoritos son .....\n¿Qué hacemos a continuación?");
				MenuProcessor.refreshMainMenu(session, update, message);				
			} else {
				message.setText("El repo ingresado no existe, reintentá. Podés también *cancelar* la consulta.");
				session.setState(SessionState.ADMIN_REPO_AWAITING_ID);
			}
		}
	}

}
