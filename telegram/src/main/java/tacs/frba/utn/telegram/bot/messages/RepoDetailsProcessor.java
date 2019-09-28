package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class RepoDetailsProcessor {
	
	public static void processInit(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá el ID del Repositorio que deseás consultar:");
		session.setState(SessionState.REPO_SEARCH_AWAITING_ID);
	}
	
	public static void processQuery(UserSession session, Update update, SendMessage message) {
		String repoId = update.getMessage().getText();
		
		ExternalResponse apiResponse = TACSConnector.getRepoDetails(repoId, session);
		String msgString = "";
		
		if(apiResponse.getCode() == 200) {
			msgString += "Detalles del Repositorio #" + repoId +
					"\nNombre: " + apiResponse.getResponseJson().get("name").getAsString() +
					"\nNombre Completo: " + apiResponse.getResponseJson().get("full_name").getAsString();
		} else {
			msgString += "El repositorio no se encuentra.";
		}
		msgString += "\n\n¿Qué querés hacer ahora?";
		message.setText(msgString);
		MenuProcessor.refreshMainMenu(session, update, message);
	}

}
