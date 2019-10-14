package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class FavouriteProcessor {
	
	public static void processInit(UserSession session, Update update, SendMessage message) {
		message.setText("¿Qué deseás hacer sobre tus Favoritos?");
		session.setState(SessionState.FAVOURITES_AWAITING_OP);
		FavouriteLayout.setInitialLayout(message);
	}
	
	public static void processQuery(UserSession session, Update update, SendMessage message) {
		String operation = update.getMessage().getText();
		
		switch(operation) {
			case "Ver Favoritos":
				viewList(session, update, message);
				break;
			case "Añadir un Favorito":
				addFavourite(session, update, message);
				break;
			case "Eliminar un Favorito":
				removeFavourite(session, update, message);
				break;
			case "Vaciar Lista":
				clearList(session, update, message);
				break;
			case "Volver":
				message.setText("¿Qué querés hacer a continuación?");
				MenuProcessor.refreshMainMenu(session, update, message);
				break;
			default:
				message.setText("Opción incorrecta o no soportada. Reintente.");
				MenuProcessor.refreshMainMenu(session, update, message);
				break;
		}
	}
	
	public static void viewList(UserSession session, Update update, SendMessage message) {
		ExternalResponse apiResponse = TACSConnector.getFavouritesDetails(session);
		JsonArray favouriteArray = apiResponse.getResponseData().getAsJsonArray().get(0).getAsJsonObject().get("contents").getAsJsonArray();
		
		String dataShow = "Tu lista de Favoritos:\n\n";
		for (JsonElement favourite : favouriteArray) {
			dataShow += "\n*ID del repositorio:* " + favourite.getAsJsonObject().get("id").getAsString() +
					"\n*FechaDeRegistro:* " + favourite.getAsJsonObject().get("registerDate").getAsString() +
					"\n*Lenguaje:* " + favourite.getAsJsonObject().get("language").getAsString() +
					"\n";
		}
		if(favouriteArray.size() == 0) {
			dataShow += " No posee favoritos";
		}
		
		message.setText("\n\nSeleccioná tu siguiente acción.");
		message.setText("Detalles de usuario\n" + dataShow+ "\n\nPodés continuar operando.");
		
		
		MenuProcessor.refreshMainMenu(session, update, message);
	}
	
	public static void addFavourite(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresa el *ID de repositorio* que querés añadir a una de tus listas");
		session.setState(SessionState.FAVOURITES_ADD_AWAITING_ID);
		
	}
	
	public static void addFavouriteAction(UserSession session, Update update, SendMessage message) {
		String idRepo = update.getMessage().getText();
		session.addToCache("repo_id", idRepo);
		
		message.setText("Ingresá a qué *número de lista* donde querés agregarlo.");
		session.setState(SessionState.FAVOURITES_ADD_AWAITING_FAV_ID);
	}
	
	public static void addFavouriteRepoId(UserSession session, Update update, SendMessage message) {
		String favId = update.getMessage().getText();
		String repoId = (String)session.getFromCache("repo_id");
		
		ExternalResponse apiResponse = TACSConnector.addRepo(favId, repoId, session);
		session.removeFromCache("repo_id");
		if(apiResponse.getCode() == 200) {
			message.setText("Añadido repo " + repoId + " a tu lista " + favId + ". Ya podés continuar operando.");
		}else {
			message.setText("El repo o lista de favoritos ingresado no existe, reintentá. Podés también *cancelar* la consulta.");
			session.setState(SessionState.FAVOURITES_ADD_AWAITING_ID);
		}
		
		MenuProcessor.refreshMainMenu(session, update, message);
	}
	
	public static void removeFavourite(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresa el número de la lista donde se encuentra el repositorio que querés eliminar o podes *cancelar* la operación:");
		session.setState(SessionState.FAVOURITES_REMOVE_AWAITING_ID_FAV);
	}
	
	public static void removeFavouriteAddRepoId(UserSession session, Update update, SendMessage message) {
		String favId = update.getMessage().getText();
		
		if(favId.equalsIgnoreCase("Cancelar")) {
			message.setText("Ningún favorito será eliminado. Podemos continuar utilizando la app.");
			MenuProcessor.refreshMainMenu(session, update, message);
		} else {
			session.addToCache("fav_id", favId);
			
			message.setText("Ingresá el id del repositorio que querés eliminar. Podés *cancelar* la operacion:");
			session.setState(SessionState.FAVOURITES_REMOVE_AWAITING_ID_REPO);
		}

	}
	
	
	public static void removeFavouriteAction(UserSession session, Update update, SendMessage message) {
		String repoId = update.getMessage().getText();
		String favId = (String)session.getFromCache("fav_id");

		ExternalResponse apiResponse = TACSConnector.deleteRepo(favId, repoId, session);
		session.removeFromCache("fav_id");
		if(repoId.equalsIgnoreCase("Cancelar")) {
			message.setText("Ningún favorito será eliminado. Podemos continuar utilizando la app.");
		} else {
			if(apiResponse.getCode() == 200) {
				message.setText("Eliminado el repo " + repoId + " a tu lista de favoritos" + favId + ". Continuá utilizando la app.");
				MenuProcessor.refreshMainMenu(session, update, message);
			}else {
				message.setText("El repo o lista de favoritos ingresado no existe, reintentá. Podés también *cancelar* la consulta.");
				session.setState(SessionState.FAVOURITES_ADD_AWAITING_ID);
			}
		}
		
	}
	
	public static void clearList(UserSession session, Update update, SendMessage message) {
		message.setText("¿Estás seguro que querés vaciar tu lista? Esta operación no se puede deshacer.");
		session.setState(SessionState.FAVOURITES_CLEAR_AWAITING_CONFIRMATION);
		FavouriteLayout.setClearLayout(message);
	}
	
	public static void clearListConfirmation(UserSession session, Update update, SendMessage message) {
		Boolean confirmation = (update.getMessage().getText().equals("Sí"));
		
		if(confirmation) {
			message.setText("Tu lista de favoritos ha sido vaciada. ¡Esperamos que agregues nuevos pronto!\n¿Qué hacemos a continuación?");
		} else {
			message.setText("¡Estuvo cerca!\n¿Qué deseás hacer ahora?");
		}
		
		MenuProcessor.refreshMainMenu(session, update, message);
	}

}
