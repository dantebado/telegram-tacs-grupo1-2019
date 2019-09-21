package tacs.frba.utn.telegram.bot.messages;

import java.io.Serializable;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import tacs.frba.utn.telegram.user.UserSession;
import tacs.frba.utn.telegram.user.UserSession.SessionState;

public class MessageProcessor {

	public static SendMessage processUpdate(UserSession session, Update update) {
		SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId());
        
        session.refreshLastQuery();
        
		switch(session.getState()) {
			case AWAITING_PRE_INIT:
		        LoginProcessor.processUpdateOnPreInit(session, update, sendMessage);
				break;
			case AWAITING_INIT:
		        LoginProcessor.processUpdateOnInit(session, update, sendMessage);
				break;
			case AWAITING_USERNAME:
		        LoginProcessor.processUpdateOnUsername(session, update, sendMessage);
				break;
			case AWAITING_PASSWORD:
		        LoginProcessor.processUpdateOnPassword(session, update, sendMessage);
				break;
			case AWAITING_MENU:
		        MenuProcessor.processUpdate(session, update, sendMessage);
				break;
				
			case REPO_SEARCH_AWAITING_ID:
				RepoDetailsProcessor.processQuery(session, update, sendMessage);
				break;
				
			case FAVOURITES_AWAITING_OP:
				FavouriteProcessor.processQuery(session, update, sendMessage);
				break;
			case FAVOURITES_ADD_AWAITING_ID:
				FavouriteProcessor.addFavouriteAction(session, update, sendMessage);
				break;
			case FAVOURITES_REMOVE_AWAITING_ID:
				FavouriteProcessor.removeFavouriteAction(session, update, sendMessage);
				break;
			case FAVOURITES_CLEAR_AWAITING_CONFIRMATION:
				FavouriteProcessor.clearListConfirmation(session, update, sendMessage);
				break;
				
			case ADMIN_VIEW_USER_AWAITING_ID:
				UserProcessor.processUserQuery(session, update, sendMessage);
				break;
				
			case ADMIN_COMPARE_AWAITING_ONE:
				FavouritesComparatorProcessor.processAskAnother(session, update, sendMessage);
				break;
			case ADMIN_COMPARE_AWAITING_TWO:
				FavouritesComparatorProcessor.processComparate(session, update, sendMessage);
				break;
				
			case ADMIN_REPO_AWAITING_ID:
				RepoPopularityProcessor.processQuery(session, update, sendMessage);
				break;
				
			case ADMIN_REPO_REGISTRATION_AWAITING_DATE:
				RepoRegistrationProcessor.processQuery(session, update, sendMessage);
				break;
				
			default:
		        GenericProcessor.processUpdate(session, update, sendMessage);
				break;
		}
		
		return sendMessage;
	}
	
}
