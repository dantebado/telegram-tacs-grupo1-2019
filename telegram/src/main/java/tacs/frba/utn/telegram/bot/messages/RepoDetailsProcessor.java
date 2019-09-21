package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class RepoDetailsProcessor {
	
	public static void processInit(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá el ID del Repositorio que deseás consultar:");
		session.setState(SessionState.REPO_SEARCH_AWAITING_ID);
	}
	
	public static void processQuery(UserSession session, Update update, SendMessage message) {
		String repoId = update.getMessage().getText();
		message.setText("Detalles del repo " + repoId + "\n¿Qué querés hacer ahora?");
		MenuProcessor.refreshMainMenu(session, update, message);
	}

}
