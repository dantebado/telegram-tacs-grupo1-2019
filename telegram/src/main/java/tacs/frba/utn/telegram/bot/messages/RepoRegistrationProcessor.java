package tacs.frba.utn.telegram.bot.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.RepoRegistrationLayout;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
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
			Calendar calendar = Calendar.getInstance();
			
			Calendar todayAux = calendar.getInstance();
			Boolean validOption = true;
			
			switch(date) {
				case "El día de hoy":
					calendar.set(Calendar.YEAR, todayAux.get(Calendar.YEAR));
					calendar.set(Calendar.MONTH, todayAux.get(Calendar.MONTH));
					calendar.set(Calendar.DAY_OF_MONTH, todayAux.get(Calendar.DAY_OF_MONTH));
					
					res_str += "el día de hoy son ";
					break;
				case "Los últimos tres días":
					{
						long todayTimestamp = todayAux.getTimeInMillis();
						long finalTimestamp = todayTimestamp - (1000 * 60 * 60 * 24 * 3);
						
						calendar.setTimeInMillis(finalTimestamp);					
						res_str += "hace tres días son ";
					}
					break;
				case "La última semana":
					{
						long todayTimestamp = todayAux.getTimeInMillis();
						long finalTimestamp = todayTimestamp - (1000 * 60 * 60 * 24 * (todayAux.get(Calendar.DAY_OF_WEEK) + 7));
						
						calendar.setTimeInMillis(finalTimestamp);	
						res_str += "la semana pasada son ";
					}
					break;
				case "El último mes":
					{
						long todayTimestamp = todayAux.getTimeInMillis();
						long finalTimestamp = todayTimestamp;
						for (int i = 0; i < 60; i++) {
							finalTimestamp -= (1000 * 60 * 24 * todayAux.get(Calendar.DAY_OF_MONTH));
						}
						
						calendar.setTimeInMillis(finalTimestamp);	
						int daysLeft = calendar.get(Calendar.DAY_OF_MONTH) - 1;
						for (int i = 0; i < 60; i++) {
							finalTimestamp -= (1000 * 60 * 24 * daysLeft);
						}
						calendar.setTimeInMillis(finalTimestamp);	
						
						res_str += "el último mes son ";
					}
					break;
				case "El inicio de los tiempos":
					{
						long finalTimestamp = 0;
						
						calendar.setTimeInMillis(finalTimestamp);	
						res_str += "el último mes son ";
					}
					res_str += "el inicio de los tiempos son ";
					break;
				default:
					res_str = "La fecha ingresada no es válida, reintentá.";
					message.setText(res_str);
					session.setState(SessionState.ADMIN_REPO_REGISTRATION_AWAITING_DATE);
					RepoRegistrationLayout.setDatesLayout(message);
					validOption = false;
					return;
			}
			
			if(validOption) {				
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
				
				String queryStringDate = dateFormat.format(calendar.getTime());
				ExternalResponse apiResponse = TACSConnector.getRepoAnalytics(queryStringDate, session);
				if(apiResponse.getCode() == 200) {
					res_str += apiResponse.getResponseJson().get("repository_counter").getAsInt();
				} else {
					res_str = "Error en el request";
				}
			}
			
			res_str += ". ¿Qué hacemos a continuación?";
			message.setText(res_str);
			MenuProcessor.refreshMainMenu(session, update, message);
		}
	}

}
