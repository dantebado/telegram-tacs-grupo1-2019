package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class FavouritesComparatorProcessor {
	
	public static void processAskOne(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá un usuario (*el id*) para comparar sus favoritos contra otro. También podés *cancelar* la operación.");
		session.setState(SessionState.ADMIN_COMPARE_AWAITING_ONE);
	}
	
	public static void processAskAnother(UserSession session, Update update, SendMessage message) {
		String oneUser = update.getMessage().getText();
		
		if(!oneUser.equalsIgnoreCase("cancelar")) {
			if(TACSConnector.userExists(oneUser, session)) {
				session.addToCache("comparator_1", oneUser);
				message.setText("¿Contra qué usuario (*el id*) querés comparar a " + oneUser + "? También podés *cancelar* la operación.");
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
					ExternalResponse apiResponse = TACSConnector.comparateRepos(oneUser, anotherUser, session);
					session.removeFromCache("comparator_1");

					if(apiResponse.getCode() == 200) {
						JsonArray reposArray = apiResponse.getResponseData().getAsJsonObject().get("repositorios").getAsJsonArray();
						JsonArray langsArray = apiResponse.getResponseData().getAsJsonObject().get("langs").getAsJsonArray();
						
						String dataShow = "Repositorios en común:\n";
						for (JsonElement repo : reposArray) {
							dataShow += "\n*ID del repositorio:* " + repo.getAsJsonObject().get("id").getAsString() +
									"\n*FechaDeRegistro:* " + repo.getAsJsonObject().get("registerDate").getAsString() +
									"\n*Lenguaje:* " + repo.getAsJsonObject().get("language").getAsString() +
									"\n";
						}
						if(reposArray.size() == 0) {
							dataShow += " No hay repositorios en común";
						}
						dataShow += "\nLenguajes en común:\n\n";
						for (JsonElement language : langsArray) {
							dataShow += "\n*Lenguaje:* " + language.getAsString() +
									"\n";
						}
						if(langsArray.size() == 0) {
							dataShow += " No hay lenguajes en común";
						}
						
						message.setText(dataShow);
						MenuProcessor.refreshMainMenu(session, update, message);
					}else {
						message.setText("No se ha podido comparar");
						MenuProcessor.refreshMainMenu(session, update, message);
					}
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
