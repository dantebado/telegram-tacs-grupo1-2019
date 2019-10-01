package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class UserProcessor {
	
	public static void processUserDetails(UserSession session, Update update, SendMessage message) {
		if(PrivilegesProcessor.checkAdminPrivileges(session, update, message)) {
			message.setText("Ingresá el usuario que querés consultar o podés *cancelar* la operación.");
			session.setState(SessionState.ADMIN_VIEW_USER_AWAITING_ID);
		}
	}
	
	public static void processUserQuery(UserSession session, Update update, SendMessage message) {
		if(PrivilegesProcessor.checkAdminPrivileges(session, update, message)) {
			String user = update.getMessage().getText();
			if(user.equalsIgnoreCase("cancelar")) {
				message.setText("¿Qué querés hacer ahora?");
				MenuProcessor.refreshMainMenu(session, update, message);
			} else {				
				ExternalResponse apiResponse = TACSConnector.getDataOnUser(user, session);
				if(apiResponse.getCode() == 404) {
					message.setText("El usuario " + user + " no existe.\nPodés continuar operando.");
				} else {
					JsonObject userData = apiResponse.getResponseData().getAsJsonObject();
					JsonArray languagesArr = userData.get("languages").getAsJsonArray();
					String dataShow = "*Username:* " + userData.get("username").getAsString() +
							"\n*Último acceso:* " + userData.get("lastAccessTime").getAsString() +
							"\n*Cantidad de repositorios favoritos:* " + userData.get("favourite_count").getAsString() +
							"\n*Lenguajes favoritos:*";
					for (JsonElement language : languagesArr) {
						dataShow += " " + language.getAsString();
					}
					if(languagesArr.size() == 0) {
						dataShow += " No posee";
					}

					message.setText("Detalles de usuario\n" + dataShow+ "\n\nPodés continuar operando.");
				}
				
				MenuProcessor.refreshMainMenu(session, update, message);
			}
		}
	}
	
}
