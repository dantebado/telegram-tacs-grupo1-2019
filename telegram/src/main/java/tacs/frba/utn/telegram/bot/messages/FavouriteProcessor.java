package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.bot.layouts.FavouriteLayout;
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
		message.setText("Tu lista de Favoritos:\n\nSeleccioná tu siguiente acción.");
		MenuProcessor.refreshMainMenu(session, update, message);
	}
	
	public static void addFavourite(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresa el ID de repositorio que querés añadir a tu lista o podes *cancelar* la operación:");
		session.setState(SessionState.FAVOURITES_ADD_AWAITING_ID);
	}
	
	public static void addFavouriteAction(UserSession session, Update update, SendMessage message) {
		String repoId = update.getMessage().getText();
		
		if(repoId.equalsIgnoreCase("Cancelar")) {
			message.setText("Operación cancelada. Podés continuar utilizando la app.");
		} else {
			message.setText("Añadido repo " + repoId + " a tu lista de favoritos. Ya podés continuar operando.");
		}
		
		MenuProcessor.refreshMainMenu(session, update, message);
	}
	
	public static void removeFavourite(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresa el ID de repositorio que querés eliminar de tu lista o podes *cancelar* la operación:");
		session.setState(SessionState.FAVOURITES_REMOVE_AWAITING_ID);
	}
	
	public static void removeFavouriteAction(UserSession session, Update update, SendMessage message) {
		String repoId = update.getMessage().getText();
		
		if(repoId.equalsIgnoreCase("Cancelar")) {
			message.setText("Ningún favorito será eliminado. Podemos continuar utilizando la app.");
		} else {
			message.setText("Eliminado el repo " + repoId + " a tu lista de favoritos. Continuá utilizando la app.");
		}
		
		MenuProcessor.refreshMainMenu(session, update, message);
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
