package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.RepoRegistrationLayout;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class RepoRegistrationProcessor {
	
	public static void processMenu(UserSession session, Update update, SendMessage message) {
		message.setText("¿Desde qué fecha querés contar los repositorios registrados?");
		session.setState(SessionState.ADMIN_REPO_REGISTRATION_AWAITING_DATE);
		RepoRegistrationLayout.setDatesLayout(message);
	}
	
	public static void processQuery(UserSession session, Update update, SendMessage message) {
		String date = update.getMessage().getText();
		
		if(date.equalsIgnoreCase("volver")) {
			message.setText("¿Qué deseás hacer ahora?");
			MenuProcessor.refreshMainMenu(session, update, message);
		} else {
			String res_str = "Los repostorios registrados desde ";
			switch(date) {
				case "El día de hoy":
					res_str += "el día de hoy son ###";
					break;
				case "Los últimos tres días":
					res_str += "hace tres días son ###";
					break;
				case "La última semana":
					res_str += "la semana pasada son ###";
					break;
				case "El último mes":
					res_str += "el último mes son ###";
					break;
				case "El inicio de los tiempos":
					res_str += "el inicio de los tiempos son ###";
					break;
				default:
					res_str = "La fecha ingresada no es válida, reintentá.";
					message.setText(res_str);
					session.setState(SessionState.ADMIN_REPO_REGISTRATION_AWAITING_DATE);
					RepoRegistrationLayout.setDatesLayout(message);
					return;
			}
			res_str += ". ¿Qué hacemos a continuación?";
			message.setText(res_str);
			MenuProcessor.refreshMainMenu(session, update, message);
		}
	}

}
