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
			default:
		        GenericProcessor.processUpdate(session, update, sendMessage);
				break;
		}
		
		return sendMessage;
	}
	
}
