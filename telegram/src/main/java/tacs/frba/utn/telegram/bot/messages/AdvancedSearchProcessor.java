package tacs.frba.utn.telegram.bot.messages;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import tacs.frba.utn.telegram.bot.layouts.PreInitLayout;
import tacs.frba.utn.telegram.external.ExternalRequest;
import tacs.frba.utn.telegram.external.ExternalResponse;
import tacs.frba.utn.telegram.external.TACSConnector;
import tacs.frba.utn.telegram.user.User;
import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;
import utils.JsonTransformer;

public class AdvancedSearchProcessor {
	
	public static void processAdvancedSearchInit(UserSession session, Update update, SendMessage message) {
		message.setText("Ingresá tu query (Podes *omitir* este parámetro):");
		session.setState(SessionState.ADVANCED_SEARCH_AWAITING_QUERY);
	}
	
	public static void processAdvancedSearchQuery(UserSession session, Update update, SendMessage message) {
		String query = update.getMessage().getText();
		if(!query.equalsIgnoreCase("omitir")) {
			session.addToCache("query", query);
		}
		
		message.setText("Ingresá el nombre del repositorio (Podes *omitir* este parámetro):");
		session.setState(SessionState.ADVANCED_SEARCH_AWAITING_NAME);
	}
	
	public static void processAdvancedSearchName(UserSession session, Update update, SendMessage message) {
		String name = update.getMessage().getText();
		if(!name.equalsIgnoreCase("omitir")) {
			session.addToCache("name", name);
		}
		
		message.setText("Ingresá el lenguaje del repositorio (Podes *omitir* este parámetro):");
		session.setState(SessionState.ADVANCED_SEARCH_AWAITING_LANGUAGE);
	}
	
	public static void processAdvancedSearchLanguage(UserSession session, Update update, SendMessage message) {
		String language = update.getMessage().getText();
		if(!language.equalsIgnoreCase("omitir")) {
			session.addToCache("language", language);
		}
		
		message.setText("Ingresá el parámetro de sort (*asc*, *desc*)(Podes *omitir* este parámetro):");
		session.setState(SessionState.ADVANCED_SEARCH_AWAITING_SORT);
	}
	
	public static void processAdvancedSearchSort(UserSession session, Update update, SendMessage message) {
		String sort = update.getMessage().getText();
		if(!sort.equalsIgnoreCase("omitir")) {
			session.addToCache("sort", sort);
		}
		
		message.setText("Ingresá el parámetro de orden (Podes *omitir* este parámetro):");
		session.setState(SessionState.ADVANCED_SEARCH_AWAITING_ORDER);
	}
	
	public static void processAdvancedSearchOrder(UserSession session, Update update, SendMessage message) {
		String order = update.getMessage().getText();
		if(!order.equalsIgnoreCase("omitir")) {
			session.addToCache("order", order);
		}
		
		ExternalResponse apiResponse = TACSConnector.getAdvancedSearch(session);
		session.removeFromCache("query");
		session.removeFromCache("name");
		session.removeFromCache("language");
		session.removeFromCache("sort");
		session.removeFromCache("order");
		
		JsonObject dataResponse = apiResponse.getResponseJson();
		String dataShow = "*Cantidad de repositorios encontrados:* " + dataResponse.get("total_count").getAsInt()
			+ "\n*Resultados (primeros 5):* \n"; 
		JsonArray elements = dataResponse.get("items").getAsJsonArray();
		
		for (int i = 0; i<5 && i<elements.size(); i++) {
			JsonObject element = elements.get(i).getAsJsonObject();
			dataShow += "\n" + element.get("name").getAsString();
		}
		
		message.setText(dataShow);
		MenuProcessor.refreshMainMenu(session, update, message);
	}
}